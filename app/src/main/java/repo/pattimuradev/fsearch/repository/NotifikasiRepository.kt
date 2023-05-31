package repo.pattimuradev.fsearch.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import repo.pattimuradev.fsearch.model.Notifikasi

/**
 * Repository berisi transaksi terkait notifikasi dengan layanan firebase
 * @author PattimuraDev (Dwi Satria Patra)
 */
class NotifikasiRepository {
    private val firestoreDb = FirebaseFirestore.getInstance()
    private val firebaseCloudStorage = Firebase.storage
    private val addNotifikasiStatus = MutableLiveData<String>()
    val addNotifikasiStatusLiveData : LiveData<String> = addNotifikasiStatus
    private val listNotifikasi = MutableLiveData<List<Notifikasi>>()
    val listNotifikasiLiveData: LiveData<List<Notifikasi>> = listNotifikasi
    private val jumlahNotifikasiNotResponded = MutableLiveData<Int>()
    val jumlahNotifikasiNotRespondedLiveData: LiveData<Int> = jumlahNotifikasiNotResponded

    /**
     * Fungsi untuk menambahkan notifikasi ke firebase
     * @author PattimuraDev (Dwi Satria Patra)
     * @param notifikasi objek notifikasi yang akan diinputkan
     * @param fileUri URI dari file, bisa image atau pdf
     * @param isUploading status yang menandai ingin mengunggah file atau tidak
     * @param fileType tipe file, bisa image atau pdf
     */
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

    /**
     * Fungsi untuk mendapatkan list notifikasi dari firebase
     * @author PattimuraDev (Dwi Satria Patra)
     * @param idPenerimaNotifikasi id dari user yang akan menerima notifikasi
     */
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

    /**
     * Fungsi untuk mendapatkan jumlah notifikasi belum direspon oleh user tertentu
     * @author PattimuraDev (Dwi Satria Patra)
     * @param idPenerimaNotifikasi id dari user yang menerima notifikasi
     */
    suspend fun getJumlahNotifikasiBelumDirespon(idPenerimaNotifikasi: String){
        firestoreDb.collection("notifikasi")
            .addSnapshotListener{ value, _ ->
                var jumlah = 0
                if(!value!!.isEmpty){
                    value.forEach { item ->
                        val notifikasi = item.toObject(Notifikasi::class.java)
                        if(notifikasi.idPenerima == idPenerimaNotifikasi && notifikasi.responded == false){
                            jumlah++
                        }
                    }
                }
                jumlahNotifikasiNotResponded.postValue(jumlah)
            }
    }

    /**
     * Fungsi untuk mengupdate status notifikasi sudah dibaca atau belum
     * @author PattimuraDev (Dwi Satria Patra)
     * @param hasilRespon hasil respon dari pengguna terhadap notifikasi tertentu
     * @param idNotifikasi id dari notifikasi yang dimaksud
     */
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