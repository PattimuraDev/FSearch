package repo.pattimuradev.fsearch.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import repo.pattimuradev.fsearch.model.*

class UserRepository {
    private val firebaseAuth = Firebase.auth
    private val firestoreDb = FirebaseFirestore.getInstance()
    private val firebaseCloudStorage = Firebase.storage

    // live data
    private val checkIfEmailRegistered = MutableLiveData<String>()
    val checkIfEmailRegisteredLiveData : LiveData<String> = checkIfEmailRegistered
    private val otpEmailVerificationResult = MutableLiveData<String>()
    val otpEmailVerificationResultLiveData: LiveData<String> = otpEmailVerificationResult
    private val currentUser = MutableLiveData<FirebaseUser>()
    val currentUserLiveData = currentUser
    private val currentUserProfile = MutableLiveData<UserProfile>()
    val currentUserProfileLiveData = currentUserProfile
    private val allUser = MutableLiveData<List<UserProfile>>()
    val allUserLiveData: LiveData<List<UserProfile>> = allUser
    private val currentUserFotoProfilUrl = MutableLiveData<String>()
    val currentUserFotoProfilUrlLiveData: LiveData<String> = currentUserFotoProfilUrl
    private val updateProfilStatus = MutableLiveData<String>()
    val updateProfilLiveData : LiveData<String> = updateProfilStatus
    private val loginStatus = MutableLiveData<String>()
    val loginStatusLiveData: LiveData<String> = loginStatus
    private val getSpesificUserById = MutableLiveData<UserProfile>()
    val getSpesificUserByIdLiveData : LiveData<UserProfile> = getSpesificUserById
    private val getAllFavoritedUser = MutableLiveData<List<UserProfile>>()
    val getAllFavoritedUserLiveData: LiveData<List<UserProfile>> = getAllFavoritedUser
    private val addFriendResult = MutableLiveData<String>()
    val addFriendResultLiveData : LiveData<String> = addFriendResult
    private val addTeamUpHistory = MutableLiveData<String>()
    val addTeamUpHistoryLiveData: LiveData<String> = addTeamUpHistory
    private val addTestimoni = MutableLiveData<String>()
    val addTestimoniLiveData: LiveData<String> = addTestimoni

    suspend fun registerAccount(account: Account): MutableLiveData<String>{
        val resultMessage = MutableLiveData<String>()
        firebaseAuth.createUserWithEmailAndPassword(account.email, account.password)
            .addOnCompleteListener{ createAccountTask ->
                if(createAccountTask.isSuccessful){
                    val user = firebaseAuth.currentUser
                    val updateDisplayNameUser = userProfileChangeRequest {
                        displayName = account.nama
                    }
                    user!!.updateProfile(updateDisplayNameUser)
                        .addOnCompleteListener{ updateDisplayNameTask ->
                            if(updateDisplayNameTask.isSuccessful){
                                val profilUser = UserProfile(
                                    user.uid,
                                    user.displayName!!,
                                    user.email!!,
                                    null,
                                    0,
                                    0,
                                    null,
                                    null,
                                    arrayListOf(),
                                    0.0F,
                                    true,
                                    arrayListOf(),
                                    arrayListOf(),
                                    arrayListOf()
                                )
                                firestoreDb.collection("user_profile")
                                    .document(profilUser.id!!)
                                    .set(profilUser)
                                    .addOnCompleteListener { saveProfileTask ->
                                        if(saveProfileTask.isSuccessful){
                                            currentUser.postValue(firebaseAuth.currentUser)
                                            resultMessage.postValue("OK")
                                        }
                                    }
                            }else{
                                resultMessage.postValue("FAILED")
                            }
                        }
                }else{
                    resultMessage.postValue("FAILED")
                }
            }
        return resultMessage
    }

