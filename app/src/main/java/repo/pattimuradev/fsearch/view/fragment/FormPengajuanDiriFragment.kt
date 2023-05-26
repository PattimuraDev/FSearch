package repo.pattimuradev.fsearch.view.fragment

import android.app.AlertDialog
import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.custom_notification_dialog.view.*
import kotlinx.android.synthetic.main.fragment_form_pengajuan_diri.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.CustomObserver.observeOnce
import repo.pattimuradev.fsearch.misc.DateAndTimeHandler
import repo.pattimuradev.fsearch.model.Notifikasi
import repo.pattimuradev.fsearch.model.Pengumuman
import repo.pattimuradev.fsearch.viewmodel.NotifikasiViewModel
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class FormPengajuanDiriFragment : Fragment() {

    private var pdfFileUri: Uri? = null
    private val notifikasiViewModel: NotifikasiViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form_pengajuan_diri, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkDeskripsiField()
        form_pengajuan_diri_button_back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_formPengajuanDiriFragment_to_homeFragment)
        }
        form_pengajuan_diri_button_upload_file.setOnClickListener {
            handleTakePdfFile()
        }
        form_pengajuan_diri_button_kirim.setOnClickListener {
            handleKirimPengajuanDiri()
        }
    }

    private fun handleKirimPengajuanDiri() {
        val pengumuman = arguments!!.getParcelable("pengumuman") as Pengumuman?
        val idPenerima = pengumuman!!.idPengirim
        val namaPenerima = pengumuman.namaPengirim


        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
            if(pdfFileUri != null){
                notifikasiViewModel.postFileToStorage(pdfFileUri, true, "pdf")
            }else{
                notifikasiViewModel.postFileToStorage(null, false, null)
            }
        }

        userViewModel.currentUserProfile.observeOnce(viewLifecycleOwner){ userProfile ->
            val urlFotoPengirim = userProfile.urlFoto
            val jenisNotifikasi = "pengajuan_bergabung_tim"
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
            val deskripsiLengkap = form_pengajuan_diri_deskripsi.text.toString().trim()
            val jenisLampiran = "pdf"
            notifikasiViewModel.getFileDownloadUrl.observeOnce(viewLifecycleOwner){ fileUrl ->
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                    notifikasiViewModel.addNotifikasi(Notifikasi(
                        null,
                        urlFotoPengirim,
                        jenisNotifikasi,
                        idPengirim!!,
                        namaPengirim,
                        prodiPengirim,
                        asalUniversitasPengirim,
                        tahunAngkatanPengirim,
                        DateAndTimeHandler.currentDate(),
                        deskripsiLengkap,
                        jenisLampiran,
                        fileUrl,
                        idPenerima!!,
                        namaPenerima,
                        false,
                        null
                    ))
                }
            }

            notifikasiViewModel.addNotifikasiStatus.observeOnce(viewLifecycleOwner){
                if(it == "OK"){
                    Navigation.findNavController(requireView()).navigate(R.id.action_formPengajuanDiriFragment_to_homeFragment)
                    showCustomDialog(namaPenerima)
                }else{
                    Toast.makeText(requireContext(), "Gagal", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showCustomDialog(namaPenerima: String){
        val dialogView = layoutInflater.inflate(R.layout.custom_notification_dialog, null)
        val customDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()
        dialogView.custom_notification_dialog_message.text = "Selamat! kamu telah berhasil mengirim pengajuan bergabung ke tim lomba $namaPenerima"
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customDialog.window!!.attributes.gravity = Gravity.TOP
        customDialog.window!!.attributes.verticalMargin = 0.2F
        customDialog.show()
    }

    private fun handleTakePdfFile() {
        getContext.launch("application/pdf")
    }

    private val getContext = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        pdfFileUri = uri
        val contentResolver: ContentResolver = context!!.contentResolver
        val cursor = contentResolver.query(uri!!, null, null, null, null)
        cursor?.moveToFirst()
        val fileName = cursor?.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        cursor?.close()
        form_pengajuan_diri_nama_file.text = fileName
    }

    private fun checkDeskripsiField() {
        form_pengajuan_diri_button_kirim.isEnabled = false
        form_pengajuan_diri_deskripsi.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // no action needed
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val deskripsiField = form_pengajuan_diri_deskripsi.text.toString()
                form_pengajuan_diri_button_kirim.isEnabled = deskripsiField.trim().isNotEmpty()
                if(form_pengajuan_diri_button_kirim.isEnabled){
                    form_pengajuan_diri_button_kirim.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_button_enabled_layout, null)
                    form_pengajuan_diri_word_count.text = "${deskripsiField.length}/500"
                }else{
                    form_pengajuan_diri_button_kirim.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_button_not_enabled_layout, null)
                    form_pengajuan_diri_word_count.text = "0/500"
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                // no action needed
            }
        })
    }
}