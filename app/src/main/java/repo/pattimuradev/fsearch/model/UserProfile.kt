package repo.pattimuradev.fsearch.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Representasi data class dari objek user profile
 * @author PattimuraDev (Dwi Satria Patra)
 */
@Parcelize
data class UserProfile(
    val id: String? = null,
    val nama: String = "",
    val email: String = "",
    val urlFoto: String? = null,
    val jumlahTeman: Int? = null,
    val jumlahLike: Int? = null,
    val bio: String? = null,
    val dataDiri: DataDiriUser? = null,
    val testimoni: ArrayList<Testimoni> = arrayListOf(),
    val ratingKeseluruhan: Float = 0.0F,
    var statusBersediaMenerimaAjakan: Boolean = true,
    val likedByUserId: ArrayList<String> = arrayListOf(),
    val friendListUserId: ArrayList<String> = arrayListOf(),
    val teamHistory: ArrayList<String> = arrayListOf()
) : Parcelable
