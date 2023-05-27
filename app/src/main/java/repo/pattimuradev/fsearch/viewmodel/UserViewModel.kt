package repo.pattimuradev.fsearch.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import repo.pattimuradev.fsearch.model.Account
import repo.pattimuradev.fsearch.model.DataLogin
import repo.pattimuradev.fsearch.model.EmailVerification
import repo.pattimuradev.fsearch.model.UserProfile
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
    val AllUserFavorited = userRepository.getAllFavoritedUserLiveData
    val addFriendStatus = userRepository.addFriendResultLiveData

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
    suspend fun addFriend(idUserPengirim: String, idUserPenerima: String) = userRepository.addUserFriendList(idUserPengirim, idUserPenerima)
}