    suspend fun updateUser(userProfile: UserProfile){
        val user = firebaseAuth.currentUser
        val userProfileRef = firestoreDb.collection("user_profile").document(user!!.uid)
        userProfileRef.update(
            mapOf(
                "nama" to userProfile.nama,
                "bio" to userProfile.bio,
                "statusBersediaMenerimaAjakan" to userProfile.statusBersediaMenerimaAjakan,
                "dataDiri.asalUniversitas" to userProfile.dataDiri!!.asalUniversitas,
                "dataDiri.tahunAngkatan" to userProfile.dataDiri.tahunAngkatan,
                "dataDiri.fakultas" to userProfile.dataDiri.fakultas,
                "dataDiri.jurusan" to userProfile.dataDiri.jurusan,
                "dataDiri.programStudi" to userProfile.dataDiri.programStudi,
                "dataDiri.keminatan" to userProfile.dataDiri.keminatan,
                "dataDiri.jenisKelamin" to userProfile.dataDiri.jenisKelamin,
                "dataDiri.umur" to userProfile.dataDiri.umur,
                "dataDiri.asalKota" to userProfile.dataDiri.asalKota,
                "dataDiri.kepribadian" to userProfile.dataDiri.kepribadian,
                "urlFoto" to userProfile.urlFoto
            )
        )
            .addOnSuccessListener {
                firestoreDb.collection("user_profile")
                    .document(currentUser.value!!.uid)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        val profile = snapshot.toObject(UserProfile::class.java)
                        currentUserProfile.value = profile
                        updateProfilStatus.postValue("OK")
                    }
            }
            .addOnFailureListener {
                updateProfilStatus.postValue("FAILED")
            }
    }

    suspend fun login(dataLogin: DataLogin){
        firebaseAuth.signInWithEmailAndPassword(dataLogin.email!!, dataLogin.password!!)
            .addOnCompleteListener{ loginTask ->
                if(loginTask.isSuccessful){
                    currentUser.value = firebaseAuth.currentUser
                    firestoreDb.collection("user_profile")
                        .document(currentUser.value!!.uid)
                        .get()
                        .addOnSuccessListener { snapshot ->
                            val profile = snapshot.toObject(UserProfile::class.java)
                            currentUserProfile.value = profile
                            loginStatus.postValue("OK")
                        }
                        .addOnFailureListener {
                            currentUserProfile.value = null
                            loginStatus.postValue("FAILED")
                        }
                }else{
                    loginStatus.postValue("FAILED")
                }
            }
    }

    suspend fun getUserProfilePhotoUrl(fileUri: Uri?, isUploadingImage: Boolean, namaFile: String?){
        if(isUploadingImage){
            val storageRef = firebaseCloudStorage.reference
            val fotoBaruRef = storageRef.child("foto_profil_user/$namaFile")
            val uploadTask = fotoBaruRef.putFile(fileUri!!)
            uploadTask.continueWithTask { getDownloadUrlTask ->
                if(!getDownloadUrlTask.isSuccessful){
                    getDownloadUrlTask.exception?.let {
                        throw it
                    }
                }
                fotoBaruRef.downloadUrl
            }.addOnCompleteListener {  getDownloadTaskStatus ->
                if(getDownloadTaskStatus.isSuccessful){
                    currentUserFotoProfilUrl.postValue(getDownloadTaskStatus.result.toString())
                }else{
                    currentUserFotoProfilUrl.postValue(null)
                }
            }

        }else{
            firestoreDb.collection("user_profile")
                .document(currentUser.value!!.uid)
                .get()
                .addOnSuccessListener { snapshot ->
                    val userProfile = snapshot.toObject(UserProfile::class.java)
                    currentUserFotoProfilUrl.postValue(userProfile!!.urlFoto)
                }
        }
    }

    suspend fun saveOtpEmail(emailVerification: EmailVerification){

        firebaseAuth.fetchSignInMethodsForEmail(emailVerification.email)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val check = !task.result.signInMethods.isNullOrEmpty()
                    if(!check){
                        firestoreDb.collection("email_verification")
                            .add(emailVerification)
                            .addOnSuccessListener {
                                checkIfEmailRegistered.postValue("OK")
                            }
                            .addOnFailureListener {
                                checkIfEmailRegistered.postValue("Failed")
                            }
                    }else{
                        checkIfEmailRegistered.postValue("ALREADY REGISTERED")
                    }
                }
            }
    }

    suspend fun verifyOtpEmail(emailVerification: EmailVerification){
        firestoreDb.collection("email_verification")
            .addSnapshotListener { value, _ ->
                if(!value?.isEmpty!!){
                    value.forEach { item ->
                        val emailVerificationItem = item.toObject(EmailVerification::class.java)
                        if(emailVerificationItem.verificationCode == emailVerification.verificationCode){
                            otpEmailVerificationResult.postValue("OK")
                        }
                    }
                }else{
                    otpEmailVerificationResult.postValue("FAILED")
                }
            }
    }

    fun getSpesificUserById(userId: String) {
        firestoreDb.collection("user_profile")
            .document(userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val profile = snapshot.toObject(UserProfile::class.java)
                getSpesificUserById.postValue(profile)
            }
            .addOnFailureListener {
                getSpesificUserById.value = null
            }
    }

    fun getAllUser(){
        firestoreDb.collection("user_profile")
            .get()
            .addOnSuccessListener { result ->
                val hasil = mutableListOf<UserProfile>()
                for(document in result){
                    val userProfilePengguna = document.toObject(UserProfile::class.java)
                    hasil += userProfilePengguna
                }
                allUser.postValue(hasil)
            }.addOnFailureListener {
                allUser.postValue(null)
            }
    }

    suspend fun getAllUserFavorited(){
        firestoreDb.collection("user_profile")
            .get()
            .addOnSuccessListener { result ->
                val hasil = mutableListOf<UserProfile>()
                for(i in result){
                    val userProfile = i.toObject(UserProfile::class.java)
                    if(userProfile.likedByUserId.contains(currentUser.value!!.uid)){
                        hasil += userProfile
                    }
                }
                getAllFavoritedUser.postValue(hasil)
            }
            .addOnFailureListener {
                getAllFavoritedUser.postValue(null)
            }
    }

    suspend fun addUserFriendList(idUserPengirimPermintaan: String, idUserPenerimaPermintaan: String){
        firestoreDb.collection("user_profile")
            .document(idUserPenerimaPermintaan)
            .get()
            .addOnSuccessListener { userProfile ->
                val userProfilePenerima = userProfile.toObject(UserProfile::class.java)
                if(userProfilePenerima!!.friendListUserId.contains(idUserPengirimPermintaan)){
                    addFriendResult.postValue("ALREADY A FRIEND")
                }else{
                    firestoreDb.collection("user_profile")
                        .document(idUserPenerimaPermintaan)
                        .update(mapOf(
                            "friendListUserId" to FieldValue.arrayUnion(idUserPengirimPermintaan),
                            "jumlahTeman" to FieldValue.increment(1)
                        ))
                        .addOnSuccessListener {
                            firestoreDb.collection("user_profile")
                                .document(idUserPengirimPermintaan)
                                .update(mapOf(
                                    "friendListUserId" to FieldValue.arrayUnion(idUserPenerimaPermintaan),
                                    "jumlahTeman" to FieldValue.increment(1)
                                ))
                                .addOnSuccessListener {
                                    addFriendResult.postValue("OK")
                                }
                                .addOnFailureListener {
                                    addFriendResult.postValue("FAILED")
                                }
                        }
                        .addOnFailureListener {
                            addFriendResult.postValue("FAILED")
                        }
                }
            }
    }

    suspend fun addUserTeamUpHistory(idUserPengirimPermintaan: String, idUserPenerimaPermintaan: String){
        firestoreDb.collection("user_profile")
            .document(idUserPenerimaPermintaan)
            .update(mapOf(
                "teamHistory" to FieldValue.arrayUnion(idUserPengirimPermintaan)
            ))
            .addOnSuccessListener {
                firestoreDb.collection("user_profile")
                    .document(idUserPengirimPermintaan)
                    .update(mapOf(
                        "teamHistory" to FieldValue.arrayUnion(idUserPenerimaPermintaan)
                    ))
                    .addOnSuccessListener {
                        addTeamUpHistory.postValue("OK")
                    }
                    .addOnFailureListener {
                        addTeamUpHistory.postValue("FAILED")
                    }
            }
            .addOnFailureListener {
                addTeamUpHistory.postValue("FAILED")
            }
    }

    suspend fun addTestimoniToAnUser(idUserTarget: String, testimoni: Testimoni){
        firestoreDb.collection("user_profile")
            .document(idUserTarget)
            .get()
            .addOnSuccessListener { userProfile ->
                val userProfileTarget = userProfile.toObject(UserProfile::class.java)
                if(userProfileTarget!!.teamHistory.contains(currentUser.value!!.uid)){
                    firestoreDb.collection("user_profile")
                        .document(idUserTarget)
                        .update(mapOf(
                            "testimoni" to FieldValue.arrayUnion(testimoni)
                        ))
                        .addOnSuccessListener {
                            firestoreDb.collection("user_profile")
                                .document(idUserTarget)
                                .get()
                                .addOnSuccessListener { _userProfile ->
                                    var totalJumlahAngkaRating = 0.0F
                                    val _userProfileTarget = _userProfile.toObject(UserProfile::class.java)
                                    for(i in _userProfileTarget!!.testimoni){
                                        totalJumlahAngkaRating += i.rating
                                    }
                                    val ratingKeseluruhan = totalJumlahAngkaRating / _userProfileTarget.testimoni.size
                                    firestoreDb.collection("user_profile")
                                        .document(idUserTarget)
                                        .update(mapOf(
                                            "ratingKeseluruhan" to ratingKeseluruhan
                                        ))
                                        .addOnSuccessListener {
                                            addTestimoni.postValue("OK")
                                            getSpesificUserById(idUserTarget)
                                        }
                                        .addOnFailureListener {
                                            addTestimoni.postValue("FAILED2")
                                        }
                                }
                            //
                        }
                        .addOnFailureListener {
                            addTestimoni.postValue("FAILED")
                        }
                }else{
                    addTestimoni.postValue("BELUM PERNAH SATU TEAM")
                }
            }
    }

    suspend fun addUserLike(idUserSelected: String){
        firestoreDb.collection("user_profile")
            .document(idUserSelected)
            .get()
            .addOnSuccessListener { userProfile ->
                val userProfileSelected = userProfile.toObject(UserProfile::class.java)
                if(userProfileSelected!!.likedByUserId.contains(currentUser.value!!.uid)){
                    firestoreDb.collection("user_profile")
                        .document(idUserSelected)
                        .update(mapOf(
                            "likedByUserId" to FieldValue.arrayRemove(currentUser.value!!.uid),
                            "jumlahLike" to FieldValue.increment(-1)
                        ))
                        .addOnSuccessListener {
                            getAllUser()
                        }
                }else{
                    firestoreDb.collection("user_profile")
                        .document(idUserSelected)
                        .update(mapOf(
                            "likedByUserId" to FieldValue.arrayUnion(currentUser.value!!.uid),
                            "jumlahLike" to FieldValue.increment(1)
                        ))
                        .addOnSuccessListener {
                            getAllUser()
                        }
                }
            }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(): UserRepository{
            return instance ?: synchronized(this) {
                if (instance == null) {
                    instance = UserRepository()
                }
                return instance as UserRepository
            }
        }
    }
}