package repo.pattimuradev.fsearch.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import kotlinx.android.synthetic.main.fragment_notifikasi.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.CustomObserver.observeOnce
import repo.pattimuradev.fsearch.misc.DateAndTimeHandler
import repo.pattimuradev.fsearch.model.Notifikasi
import repo.pattimuradev.fsearch.view.adapter.NotifikasiAdapter
import repo.pattimuradev.fsearch.viewmodel.NotifikasiViewModel
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class NotifikasiFragment : Fragment() {

    private lateinit var notifikasiAdapter: NotifikasiAdapter
    private val notifikasiViewModel: NotifikasiViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notifikasi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionBar()
        notifikasi_button_back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_notifikasiFragment_to_homeFragment)
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_notifikasiFragment_to_homeFragment)
            }
        })
    }

    /**
     * Fungsi untuk menginisiasi action bar pada halaman notifikasi
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun initActionBar() {
        notifikasi_action_bar.inflateMenu(R.menu.custom_fragment_toolbar_menu)
        notifikasi_action_bar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.search_view -> {
                    true
                }
                R.id.go_to_notification -> {
                    true
                }
                R.id.go_to_favorit -> {
                    true
                }
                else -> false
            }
        }

        val badgeDrawable = BadgeDrawable.create(requireContext())
        userViewModel.currentUser.observe(viewLifecycleOwner){ currentUser ->
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                notifikasiViewModel.getJumlahNotifikasiBelumDirespon(currentUser.uid)
            }
            notifikasiViewModel.jumlahNotifikasiBelumDirespon.observe(viewLifecycleOwner){ jumlah ->
                if(jumlah == 0){
                    badgeDrawable.isVisible = false
                }else{
                    badgeDrawable.isVisible = true
                    badgeDrawable.backgroundColor = resources.getColor(R.color.secondary_one, null)
                    badgeDrawable.number = jumlah
                }
            }
        }
        val toolbar = notifikasi_action_bar
        BadgeUtils.attachBadgeDrawable(badgeDrawable, toolbar, R.id.go_to_notification)

        initNotifikasiAdapter()
    }

    /**
     * Fungsi untuk menginisiasi daftar notifikasi
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun initNotifikasiAdapter() {
        notifikasiAdapter = NotifikasiAdapter{ notifikasi ->
            if(notifikasi.jenisNotifikasi == "permintaan_pertemanan"){
                if(notifikasi.responded == false){
                    handlePermintaanPertemananRespon(notifikasi)
                }else{
                    Toast.makeText(requireContext(), "Permintaan sudah mendapat respon!", Toast.LENGTH_SHORT).show()
                }

            }else if(notifikasi.jenisNotifikasi == "pengajuan_bergabung_tim" || notifikasi.jenisNotifikasi == "mengajak_bergabung_tim"){
                if(notifikasi.responded == false){
                    val bundle = Bundle()
                    bundle.putParcelable("notifikasi", notifikasi)
                    Navigation.findNavController(requireView()).navigate(R.id.action_notifikasiFragment_to_detailNotifikasiFragment, bundle)
                }else{
                    Toast.makeText(requireContext(), "Anda sudah merespon!", Toast.LENGTH_SHORT).show()
                }

            }else if(notifikasi.jenisNotifikasi == "respon_pengajuan_bergabung_tim" || notifikasi.jenisNotifikasi == "respon_ajakan_bergabung_tim"){
                handleOnlyReadNotification(notifikasi)
                val bundle = Bundle()
                bundle.putParcelable("notifikasi", notifikasi)
                Navigation.findNavController(requireView()).navigate(R.id.action_notifikasiFragment_to_detailNotifikasiFragment, bundle)
            }else{
                handleOnlyReadNotification(notifikasi)
            }
        }
        notifikasi_rv_notifikasi.layoutManager = LinearLayoutManager(requireContext())
        notifikasi_rv_notifikasi.adapter = notifikasiAdapter

        userViewModel.currentUser.observe(viewLifecycleOwner){
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                notifikasiViewModel.getAllNotifikasi(it.uid)
            }
        }

        notifikasiViewModel.listNotifikasi.observe(viewLifecycleOwner){
            notifikasiAdapter.setListNotifikasi(it)
            notifikasiAdapter.notifyDataSetChanged()
        }
    }

    /**
     * Fungsi untuk menghandle aksi ketika notifikasi bersifat read-only di klik
     * @author PattimuraDev (Dwi Satria Patra)
     * @param notifikasi objek notifikasi yang sedang diinteraksi
     */
    private fun handleOnlyReadNotification(notifikasi: Notifikasi) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
            notifikasiViewModel.updateNotifikasiIsResponded("", notifikasi.idNotifikasi!!)
        }
    }

    /**
     * Fungsi untuk menghandle request untuk mengirimkan respon permintaan pertemanan
     * @author PattimuraDev (Dwi Satria Patra)
     * @param notifikasi objek notifikasi yang sedang diinteraksi
     */
    private fun handlePermintaanPertemananRespon(notifikasi: Notifikasi) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setMessage("Bagaimana respon kamu terhadap permintaan pertemaan dari ${notifikasi.namaPengirim}")
            .setPositiveButton("Terima"){ _, _ ->
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                    notifikasiViewModel.updateNotifikasiIsResponded("Terima", notifikasi.idNotifikasi!!)
                    userViewModel.addFriend(notifikasi.idPengirim, notifikasi.idPenerima)
                }

                userViewModel.addFriendStatus.observeOnce(viewLifecycleOwner){ status ->
                    when (status) {
                        "OK" -> {
                            userViewModel.currentUserProfile.observeOnce(viewLifecycleOwner){ currentUserProfile ->
                                val urlFotoPengirim = currentUserProfile.urlFoto
                                val jenisNotifikasi = "menerima_permintaan_pertemanan"
                                val idPengirim = currentUserProfile.id
                                val namaPengirim = currentUserProfile.nama
                                val prodiPengirim = if(currentUserProfile.dataDiri == null){
                                    null
                                }else{
                                    currentUserProfile.dataDiri.programStudi
                                }
                                val asalUniversitasPengirim = if(currentUserProfile.dataDiri == null){
                                    null
                                }else{
                                    currentUserProfile.dataDiri.asalUniversitas
                                }
                                val tahunAngkatanPengirim = if(currentUserProfile.dataDiri == null){
                                    null
                                }else{
                                    currentUserProfile.dataDiri.tahunAngkatan
                                }
                                val riwayatNotifikasi = DateAndTimeHandler.currentDate()
                                val idPenerima = notifikasi.idPengirim
                                val namaPenerima = notifikasi.namaPengirim
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
                                            riwayatNotifikasi,
                                            null,
                                            null,
                                            null,
                                            idPenerima,
                                            namaPenerima,
                                            false,
                                            null
                                        ),
                                        null,
                                        false,
                                        null
                                    )
                                }
                            }
                            Toast.makeText(requireContext(), "Respon berhasil dikirimkan", Toast.LENGTH_SHORT).show()
                        }
                        "ALREADY A FRIEND" -> {
                            Toast.makeText(requireContext(), "Anda sudah berteman", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(requireContext(), "Gagal", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("Tolak"){ dialogInterface, _ ->
                userViewModel.currentUserProfile.observeOnce(viewLifecycleOwner){ currentUserProfile ->
                    val urlFotoPengirim = currentUserProfile.urlFoto
                    val jenisNotifikasi = "menolak_permintaan_pertemanan"
                    val idPengirim = currentUserProfile.id
                    val namaPengirim = currentUserProfile.nama
                    val prodiPengirim = if(currentUserProfile.dataDiri == null){
                        null
                    }else{
                        currentUserProfile.dataDiri.programStudi
                    }
                    val asalUniversitasPengirim = if(currentUserProfile.dataDiri == null){
                        null
                    }else{
                        currentUserProfile.dataDiri.asalUniversitas
                    }
                    val tahunAngkatanPengirim = if(currentUserProfile.dataDiri == null){
                        null
                    }else{
                        currentUserProfile.dataDiri.tahunAngkatan
                    }
                    val riwayatNotifikasi = DateAndTimeHandler.currentDate()
                    val idPenerima = notifikasi.idPengirim
                    val namaPenerima = notifikasi.namaPengirim
                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                        notifikasiViewModel.updateNotifikasiIsResponded("Tolak", notifikasi.idNotifikasi!!)
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
                                riwayatNotifikasi,
                                null,
                                null,
                                null,
                                idPenerima,
                                namaPenerima,
                                false,
                                null
                            ),
                            null,
                            false,
                            null
                        )
                    }
                }
                Toast.makeText(requireContext(), "Respon berhasil dikirimkan", Toast.LENGTH_SHORT).show()
                dialogInterface.dismiss()
            }
        val alertDialog = dialogBuilder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }

}