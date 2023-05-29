package repo.pattimuradev.fsearch.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Representasi data class dari objek testimoni
 * @author PattimuraDev (Dwi Satria Patra)
 */
@Parcelize
data class Testimoni(
    val riwayatPosting: Long? = null,
    val urlFotoPengirim: String? = null,
    val namaPengirim: String? = null,
    val programStudiPengirim: String? = null,
    val asalUniversitasPengirim: String? = null,
    val tahunAngkatanPengirim: Int? = null,
    val rating: Float = 0.0F,
    val deskripsi: String = "",
): Parcelable