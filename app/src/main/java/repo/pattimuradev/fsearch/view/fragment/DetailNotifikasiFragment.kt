package repo.pattimuradev.fsearch.view.fragment

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_detail_notifikasi.*
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.DateAndTimeHandler
import repo.pattimuradev.fsearch.model.Notifikasi

class DetailNotifikasiFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_notifikasi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        detail_notifikasi_button_back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_detailTawaranAjakanFragment_to_notifikasiFragment)
        }
        detail_notifikasi_button_respon.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val notifikasi = arguments!!.getParcelable("notifikasi") as Notifikasi?
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        if(notifikasi!!.jenisNotifikasi == "pengajuan_bergabung_tim"){
            alertDialogBuilder.setMessage("Bagaimana respon kamu terhadap permintaan dari ${notifikasi.namaPengirim} untuk bergabung ke tim lombamu?")
        }else if(notifikasi.jenisNotifikasi == "mengajak_bergabung_tim"){
            alertDialogBuilder.setMessage("Bagaimana respon kamu terhadap ajakan dari ${notifikasi.namaPengirim}?")
        }
        alertDialogBuilder.setPositiveButton("Ya"){_, _ ->
            val bundle = Bundle()
            bundle.putParcelable("notifikasi", notifikasi)
            bundle.putString("respon", "Terima")
            Navigation.findNavController(requireView()).navigate(R.id.action_detailTawaranAjakanFragment_to_formBalasanFragment, bundle)
        }
        alertDialogBuilder.setNegativeButton("Tidak"){ _, _ ->
            val bundle = Bundle()
            bundle.putParcelable("notifikasi", notifikasi)
            bundle.putString("respon", "Tolak")
            Navigation.findNavController(requireView()).navigate(R.id.action_detailTawaranAjakanFragment_to_formBalasanFragment, bundle)
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }

    private fun initData() {
        val notifikasi = arguments!!.getParcelable("notifikasi") as Notifikasi?
        if(notifikasi!!.jenisNotifikasi == "respon_pengajuan_bergabung_tim" || notifikasi.jenisNotifikasi == "respon_ajakan_bergabung_tim"){
            detail_notifikasi_button_respon.isVisible = false
            detail_notifikasi_title.text = "Detail Notifikasi"
        }else{
            detail_notifikasi_title.text = "Tawaran Ajakan"
        }
        val nama = notifikasi.namaPengirim
        val asalProdiPengirim = notifikasi.prodiPengirim
        val asalUniversitasPengirim = notifikasi.asalUniversitasPengirim
        val tahunAngkatanPengirim = notifikasi.tahunAngkatanPengirim
        val deskripsiNotifikasi = notifikasi.deskripsiLengkap
        val jenisLampiran = notifikasi.jenisLampiran
        val urlFotoPengirim = notifikasi.urlFotoPengirim
        val fileUrl = notifikasi.urlLampiran
        val waktuPosting = notifikasi.riwayatNotifikasi

        detail_notifikasi_nama_pengirim.text = nama
        detail_notifikasi_asal_prodi_pengirim.text = asalProdiPengirim
        detail_notifikasi_asal_universitas_pengirim.text = asalUniversitasPengirim
        detail_notifikasi_tahun_angkatan_pengirim.text = tahunAngkatanPengirim?.toString() ?: ""
        detail_notifikasi_deskripsi_lengkap.text = deskripsiNotifikasi
        detail_notifikasi_waktu_penerimaan_notifikasi.text = DateAndTimeHandler.formatTanggalPostingDetailNotifikasi(waktuPosting!!.time)
        Glide.with(detail_notifikasi_foto_pengirim.context)
            .load(urlFotoPengirim)
            .error(R.drawable.standard_user_photo)
            .into(detail_notifikasi_foto_pengirim)

        when(jenisLampiran){
            "pdf" -> {
                detail_notifikasi_poster.isVisible = false
                detail_notifikasi_button_buka_pdf.setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(fileUrl)))
                }
            }
            "image" -> {
                detail_notifikasi_button_buka_pdf.isVisible = false
                Glide.with(detail_notifikasi_poster.context)
                    .load(fileUrl)
                    .error(R.drawable.no_image_available)
                    .into(detail_notifikasi_poster)
            }
            else -> {
                detail_notifikasi_poster.isVisible = false
                detail_notifikasi_button_buka_pdf.isVisible = false
            }
        }
    }
}