package repo.pattimuradev.fsearch.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Notifikasi(
    var idNotifikasi: String? = null,
    val jenisNotifikasi: String = "",
    val idPengirim: String = "",
    val namaPengirim: String = "",
    val prodiPengirim: String? = null,
    val asalUniversitasPengirim: String? = null,
    val tahunAngkatanPengirim: Int? = null,
    val riwayatNotifikasi: Date? = null,
    val deskprisi: String? = null,
    val idPenerima: String = "",
    val namaPenerima: String = "",
    val isResponded: Boolean? = null,
    val respon: String? = null
): Parcelable