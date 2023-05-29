package repo.pattimuradev.fsearch.model

/**
 * Representasi data class dari objek verifikasi email yang digunakan untuk
 * memverifikasi email agar terdaftar di sistem
 * @author PattimuraDev (Dwi Satria Patra)
 */
data class EmailVerification(
    val email: String = "",
    val verificationCode: String = ""
)