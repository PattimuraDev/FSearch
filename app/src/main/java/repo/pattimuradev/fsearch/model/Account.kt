package repo.pattimuradev.fsearch.model

/**
 * Representasi data class dari objek akun firebase
 * @author PattimuraDev (Dwi Satria Patra)
 */
data class Account (
    val email: String = "",
    val password: String = "",
    val nama: String? = null
)

