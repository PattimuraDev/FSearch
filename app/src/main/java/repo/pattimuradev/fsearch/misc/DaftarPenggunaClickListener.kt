package repo.pattimuradev.fsearch.misc

import repo.pattimuradev.fsearch.model.UserProfile

/**
 * Interface untuk handle aksi klik di halaman daftar pengguna, pada tombol like dan body
 * @author PattimuraDev (Dwi Satria Patra)
 */
interface DaftarPenggunaClickListener {
    fun clickOnLikeButton(item: UserProfile, position: Int)
    fun clickOnDaftarPenggunaBody(item: UserProfile, position: Int)
}