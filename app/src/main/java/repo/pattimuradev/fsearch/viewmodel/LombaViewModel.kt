package repo.pattimuradev.fsearch.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import repo.pattimuradev.fsearch.model.Lomba
import repo.pattimuradev.fsearch.repository.LombaRepository

class LombaViewModel: ViewModel() {
    private val lombaRepository = LombaRepository.getInstance()
    val listLomba = lombaRepository.listLombaLiveData
    val addLombaStatus = lombaRepository.addLombaStatusLiveData
    val getPosterLombaImageUrl = lombaRepository.getPosterLombaImageUrlLiveData

    suspend fun getAllLomba() = lombaRepository.getAllListLomba()
    suspend fun addLomba(lomba: Lomba) = lombaRepository.addLomba(lomba)
    suspend fun getPosterUrl(fileUri: Uri?, isUploading: Boolean) = lombaRepository.postImageToStorage(fileUri, isUploading)
    suspend fun addUserLike(idUser: String, idLomba: String) = lombaRepository.addUserLike(idUser, idLomba)
}