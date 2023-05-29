package repo.pattimuradev.fsearch.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Representasi data class dari objek data diri user (bagian dari user profile)
 * @author PattimuraDev (Dwi Satria Patra)
 */
@Parcelize
data class DataDiriUser(
    val asalUniversitas: String? = null,
    val tahunAngkatan: Int? = null,
    val jurusan: String? = null,
    val programStudi: String? = null,
    val keminatan: String? = null,
    val jenisKelamin: String? = null,
    val umur: Int? = null,
    val asalKota: String? = null,
    val fakultas: String? = null,
    val kepribadian: String? = null
) : Parcelable