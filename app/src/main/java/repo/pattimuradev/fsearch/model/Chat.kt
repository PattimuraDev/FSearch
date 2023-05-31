package repo.pattimuradev.fsearch.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Representasi data class dari objek chat (khusus per message)
 * @author PattimuraDev (Dwi Satria Patra)
 */
@Parcelize
data class Chat(
    val sendingTime: Long = 0L,
    val idSender: String = "",
    val idReceiver: String = "",
    val message: String = ""
): Parcelable
