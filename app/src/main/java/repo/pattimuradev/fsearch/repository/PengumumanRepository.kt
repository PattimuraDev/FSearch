package repo.pattimuradev.fsearch.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import repo.pattimuradev.fsearch.model.Pengumuman

class PengumumanRepository {
    private val firestoreDb = FirebaseFirestore.getInstance()
    private val firebaseCloudStorage = Firebase.storage
    private val listPengumuman = MutableLiveData<List<Pengumuman>>()
    val listPengumumanLiveData: LiveData<List<Pengumuman>> = listPengumuman
    private val addPengumumanStatus = MutableLiveData<String>()
    val addPengumumanStatusLiveData: LiveData<String> = addPengumumanStatus
    private val getPosterPengumumanImageUrl = MutableLiveData<String>()
    val getPosterPengumumanImageUrlLiveData: LiveData<String> = getPosterPengumumanImageUrl

    suspend fun getAllPengumuman(){
        firestoreDb.collection("pengumuman")
            .addSnapshotListener { value, _ ->
                val listPengumumanResult: MutableList<Pengumuman> = mutableListOf()
                if(!value!!.isEmpty){
                    value.forEach { item ->
                        val pengumuman = item.toObject(Pengumuman::class.java)
                        listPengumumanResult += pengumuman
                    }
                }
                listPengumuman.postValue(listPengumumanResult)
            }
    }

    suspend fun postImageToStorage(fileUri: Uri?, isUploadingImage: Boolean){
        if(isUploadingImage){
            val storageRef = firebaseCloudStorage.reference
            val riversRef = storageRef.child("pengumuman/${fileUri!!.lastPathSegment}")
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
                    getPosterPengumumanImageUrl.postValue(getDownloadTaskStatus.result.toString())
                }else{
                    getPosterPengumumanImageUrl.postValue(null)
                }
            }
        }else{
            getPosterPengumumanImageUrl.postValue("")
        }
    }

    suspend fun addPengumuman(pengumuman: Pengumuman){
        firestoreDb.collection("pengumuman")
            .add(pengumuman)
            .addOnCompleteListener { addPengumumanTask ->
                if(addPengumumanTask.isSuccessful){
                    addPengumumanStatus.postValue("OK")
                }else{
                    addPengumumanStatus.postValue("FAILED")
                }
            }
    }

    companion object {
        @Volatile
        private var instance: PengumumanRepository? = null
        fun getInstance(): PengumumanRepository{
            return instance ?: synchronized(this) {
                if (instance == null) {
                    instance = PengumumanRepository()
                }
                return instance as PengumumanRepository
            }
        }
    }
}