package repo.pattimuradev.fsearch.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Representasi data class dari objek chat room atau ruang obrolan
 * @author PattimuraDev (Dwi Satria Patra)
 */
@Parcelize
data class ChatRoom(
    var chatRoomId: String? = null,
    val personInChat: ArrayList<String> = arrayListOf(),
    val listChat: ArrayList<Chat> = arrayListOf(),
    val lastChatMessage: String = "",
    val lastChatTime: Long = 0L,
    val messageNotReadByPersonOne: Int = 0,
    val messageNotReadByPersonTwo: Int = 0,
    val fotoUrlPersonOne: String? = null,
    val fotoUrlPersonTwo: String? = null,
    val namaPersonOne: String = "",
    val namaPersonTwo: String = ""
): Parcelable
