package repo.pattimuradev.fsearch.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_adapter_pengumuman.view.*
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.DateAndTimeHandler
import repo.pattimuradev.fsearch.model.Pengumuman

class PengumumanAdapter(private val onClick: (Pengumuman) -> Unit): RecyclerView.Adapter<PengumumanAdapter.ViewHolder>() {
    private var listPengumuman: List<Pengumuman>? = null
    fun setListPengumuman(list: List<Pengumuman>){
        this.listPengumuman = list
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_adapter_pengumuman, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView){
            rv_pengumuman_nama.text = listPengumuman!![position].namaPengirim
            rv_pengumuman_program_studi.text = listPengumuman!![position].asalProgramStudi
            rv_pengumuman_nama_universitas.text = listPengumuman!![position].asalUniversitas
            rv_pengumuman_waktu_posting.text = DateAndTimeHandler.getTimeAgo(listPengumuman!![position].riwayatPosting!!.time)
            rv_pengumuman_tahun_angkatan.text = if(listPengumuman!![position].tahunAngkatan == null){
                ""
            }else{
                listPengumuman!![position].tahunAngkatan.toString()
            }
            rv_pengumuman_deskripsi.text = listPengumuman!![position].deskripsiPengumuman
            Glide.with(rv_pengumuman_foto_profile.context)
                .load(listPengumuman!![position].fotoProfilUrl)
                .error(R.drawable.standard_user_photo)
                .into(rv_pengumuman_foto_profile)
            Glide.with(rv_pengumuman_poster.context)
                .load(listPengumuman!![position].posterUrl)
                .error(R.drawable.no_image_available)
                .into(rv_pengumuman_poster)

            rv_pengumuman_button_ajukan_diri.setOnClickListener{
                onClick(listPengumuman!![position])
            }
        }
    }

    override fun getItemCount(): Int {
        return if(listPengumuman.isNullOrEmpty()){
            0
        }else{
            listPengumuman!!.size
        }
    }

//    override fun getFilter(): Filter {
//        val filter = object: Filter(){
//            override fun performFiltering(p0: CharSequence?): FilterResults {
//                val filterResult = FilterResults()
//                if(p0.isNullOrEmpty()){
//                    filterResult.values = listPengumumanFiltered
//                    filterResult.count = listPengumumanFiltered!!.size
//                }else{
//                    val searchChar = p0.toString().lowercase(Locale.getDefault())
//                    val filteredResult = mutableListOf<Pengumuman>()
//                    for(pengumuman in listPengumumanFiltered!!){
//                        if(pengumuman.deskripsiPengumuman.lowercase(Locale.getDefault()).contains(searchChar)){
//                            filteredResult += pengumuman
//                        }
//                    }
//
//                    filterResult.values = filteredResult.toList()
//                    filterResult.count = filteredResult.size
//                }
//
//                return filterResult
//            }
//
//            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
//                listPengumuman = p1!!.values as List<Pengumuman>
//                notifyDataSetChanged()
//            }
//
//        }
//        return filter
//    }

}