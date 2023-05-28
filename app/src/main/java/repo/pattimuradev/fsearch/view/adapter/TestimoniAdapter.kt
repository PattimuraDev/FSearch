package repo.pattimuradev.fsearch.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_adapter_testimoni.view.*
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.DateAndTimeHandler
import repo.pattimuradev.fsearch.model.Testimoni

class TestimoniAdapter: RecyclerView.Adapter<TestimoniAdapter.ViewHolder>() {

    private var listTestimoni: ArrayList<Testimoni>? = null
    fun setListTestimoni(listTestimoni: ArrayList<Testimoni>){
        this.listTestimoni = listTestimoni
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_adapter_testimoni, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView){
            viewpager_profile_rv_testimoni_nama_pengirim.text = listTestimoni!![position].namaPengirim
            viewpager_profile_rv_testimoni_asal_prodi_pengirim.text = listTestimoni!![position].programStudiPengirim
            viewpager_profile_rv_testimoni_asal_universitas_pengirim.text = listTestimoni!![position].asalUniversitasPengirim
            viewpager_profile_rv_testimoni_tahun_angkatan_pengirim.text = listTestimoni!![position].tahunAngkatanPengirim.toString()
            viewpager_profile_rv_testimoni_riwayat_posting.text = DateAndTimeHandler.getTimeAgo(listTestimoni!![position].riwayatPosting!!)
            viewpager_profile_rv_testimoni_rating_testimoni.rating = listTestimoni!![position].rating!!
            viewpager_profile_rv_testimoni_deskripsi_testimoni.text = listTestimoni!![position].deskripsi
            Glide.with(viewpager_profile_rv_testimoni_foto_pengirim.context)
                .load(listTestimoni!![position].urlFotoPengirim)
                .error(R.drawable.standard_user_photo)
                .into(viewpager_profile_rv_testimoni_foto_pengirim)
        }
    }

    override fun getItemCount(): Int {
        return if(listTestimoni.isNullOrEmpty()){
            0
        }else{
            listTestimoni!!.size
        }
    }


}