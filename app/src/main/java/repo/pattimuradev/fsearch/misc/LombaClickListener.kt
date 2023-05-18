package repo.pattimuradev.fsearch.misc

import repo.pattimuradev.fsearch.model.Lomba

interface LombaClickListener {
    fun clickOnLikeButton(item: Lomba, position: Int)
    fun clickOnDaftarLombaBody(item: Lomba, position: Int)
}