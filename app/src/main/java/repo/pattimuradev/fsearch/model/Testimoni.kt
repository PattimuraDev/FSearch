package repo.pattimuradev.fsearch.model

import java.util.*

data class Testimoni(
    val riwayatPosting: Date? = null,
    val urlFoto: String? = null,
    val nama: String? = null,
    val programStudi: String? = null,
    val asalUniversitas: String? = null,
    val tahunAngkatan: Int? = null,
    val rating: Float? = null,
    val deskripsi: String? = null,
)