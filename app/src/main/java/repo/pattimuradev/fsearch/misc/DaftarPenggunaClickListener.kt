package repo.pattimuradev.fsearch.misc

import repo.pattimuradev.fsearch.model.UserProfile

interface DaftarPenggunaClickListener {
    fun clickOnLikeButton(item: UserProfile, position: Int)
    fun clickOnDaftarPenggunaBody(item: UserProfile, position: Int)
}