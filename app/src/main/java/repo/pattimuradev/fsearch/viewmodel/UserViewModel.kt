package repo.pattimuradev.fsearch.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import repo.pattimuradev.fsearch.model.*
import repo.pattimuradev.fsearch.repository.UserRepository

class UserViewModel : ViewModel(){
    private val userRepository = UserRepository.getInstance()
    val currentUser = userRepository.currentUserLiveData
    val currentUserProfile = userRepository.currentUserProfileLiveData
    val allUser = userRepository.allUserLiveData
    val currentUserFotoProfileUrl = userRepository.currentUserFotoProfilUrlLiveData
    val updateUserStatus = userRepository.updateProfilLiveData
    val emailRegistrationStatus = userRepository.checkIfEmailRegisteredLiveData
    val loginStatus = userRepository.loginStatusLiveData
    val spesificUserById = userRepository.getSpesificUserByIdLiveData
    val allUserFavorited = userRepository.getAllFavoritedUserLiveData
    val addFriendStatus = userRepository.addFriendResultLiveData
    val addTestimoniStatus = userRepository.addTestimoniLiveData
    val addTeamUpHistoryStatus = userRepository.addTeamUpHistoryLiveData

    suspend fun registerAccount(account: Account) = userRepository.registerAccount(account)
    suspend fun saveOtpEmail(emailVerification: EmailVerification) = userRepository.saveOtpEmail(emailVerification)
    suspend fun otpEmailVerification(emailVerification: EmailVerification): LiveData<String> {
        userRepository.verifyOtpEmail(emailVerification)
        return userRepository.otpEmailVerificationResultLiveData
    }
    suspend fun login(dataLogin: DataLogin) = userRepository.login(dataLogin)
    suspend fun updateProfile(userProfile: UserProfile) = userRepository.updateUser(userProfile)
    suspend fun getAllUser() = userRepository.getAllUser()
    suspend fun getFotoProfilUrl(fileUri: Uri?, isUploading: Boolean, namaFile: String?) = userRepository.getUserProfilePhotoUrl(fileUri, isUploading, namaFile)
    suspend fun getSpesificUserById(userId: String) = userRepository.getSpesificUserById(userId)
    suspend fun addUserLike(idUserSelected: String) = userRepository.addUserLike(idUserSelected)
    suspend fun getAllFavoritedUser() = userRepository.getAllUserFavorited()
    suspend fun addFriend(idUserPengirimPermintaan: String, idUserPenerimaPermintaan: String) = userRepository.addUserFriendList(idUserPengirimPermintaan, idUserPenerimaPermintaan)
    suspend fun addTeamUpHistory(idUserPengirimPermintaan: String, idUserPenerimaPermintaan: String) = userRepository.addUserTeamUpHistory(idUserPengirimPermintaan, idUserPenerimaPermintaan)
    suspend fun addTestimoni(idUserTarget: String, testimoni: Testimoni) = userRepository.addTestimoniToAnUser(idUserTarget, testimoni)
    suspend fun logout() = userRepository.logout()

}