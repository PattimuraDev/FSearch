package repo.pattimuradev.fsearch.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.custom_notification_dialog.view.*
import kotlinx.android.synthetic.main.fragment_form_balasan.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.CustomObserver.observeOnce
import repo.pattimuradev.fsearch.misc.DateAndTimeHandler
import repo.pattimuradev.fsearch.model.Notifikasi
import repo.pattimuradev.fsearch.viewmodel.NotifikasiViewModel
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class FormBalasanFragment : Fragment() {

    private val notifikasiViewModel: NotifikasiViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_form_balasan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val notifikasi = arguments!!.getParcelable("notifikasi") as Notifikasi?
        val respon = arguments!!.getString("respon")!!
        checkDeskripsiField()
        form_balasan_button_back.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("notifikasi", notifikasi)
            Navigation.findNavController(view).navigate(R.id.action_formBalasanFragment_to_detailTawaranAjakanFragment, bundle)
        }
        form_balasan_button_kirim.setOnClickListener {
            handleBalasan(notifikasi!!, respon)
        }
    }

    private fun handleBalasan(notifikasi: Notifikasi, respon: String) {
        val idPenerima = notifikasi.idPengirim
        val namaPenerima = notifikasi.namaPengirim
        userViewModel.currentUserProfile.observe(viewLifecycleOwner){ userProfile ->
            val urlFotoPengirim = userProfile.urlFoto
            val jenisNotifikasi = if(notifikasi.jenisNotifikasi == "pengajuan_bergabung_tim"){
                "respon_pengajuan_bergabung_tim"
            }else{
                "respon_ajakan_bergabung_tim"
            }
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
            val riwayatNotifikasi = DateAndTimeHandler.currentDate()
            val deskripsiLengkap = form_balasan_deskripsi.text.toString().trim()

            val notifikasiBalasan = Notifikasi(
                null,
                urlFotoPengirim,
                jenisNotifikasi,
                idPengirim!!,
                namaPengirim,
                prodiPengirim,
                asalUniversitasPengirim,
                tahunAngkatanPengirim,
                riwayatNotifikasi,
                deskripsiLengkap,
                null,
                null,
                idPenerima,
                namaPenerima,
                false,
                respon
            )

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                if(respon == "Terima"){
                    userViewModel.addTeamUpHistory(notifikasi.idPengirim, notifikasi.idPenerima)
                }
                notifikasiViewModel.updateNotifikasiIsResponded("", notifikasi.idNotifikasi!!)
                notifikasiViewModel.addNotifikasi(notifikasiBalasan, null, false, null)
            }
        }

        notifikasiViewModel.addNotifikasiStatus.observeOnce(viewLifecycleOwner){
            if(it == "OK"){
                val bundle = Bundle()
                bundle.putParcelable("notifikasi", notifikasi)
                Navigation.findNavController(requireView()).navigate(R.id.action_formBalasanFragment_to_detailTawaranAjakanFragment, bundle)
                showCustomDialog()
            }else{
                Toast.makeText(requireContext(), "Gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showCustomDialog(){
        val dialogView = layoutInflater.inflate(R.layout.custom_notification_dialog, null)
        val customDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()
        dialogView.custom_notification_dialog_message.text = "Respon jawaban kamu telah berhasil disampaikan"
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customDialog.window!!.attributes.gravity = Gravity.TOP
        customDialog.window!!.attributes.verticalMargin = 0.2F
        customDialog.show()
    }

    private fun checkDeskripsiField() {
        form_balasan_button_kirim.isEnabled = false
        form_balasan_deskripsi.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // no action needed
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val deskripsiField = form_balasan_deskripsi.text.toString()
                form_balasan_button_kirim.isEnabled = deskripsiField.trim().isNotEmpty()
                if(form_balasan_button_kirim.isEnabled){
                    form_balasan_button_kirim.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_button_enabled_layout, null)
                    form_balasan_word_count.text = "${deskripsiField.length}/500"
                }else{
                    form_balasan_button_kirim.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_button_not_enabled_layout, null)
                    form_balasan_word_count.text = "0/500"
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                // no action needed
            }

        })
    }
}