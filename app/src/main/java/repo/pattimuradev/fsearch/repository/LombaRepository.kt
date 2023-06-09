package repo.pattimuradev.fsearch.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import repo.pattimuradev.fsearch.model.Lomba

/**
 * Repository berisi transaksi terkait lomba dengan layanan firebase
 * @author PattimuraDev (Dwi Satria Patra)
 */
class LombaRepository {
    private val firestoreDb = FirebaseFirestore.getInstance()
    private val firebaseCloudStorage = Firebase.storage
    private val listLomba = MutableLiveData<List<Lomba>>()
    val listLombaLiveData: LiveData<List<Lomba>> = listLomba
    private val addLombaStatus = MutableLiveData<String>()
    val addLombaStatusLiveData: LiveData<String> = addLombaStatus
    private val getPosterLombaImageUrl = MutableLiveData<String>()
    val getPosterLombaImageUrlLiveData: LiveData<String> = getPosterLombaImageUrl

    /**
     * Fungsi untuk mendapatkan list lomba dari firebase
     * @author PattimuraDev (Dwi Satria Patra)
     */
    suspend fun getAllListLomba(){
        firestoreDb.collection("lomba")
            .addSnapshotListener{ value, _ ->
                val listLombaResult: MutableList<Lomba> = mutableListOf()
                if(!value!!.isEmpty){
                    value.forEach { item ->
                        val lomba = item.toObject(Lomba::class.java)
                        listLombaResult += lomba
                    }
                }
                listLomba.postValue(listLombaResult)
            }
    }

    /**
     * Fungsi untuk menambahkan like dari user dengan id tertentu ke dalam suatu lomba
     * @author PattimuraDev (Dwi Satria Patra)
     * @param idUser id dari user yang melakukan like
     * @param idLomba id dari lomba
     */
    suspend fun addUserLike(idUser: String, idLomba: String){
        firestoreDb.collection("lomba")
            .document(idLomba)
            .get()
            .addOnSuccessListener {
                val lomba = it.toObject(Lomba::class.java)
                if(lomba!!.likedByUserId.contains(idUser)){
                    firestoreDb.collection("lomba")
                        .document(idLomba)
                        .update("likedByUserId", FieldValue.arrayRemove(idUser))
                }else{
                    firestoreDb.collection("lomba")
                        .document(idLomba)
                        .update("likedByUserId", FieldValue.arrayUnion(idUser))
                }
            }
    }

    /**
     * Fungsi untuk menambahkan lomba ke firebase
     * @author PattimuraDev (Dwi Satria Patra)
     * @param lomba objek lomba yang akan diinputkan
     */
    suspend fun addLomba(lomba: Lomba) {
        val document = firestoreDb.collection("lomba").document()
        val documentId = document.id
        lomba.idLomba = documentId
        firestoreDb.collection("lomba")
            .document(documentId)
            .set(lomba)
            .addOnCompleteListener { addLombaTask ->
                if (addLombaTask.isSuccessful) {
                    addLombaStatus.postValue("OK")
                } else {
                    addLombaStatus.postValue("FAILED")
                }
            }
    }

    /**
     * Fungsi untuk menambahkan poster lomba ke cloud storage
     * @author PattimuraDev (Dwi Satria Patra)
     * @param fileUri URI dari poster lomba
     * @param isUploadingImage status untuk menandai ingin mengunggah poster atau tidak
     */
    suspend fun postImageToStorage(fileUri: Uri?, isUploadingImage: Boolean){
        if(isUploadingImage){
            val storageRef = firebaseCloudStorage.reference
            val riversRef = storageRef.child("lomba/${fileUri!!.lastPathSegment}")
            val uploadTask = riversRef.putFile(fileUri)
            uploadTask.continueWithTask { getDownloadUrlTask ->
                if(!getDownloadUrlTask.isSuccessful){
                    getDownloadUrlTask.exception?.let {
                        throw it
                    }
                }
                riversRef.downloadUrl
            }.addOnCompleteListener {  getDownloadTaskStatus ->
                if(getDownloadTaskStatus.isSuccessful){
                    getPosterLombaImageUrl.postValue(getDownloadTaskStatus.result.toString())
                }else{
                    getPosterLombaImageUrl.postValue("")
                }
            }
        }else{
            getPosterLombaImageUrl.postValue("")
        }
    }

    companion object {
        @Volatile
        private var instance: LombaRepository? = null
        fun getInstance(): LombaRepository{
            return instance ?: synchronized(this) {
                if (instance == null) {
                    instance = LombaRepository()
                }
                return instance as LombaRepository
            }
        }
    }
}