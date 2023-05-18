package repo.pattimuradev.fsearch.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_adapter_lomba.view.*
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.LombaClickListener
import repo.pattimuradev.fsearch.model.Lomba
import java.util.*

class LombaAdapter(private val lombaClickListener: LombaClickListener): RecyclerView.Adapter<LombaAdapter.ViewHolder>(), Filterable{
    private var listLomba: List<Lomba>? = null
    private var listLombaFiltered: List<Lomba>? = null
    fun setListLomba(listLomba: List<Lomba>){
        this.listLomba = listLomba
        this.listLombaFiltered = listLomba
        notifyDataSetChanged()
    }
    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_adapter_lomba, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView){
            rv_lomba_judul_lomba.text = listLomba!![position].judulLomba
            rv_lomba_nama_penyelenggara.text = listLomba!![position].penyelenggaraLomba
            rv_lomba_lokasi_lomba.text = listLomba!![position].lokasi
            rv_lomba_tanggal_lomba.text = listLomba!![position].tanggalPelaksanaan
            rv_lomba_biaya_pendaftaran.text = "Rp ${listLomba!![position].biayaPendaftaran}"
            Glide.with(rv_lomba_poster_lomba.context)
                .load(listLomba!![position].posterLombaUrl)
                .error(R.drawable.no_image_available)
                .into(rv_lomba_poster_lomba)

            rv_lomba_container.setOnClickListener {
                lombaClickListener.clickOnDaftarLombaBody(listLomba!![position], position)
            }

            rv_lomba_button_like.setOnClickListener {
                lombaClickListener.clickOnLikeButton(listLomba!![position], position)
            }
        }
    }

    override fun getItemCount(): Int {
        return if(listLomba.isNullOrEmpty()){
            0
        }else{
            listLomba!!.size
        }
    }

    override fun getFilter(): Filter {
        val filter = object: Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val filterResult = FilterResults()
                if(p0.isNullOrEmpty()){
                    filterResult.values = listLombaFiltered
                    filterResult.count = listLombaFiltered!!.size
                }else{
                    val searchChar = p0.toString().lowercase(Locale.getDefault())
                    val filteredResult = mutableListOf<Lomba>()
                    for(lomba in listLombaFiltered!!){
                        if(lomba.judulLomba.lowercase(Locale.getDefault()).contains(searchChar)){
                            filteredResult += lomba
                        }
                    }

                    filterResult.values = filteredResult.toList()
                    filterResult.count = filteredResult.size
                }

                return filterResult
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                listLomba = p1!!.values as List<Lomba>
                notifyDataSetChanged()
            }

        }
        return filter
    }
}