package repo.pattimuradev.fsearch.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import repo.pattimuradev.fsearch.model.Pengumuman

/**
 * Repository berisi transaksi terkait pengumuman/post dengan layanan firebase
 * @author PattimuraDev (Dwi Satria Patra)
 */
class PengumumanRepository {
    private val firestoreDb = FirebaseFirestore.getInstance()
    private val firebaseCloudStorage = Firebase.storage
    private val listPengumuman = MutableLiveData<List<Pengumuman>>()
    val listPengumumanLiveData: LiveData<List<Pengumuman>> = listPengumuman
    private val addPengumumanStatus = MutableLiveData<String>()
    val addPengumumanStatusLiveData: LiveData<String> = addPengumumanStatus
    private val getPosterPengumumanImageUrl = MutableLiveData<String>()
    val getPosterPengumumanImageUrlLiveData: LiveData<String> = getPosterPengumumanImageUrl

    /**
     * Fungsi untuk mendapatkan list pengumuman dari firebase
     * @author PattimuraDev (Dwi Satria Patra)
     */
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

    /**
     * Fungsi untuk mengunggah foto/poster ke cloud storage
     * @author PattimuraDev (Dwi Satria Patra)
     * @param fileUri URI dari file image/poster
     * @param isUploadingImage Status yang menandakan aapakah ingin mengunggah image
     * atau tidak
     */
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
                    getPosterPengumumanImageUrl.postValue("")
                }
            }
        }else{
            getPosterPengumumanImageUrl.postValue("")
        }
    }

    /**
     * Fungsi untuk mendapatkan menambah pengumuman ke firebase
     * @author PattimuraDev (Dwi Satria Patra)
     * @param pengumuman objek pengumuman yang ingin ditambahkan
     */
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