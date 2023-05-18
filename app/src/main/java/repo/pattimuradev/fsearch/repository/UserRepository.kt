package repo.pattimuradev.fsearch.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import repo.pattimuradev.fsearch.model.Account
import repo.pattimuradev.fsearch.model.DataLogin
import repo.pattimuradev.fsearch.model.EmailVerification
import repo.pattimuradev.fsearch.model.UserProfile

class UserRepository {
    private val firebaseAuth = Firebase.auth
    private val firestoreDb = FirebaseFirestore.getInstance()
    private val otpEmailVerificationResult = MutableLiveData<String>()
    val otpEmailVerificationResultLiveData: LiveData<String> = otpEmailVerificationResult
    private val currentUser = MutableLiveData<FirebaseUser>()
    val currentUserLiveData = currentUser
    private val currentUserProfile = MutableLiveData<UserProfile>()
    val currentUserProfileLiveData = currentUserProfile

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
                                val dataDiriUser = UserProfile(
                                    user.uid,
                                    user.displayName!!,
                                    user.email!!,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null
                                )
                                firestoreDb.collection("user_profile")
                                    .document(dataDiriUser.id!!)
                                    .set(dataDiriUser)
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

    suspend fun login(dataLogin: DataLogin): MutableLiveData<String>{
        val result = MutableLiveData<String>()

        firebaseAuth.signInWithEmailAndPassword(dataLogin.email!!, dataLogin.password!!)
            .addOnCompleteListener{ loginTask ->
                if(loginTask.isSuccessful){
                    currentUser.value = firebaseAuth.currentUser
                    result.postValue("OK")

                    val dataDiriUser = UserProfile(
                        currentUser.value!!.uid,
                        currentUser.value!!.displayName!!,
                        currentUser.value!!.email!!,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                    currentUserProfile.postValue(dataDiriUser)
                }else{
                    result.postValue("FAILED")
                }
            }
        return result
    }

    suspend fun saveOtpEmail(emailVerification: EmailVerification): MutableLiveData<String>{
        val result = MutableLiveData<String>()

        firestoreDb.collection("email_verification")
            .addSnapshotListener{ value, _ ->
                var emailRegistered = true
                if(!value?.isEmpty!!){
                    for(item in value){
                        val emailVerificationItem = item.toObject(EmailVerification::class.java)
                        if(emailVerificationItem.email == emailVerification.email){
                            emailRegistered = true
                        }else if(item == value.last() && emailVerificationItem.email != emailVerification.email){
                            emailRegistered = false
                        }
                    }
                }else{
                    emailRegistered = false
                }

                if(!emailRegistered){
                    firestoreDb.collection("email_verification")
                        .add(emailVerification)
                        .addOnSuccessListener {
                            result.postValue("OK")
                        }
                        .addOnFailureListener {
                            result.postValue("FAILED")
                        }
                }else{
                    result.postValue("ALREADY REGISTERED")
                }
            }

        return result
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

    suspend fun getProfile(userId: String) {
        firestoreDb.collection("user_profile")
            .document(currentUser.value!!.uid)
            .get()
            .addOnSuccessListener { snapshot ->
                val profile = snapshot.toObject(UserProfile::class.java)
                currentUserProfile.value = profile
            }
            .addOnFailureListener {
                currentUserProfile.value = null
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