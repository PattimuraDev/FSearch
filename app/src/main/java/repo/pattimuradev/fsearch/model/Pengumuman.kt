package repo.pattimuradev.fsearch.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Pengumuman(
    val fotoProfilUrl: String? = null,
    val namaPengirim: String = "",
    val asalProgramStudi: String? = null,
    val asalUniversitas: String? = null,
    val riwayatPosting: Date? = null,
    val tahunAngkatan: Int? = null,
    val deskripsiPengumuman: String = "",
    val posterUrl: String? = null,
    val idPengirim: String? = null
): Parcelable