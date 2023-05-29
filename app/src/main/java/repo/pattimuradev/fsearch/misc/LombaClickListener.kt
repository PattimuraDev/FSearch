package repo.pattimuradev.fsearch.misc

import repo.pattimuradev.fsearch.model.Lomba

/**
 * Interface untuk menghandle aksi klik di daftar lomba
 * @author PattimuraDev (Dwi Satria Patra)
 */
interface LombaClickListener {
    fun clickOnLikeButton(item: Lomba, position: Int)
    fun clickOnDaftarLombaBody(item: Lomba, position: Int)
}