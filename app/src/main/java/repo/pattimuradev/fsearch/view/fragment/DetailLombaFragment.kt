package repo.pattimuradev.fsearch.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_detail_lomba.*
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.DateAndTimeHandler
import repo.pattimuradev.fsearch.model.Lomba

class DetailLombaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_lomba, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDetailLombaData()

        detail_lomba_button_back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_detailLombaFragment_to_lombaFragment)
        }
    }

    private fun initDetailLombaData() {
        val lomba = arguments!!.getParcelable("lomba") as Lomba?
        val tanggalPosting = DateAndTimeHandler.formatTanggalDaftarLombaPickerDialog(
            lomba!!.tanggalPosting!!.year,
            lomba.tanggalPosting!!.month,
            lomba.tanggalPosting.day
        )

        Glide.with(detail_lomba_poster.context)
            .load(lomba.posterLombaUrl)
            .error(R.drawable.no_image_available)
            .into(detail_lomba_poster)
        detail_lomba_tanggal_posting.text = tanggalPosting
        detail_lomba_judul_lomba.text = lomba.judulLomba
        detail_lomba_nama_penyelenggara.text = lomba.penyelenggaraLomba
        detail_lomba_lokasi_lomba.text = lomba.lokasi
        detail_lomba_tanggal_lomba.text = lomba.tanggalPelaksanaan
        detail_lomba_biaya_pendaftaran.text = lomba.biayaPendaftaran
        detail_lomba_deskripsi_lomba.text = lomba.deskripsiLomba
    }
}