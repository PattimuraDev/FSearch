package repo.pattimuradev.fsearch.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Notifikasi(
    var idNotifikasi: String? = null,
    val urlFotoPengirim: String? = null,
    val jenisNotifikasi: String = "",
    val idPengirim: String = "",
    val namaPengirim: String = "",
    val prodiPengirim: String? = null,
    val asalUniversitasPengirim: String? = null,
    val tahunAngkatanPengirim: Int? = null,
    val riwayatNotifikasi: Date? = null,
    val deskripsiLengkap: String? = null,
    val jenisLampiran: String? = null,
    val urlLampiran: String? = null,
    val idPenerima: String = "",
    val namaPenerima: String = "",
    val responded: Boolean? = null,
    val hasilRespon: String? = null,
): Parcelable