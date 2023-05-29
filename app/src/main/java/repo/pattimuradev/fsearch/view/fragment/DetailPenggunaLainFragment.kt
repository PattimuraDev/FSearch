package repo.pattimuradev.fsearch.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_detail_pengguna_lain_fagment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.CustomObserver.observeOnce
import repo.pattimuradev.fsearch.misc.DateAndTimeHandler
import repo.pattimuradev.fsearch.model.Notifikasi
import repo.pattimuradev.fsearch.view.adapter.DetailPenggunaLainViewPagerAdapter
import repo.pattimuradev.fsearch.viewmodel.NotifikasiViewModel
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class DetailPenggunaLainFragment : Fragment() {

    private lateinit var detailPenggunaLainViewPagerAdapter: DetailPenggunaLainViewPagerAdapter
    private val userViewModel: UserViewModel by viewModels()
    private val notifikasiViewModel: NotifikasiViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_pengguna_lain_fagment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDetailPengguna()
        initViewPagerAdapter()

        detail_pengguna_lain_button_back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_detailPenggunaLainFagment_to_penggunaFragment)
        }

        detail_pengguna_lain_button_ajak.setOnClickListener {
            userViewModel.spesificUserById.observeOnce(viewLifecycleOwner){ userProfile ->
                if(userProfile.statusBersediaMenerimaAjakan){
                    val bundle = Bundle()
                    bundle.putParcelable("profile_pengguna_lain", userProfile)
                    Navigation.findNavController(view).navigate(R.id.action_detailPenggunaLainFagment_to_formAjakanBergabungTimFragment, bundle)
                }else{
                    Toast.makeText(context, "Pengguna tidak menerima permintaan ajakan", Toast.LENGTH_SHORT).show()
                }
            }
        }

        detail_pengguna_lain_button_tambah_teman.setOnClickListener {
            tambahTemanHandler()
        }

        detail_pengguna_lain_button_tulis_testimoni.setOnClickListener {
            userViewModel.spesificUserById.observeOnce(viewLifecycleOwner){ userProfile ->
                val bundle = Bundle()
                bundle.putParcelable("profile_pengguna_lain", userProfile)
                Navigation.findNavController(view).navigate(R.id.action_detailPenggunaLainFagment_to_formTestimoniFragment, bundle)
            }
        }
    }

    /**
     * Fungsi untuk menghandle aktivitas tambah teman
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun tambahTemanHandler() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setMessage("Anda yakin ingin mengajak berteman pengguna ini?")
            .setPositiveButton("ya"){ _, _ ->
                userViewModel.currentUserProfile.observeOnce(viewLifecycleOwner){ userProfile ->
                    val urlFotoPengirim = userProfile.urlFoto
                    val jenisNotifikasi = "permintaan_pertemanan"
                    val idPengirim = userProfile.id
                    val namaPengirim = userProfile.nama
                    val prodiPengirim = if(userProfile.dataDiri == null){
                        null
                    }else{
                        userProfile.dataDiri.programStudi
                    }
                    val asalUniversitasPengirim = if(userProfile.dataDiri == null){
                        null
                    }else{
                        userProfile.dataDiri.asalUniversitas
                    }
                    val tahunAngkatanPengirim = if(userProfile.dataDiri == null){
                        null
                    }else{
                        userProfile.dataDiri.tahunAngkatan
                    }

                    userViewModel.spesificUserById.observeOnce(viewLifecycleOwner){ penggunaLain ->
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                            notifikasiViewModel.addNotifikasi(
                                Notifikasi(
                                    null,
                                    urlFotoPengirim,
                                    jenisNotifikasi,
                                    idPengirim!!,
                                    namaPengirim,
                                    prodiPengirim,
                                    asalUniversitasPengirim,
                                    tahunAngkatanPengirim,
                                    DateAndTimeHandler.currentDate(),
                                    null,
                                    null,
                                    null,
                                    penggunaLain.id!!,
                                    penggunaLain.nama,
                                    false,
                                    null
                                ),
                                null,
                                false,
                                null)
                        }
                    }

                    notifikasiViewModel.addNotifikasiStatus.observeOnce(viewLifecycleOwner){
                        if(it == "OK"){
                            Toast.makeText(requireContext(), "Permintaan pertemanan berhasil dikirimkan", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(requireContext(), "Gagal mengirimkan permintaan pertemanan", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("Tidak"){ dialogInterface, _ ->
                dialogInterface.dismiss()
            }
        val alertDialog = dialogBuilder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }

    /**
     * Fungsi untuk menginisiasi data pengguna lain sehingga bisa ditampilkan
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun initDetailPengguna() {
        userViewModel.spesificUserById.observe(viewLifecycleOwner){ userProfile ->
            detail_pengguna_lain_nama_pengguna.text = userProfile.nama
            detail_pengguna_lain_jumlah_like.text = if(userProfile.jumlahLike == null){
                "0"
            }else{
                userProfile.jumlahLike.toString()
            }
            detail_pengguna_lain_jumlah_teman.text = if(userProfile.jumlahTeman == null){
                "0"
            }else{
                userProfile.jumlahTeman.toString()
            }
            detail_pengguna_lain_bio.text = if(userProfile.bio == null){
                ""
            }else{
                userProfile.bio.toString()
            }
            Glide.with(detail_pengguna_lain_foto_profil_user.context)
                .load(userProfile.urlFoto)
                .error(R.drawable.standard_user_photo)
                .into(detail_pengguna_lain_foto_profil_user)
        }
    }

    /**
     * Fungsi untuk menginisiasi tab layout dan view pager pada halaman
     * profil pengguna lain
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun initViewPagerAdapter() {
        detailPenggunaLainViewPagerAdapter = DetailPenggunaLainViewPagerAdapter(this)
        detail_pengguna_lain_view_pager.adapter = detailPenggunaLainViewPagerAdapter

        TabLayoutMediator(detail_pengguna_lain_tab_layout, detail_pengguna_lain_view_pager){ tab, position ->
            tab.text = when(position){
                0 -> "Data Diri"
                1 -> "Testimoni"
                else -> "Post"
            }
        }.attach()
    }
}