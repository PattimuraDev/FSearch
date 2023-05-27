package repo.pattimuradev.fsearch.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_adapter_notifikasi.view.*
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.DateAndTimeHandler
import repo.pattimuradev.fsearch.model.NotificationType
import repo.pattimuradev.fsearch.model.Notifikasi

class NotifikasiAdapter(private val onClick: (Notifikasi) -> Unit): RecyclerView.Adapter<NotifikasiAdapter.ViewHolder>() {
    private var listNotifikasi: List<Notifikasi>? = null

    fun setListNotifikasi(listNotifikasi: List<Notifikasi>){
        this.listNotifikasi = listNotifikasi
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_adapter_notifikasi, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView){
            rv_notifikasi_nama_pengirim.text = listNotifikasi!![position].namaPengirim
            rv_notifikasi_riwayat_posting.text = DateAndTimeHandler.getTimeAgo(listNotifikasi!![position].riwayatNotifikasi!!.time)
            rv_notifikasi_asal_prodi_pengirim.text = listNotifikasi!![position].prodiPengirim
            rv_notifikasi_asal_universitas_pengirim.text = listNotifikasi!![position].asalUniversitasPengirim
            rv_notifikasi_tahun_angkatan_pengirim.text = if(listNotifikasi!![position].tahunAngkatanPengirim != null){
                listNotifikasi!![position].tahunAngkatanPengirim.toString()
            }else{
                ""
            }
            rv_notifikasi_deskripsi_singkat_notifikasi.text = when(listNotifikasi!![position].jenisNotifikasi){
                "permintaan_pertemanan" -> NotificationType.PERMINTAAN_PERTEMANAN.pesanSingkatNotifikasi
                "menerima_permintaan_pertemanan" -> NotificationType.MENERIMA_PERMINTAAN_PERTEMANAN.pesanSingkatNotifikasi
                "menolak_permintaan_pertemanan" -> NotificationType.MENOLAK_PERMINTAAN_PERTEMANAN.pesanSingkatNotifikasi
                "pengajuan_bergabung_tim" -> NotificationType.PENGAJUAN_BERGABUNG_TIM.pesanSingkatNotifikasi
                "respon_pengajuan_bergabung_tim" -> NotificationType.RESPON_PENGAJUAN_BERGABUNG_TIM.pesanSingkatNotifikasi
                "mengajak_bergabung_tim" -> NotificationType.MENGAJAK_BERGABUNG_TIM.pesanSingkatNotifikasi
                "respon_ajakan_bergabung_tim" -> NotificationType.RESPON_AJAKAN_BERGABUNG_TIM.pesanSingkatNotifikasi
                else -> null
            }

            rv_notifikasi_container.setOnClickListener {
                onClick(listNotifikasi!![position])
            }

            if(listNotifikasi!![position].responded == false){
                setBackgroundColor(Color.parseColor("#260084FF"))
            }else{
                setBackgroundColor(Color.WHITE)
            }

            Glide.with(rv_notifikasi_foto_pengirim.context)
                .load(listNotifikasi!![position].urlFotoPengirim)
                .error(R.drawable.standard_user_photo)
                .into(rv_notifikasi_foto_pengirim)
        }
    }

    override fun getItemCount(): Int {
        return if(listNotifikasi.isNullOrEmpty()){
            0
        }else{
            listNotifikasi!!.size
        }
    }
}