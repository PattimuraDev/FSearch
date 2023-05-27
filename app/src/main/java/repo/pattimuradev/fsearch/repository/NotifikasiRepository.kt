package repo.pattimuradev.fsearch.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import repo.pattimuradev.fsearch.model.Notifikasi

class NotifikasiRepository {
    private val firestoreDb = FirebaseFirestore.getInstance()
    private val firebaseCloudStorage = Firebase.storage
    private val addNotifikasiStatus = MutableLiveData<String>()
    val addNotifikasiStatusLiveData : LiveData<String> = addNotifikasiStatus
    private val listNotifikasi = MutableLiveData<List<Notifikasi>>()
    val listNotifikasiLiveData: LiveData<List<Notifikasi>> = listNotifikasi

    suspend fun addNotifikasi(notifikasi: Notifikasi, fileUri: Uri?, isUploading: Boolean, fileType: String?){
        val document = firestoreDb.collection("notifikasi").document()
        val documentId = document.id
        val storageRef = firebaseCloudStorage.reference
        notifikasi.idNotifikasi = documentId

        if(isUploading){
            val storageFilePath = when (fileType) {
                "pdf" -> {
                    "notifikasi/pdf/${fileUri!!.lastPathSegment}"
                }
                "image" -> {
                    "notifikasi/image/${fileUri!!.lastPathSegment}"
                }
                else -> {
                    ""
                }
            }
            val riversRef = storageRef.child(storageFilePath)
            val uploadTask = riversRef.putFile(fileUri!!)
            uploadTask.continueWithTask { getDownloadUrlTask ->
                if(!getDownloadUrlTask.isSuccessful){
                    getDownloadUrlTask.exception?.let {
                        throw it
                    }
                }
                riversRef.downloadUrl
            }.addOnCompleteListener {  getDownloadTaskStatus ->
                if(getDownloadTaskStatus.isSuccessful){
                    notifikasi.urlLampiran = getDownloadTaskStatus.result.toString()
                    firestoreDb.collection("notifikasi")
                        .document(documentId)
                        .set(notifikasi)
                        .addOnCompleteListener { addNotifikasiTask ->
                            if (addNotifikasiTask.isSuccessful) {
                                addNotifikasiStatus.postValue("OK")
                            } else {
                                addNotifikasiStatus.postValue("FAILED")
                            }
                        }
                }else{
                    notifikasi.urlLampiran = ""
                    firestoreDb.collection("notifikasi")
                        .document(documentId)
                        .set(notifikasi)
                        .addOnCompleteListener { addNotifikasiTask ->
                            if (addNotifikasiTask.isSuccessful) {
                                addNotifikasiStatus.postValue("OK")
                            } else {
                                addNotifikasiStatus.postValue("FAILED")
                            }
                        }
                }
            }
        }else{
            notifikasi.urlLampiran = ""
            firestoreDb.collection("notifikasi")
                .document(documentId)
                .set(notifikasi)
                .addOnCompleteListener { addNotifikasiTask ->
                    if (addNotifikasiTask.isSuccessful) {
                        addNotifikasiStatus.postValue("OK")
                    } else {
                        addNotifikasiStatus.postValue("FAILED")
                    }
                }
        }
    }

    suspend fun getAllNotifikasi(idPenerimaNotifikasi: String){
        firestoreDb.collection("notifikasi")
            .addSnapshotListener{ value, _ ->
                val listNotifikasiResult: MutableList<Notifikasi> = mutableListOf()
                if(!value!!.isEmpty){
                    value.forEach { item ->
                        val notifikasi = item.toObject(Notifikasi::class.java)
                        if(notifikasi.idPenerima == idPenerimaNotifikasi){
                            listNotifikasiResult += notifikasi
                        }
                    }
                }
                listNotifikasi.postValue(listNotifikasiResult)
            }
    }

    suspend fun updateNotifikasiIsResponded(hasilRespon: String, idNotifikasi: String){
        val notifikasiPref = firestoreDb.collection("notifikasi").document(idNotifikasi)
        notifikasiPref.update(mapOf(
            "responded" to true,
            "hasilRespon" to hasilRespon
        ))
    }

    companion object {
        @Volatile
        private var instance: NotifikasiRepository? = null
        fun getInstance(): NotifikasiRepository{
            return instance ?: synchronized(this) {
                if (instance == null) {
                    instance = NotifikasiRepository()
                }
                return instance as NotifikasiRepository
            }
        }
    }
}