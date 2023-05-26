package repo.pattimuradev.fsearch.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import repo.pattimuradev.fsearch.model.Notifikasi
import repo.pattimuradev.fsearch.repository.NotifikasiRepository

class NotifikasiViewModel: ViewModel(){
    private val notifikasiRepository = NotifikasiRepository.getInstance()
    val listNotifikasi = notifikasiRepository.listNotifikasiLiveData
    val getFileDownloadUrl = notifikasiRepository.getFileDownloadUrlLiveData
    val addNotifikasiStatus = notifikasiRepository.addNotifikasiStatusLiveData

    suspend fun addNotifikasi(notifikasi: Notifikasi) = notifikasiRepository.addNotifikasi(notifikasi)
    suspend fun getAllNotifikasi(idPenerimaNotifikasi: String) = notifikasiRepository.getAllNotifikasi(idPenerimaNotifikasi)
    suspend fun postFileToStorage(fileUri: Uri?, isUploading: Boolean, fileType: String?) = notifikasiRepository.postFileToStorage(fileUri, isUploading, fileType)
}