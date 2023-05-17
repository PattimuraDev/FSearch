package repo.pattimuradev.fsearch.model

import java.util.*

data class Pengumuman(
    val fotoProfilUrl: String? = null,
    val namaPengirim: String = "",
    val asalProgramStudi: String? = null,
    val asalUniversitas: String? = null,
    val riwayatPosting: Date? = null,
    val tahunAngkatan: Int? = null,
    val deskripsiPengumuman: String = "",
    val posterUrl: String? = null
)