package repo.pattimuradev.fsearch.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import repo.pattimuradev.fsearch.model.Notifikasi
import repo.pattimuradev.fsearch.repository.NotifikasiRepository

class NotifikasiViewModel: ViewModel(){
    private val notifikasiRepository = NotifikasiRepository.getInstance()
    val listNotifikasi = notifikasiRepository.listNotifikasiLiveData
    val addNotifikasiStatus = notifikasiRepository.addNotifikasiStatusLiveData
    val jumlahNotifikasiBelumDirespon = notifikasiRepository.jumlahNotifikasiNotRespondedLiveData

    suspend fun addNotifikasi(notifikasi: Notifikasi, fileUri: Uri?, isUploadingFile: Boolean, fileType: String?) = notifikasiRepository.addNotifikasi(notifikasi, fileUri, isUploadingFile, fileType)
    suspend fun getAllNotifikasi(idPenerimaNotifikasi: String) = notifikasiRepository.getAllNotifikasi(idPenerimaNotifikasi)
    suspend fun getJumlahNotifikasiBelumDirespon(idPenerimaNotifikasi: String) = notifikasiRepository.getJumlahNotifikasiBelumDirespon(idPenerimaNotifikasi)
    suspend fun updateNotifikasiIsResponded(hasilRespon: String, idNotifikasi: String) = notifikasiRepository.updateNotifikasiIsResponded(hasilRespon, idNotifikasi)
}