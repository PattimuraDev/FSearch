package repo.pattimuradev.fsearch.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Lomba(
    val posterLombaUrl: String? = null,
    val kategoriTingkatLomba: String = "",
    val kategoriPembuatLomba: String = "",
    val penyelenggaraLomba: String = "",
    val temaLomba: String = "",
    val judulLomba: String = "",
    val tanggalPelaksanaan: String = "",
    val lokasi: String = "",
    val tanggalPosting: Date? = null,
    val biayaPendaftaran: String = "",
    val deskripsiLomba: String = "",
    val linkLomba: String? = null,
    val likedByUserId: ArrayList<String>? = null
) : Parcelable
