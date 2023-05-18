package repo.pattimuradev.fsearch.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import repo.pattimuradev.fsearch.model.Pengumuman
import repo.pattimuradev.fsearch.repository.PengumumanRepository

class PengumumanViewModel : ViewModel() {
    private val pengumumanRepository =  PengumumanRepository.getInstance()
    val listPengumuman = pengumumanRepository.listPengumumanLiveData
    val addPengumumanStatus = pengumumanRepository.addPengumumanStatusLiveData
    val posterPengumumanUrl = pengumumanRepository.getPosterPengumumanImageUrlLiveData

    suspend fun addPengumuman(pengumuman: Pengumuman) = pengumumanRepository.addPengumuman(pengumuman)
    suspend fun getAllPengumuman() = pengumumanRepository.getAllPengumuman()
    suspend fun postImageToStorage(fileUri: Uri?, isUploadingImage: Boolean) = pengumumanRepository.postImageToStorage(fileUri, isUploadingImage)
}