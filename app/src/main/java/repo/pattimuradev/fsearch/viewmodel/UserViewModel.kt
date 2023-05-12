package repo.pattimuradev.fsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import repo.pattimuradev.fsearch.model.Account
import repo.pattimuradev.fsearch.model.DataLogin
import repo.pattimuradev.fsearch.model.EmailVerification
import repo.pattimuradev.fsearch.repository.UserRepository

class UserViewModel : ViewModel(){
    private val userRepository = UserRepository.getInstance()

    suspend fun registerAccount(account: Account) = userRepository.registerAccount(account)
    suspend fun saveOtpEmail(emailVerification: EmailVerification) = userRepository.saveOtpEmail(emailVerification)
    suspend fun otpEmailVerification(emailVerification: EmailVerification): LiveData<String> {
        userRepository.verifyOtpEmail(emailVerification)
        return userRepository.otpEmailVerificationResultLiveData
    }
    suspend fun login(dataLogin: DataLogin) = userRepository.login(dataLogin)
}