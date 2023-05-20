package repo.pattimuradev.fsearch.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_adapter_daftar_pengguna.view.*
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.DaftarPenggunaClickListener
import repo.pattimuradev.fsearch.model.UserProfile

class DaftarPenggunaAdapter(private val daftarPenggunaClickListener: DaftarPenggunaClickListener) :
    RecyclerView.Adapter<DaftarPenggunaAdapter.ViewHolder>()
{
    private var listPengguna: List<UserProfile>? = null

    fun setListPengguna(listPengguna: List<UserProfile>){
        this.listPengguna = listPengguna
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_adapter_daftar_pengguna, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView){
            rv_pengguna_nama_pengguna.text = listPengguna!![position].nama


            rv_pengguna_asal_prodi.text = if(listPengguna!![position].dataDiri == null){
                ""
            }else{
                if(listPengguna!![position].dataDiri!!.programStudi == null){
                    ""
                }else{
                    listPengguna!![position].dataDiri!!.programStudi
                }
            }


            rv_pengguna_asal_universitas.text = if(listPengguna!![position].dataDiri == null){
                ""
            }else{
                if(listPengguna!![position].dataDiri!!.asalUniversitas == null){
                    ""
                }else{
                    listPengguna!![position].dataDiri!!.asalUniversitas
                }
            }


            rv_pengguna_jumlah_testimoni.text = if(listPengguna!![position].testimoni == null){
                ""
            }else{
                "(${listPengguna!![position].testimoni!!.size})"
            }


            rv_pengguna_tahun_angkatan.text = if(listPengguna!![position].dataDiri == null){
                ""
            }else{
                if(listPengguna!![position].dataDiri!!.tahunAngkatan == null){
                    ""
                }else{
                    listPengguna!![position].dataDiri!!.tahunAngkatan.toString()
                }
            }


            rv_pengguna_rating.rating = if(listPengguna!![position].ratingKeseluruhan == null){
                0.0.toFloat()
            }else{
                listPengguna!![position].ratingKeseluruhan!!
            }


            rv_pengguna_button_like.setOnClickListener {
                daftarPenggunaClickListener.clickOnLikeButton(listPengguna!![position], position)
            }
            rv_pengguna_container.setOnClickListener {
                daftarPenggunaClickListener.clickOnDaftarPenggunaBody(listPengguna!![position], position)
            }

            Glide.with(rv_pengguna_foto_profile.context)
                .load(listPengguna!![position].urlFoto)
                .error(R.drawable.standard_user_photo)
                .into(rv_pengguna_foto_profile)
        }
    }

    override fun getItemCount(): Int {
        return if(listPengguna.isNullOrEmpty()){
            0
        }else{
            listPengguna!!.size
        }
    }
}