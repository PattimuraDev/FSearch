package repo.pattimuradev.fsearch.model

data class EmailVerification(
    val email: String = "",
    val verificationCode: String = ""
)