package repo.pattimuradev.fsearch.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import repo.pattimuradev.fsearch.model.EmailVerification

class UserRepository {
    private val firestoreDb = FirebaseFirestore.getInstance()
    private val otpEmailVerificationResult = MutableLiveData<String>()
    val otpEmailVerificationResultLiveData: LiveData<String> = otpEmailVerificationResult

    suspend fun saveOtpEmail(emailVerification: EmailVerification): MutableLiveData<String>{
        val result = MutableLiveData<String>()
        firestoreDb.collection("email_verification")
            .add(emailVerification)
            .addOnSuccessListener {
                result.postValue("OK")
            }
            .addOnFailureListener {
                result.postValue("FAILED")
            }
        return result
    }

    suspend fun verifyOtpEmail(emailVerification: EmailVerification){
        firestoreDb.collection("email_verification")
            .addSnapshotListener { value, error ->
                otpEmailVerificationResult.postValue("FAILED")
                if(!value?.isEmpty!!){
                    value.forEach { item ->
                        val emailVerificationItem = item.toObject(EmailVerification::class.java)
                        if(emailVerificationItem.verificationCode.equals(emailVerification.verificationCode)){
                            otpEmailVerificationResult.postValue("OK")
                        }
                    }
                }else{
                    otpEmailVerificationResult.postValue("FAILED")
                }
            }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(): UserRepository{
            return instance ?: synchronized(this) {
                if (instance == null) {
                    instance = UserRepository()
                }
                return instance as UserRepository
            }
        }
    }
}