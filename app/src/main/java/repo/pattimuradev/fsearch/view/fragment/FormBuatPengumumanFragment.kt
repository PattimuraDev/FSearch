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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.custom_dialog.view.*
import kotlinx.android.synthetic.main.fragment_form_buat_pengumuman.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.DateAndTimeHandler
import repo.pattimuradev.fsearch.model.Pengumuman
import repo.pattimuradev.fsearch.viewmodel.PengumumanViewModel
import repo.pattimuradev.fsearch.viewmodel.UserViewModel
import repo.pattimuradev.fsearch.misc.CustomObserver.observeOnce

class FormBuatPengumumanFragment : Fragment() {
    private val userViewModel: UserViewModel by viewModels()
    private val pengumumanViewModel: PengumumanViewModel by viewModels()
    private var posterImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_form_buat_pengumuman, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkDeskripsiField()
        form_buat_pengumuman_button_upload_file.setOnClickListener {
            handleTakeImage()
        }
        form_buat_pengumuman_button_back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_formBuatPengumumanFragment_to_homeFragment)
        }
        form_buat_pengumuman_button_post.setOnClickListener {
            handlePostPengumuman()
        }
    }

    private fun handlePostPengumuman() {
        userViewModel.currentUser.observeOnce(viewLifecycleOwner){currentUser ->
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                userViewModel.getUserProfile(currentUser.uid)
                if(posterImageUri != null){
                    pengumumanViewModel.postImageToStorage(posterImageUri!!, true)
                }else{
                    pengumumanViewModel.postImageToStorage(null, false)
                }
            }
        }

        userViewModel.currentUserProfile.observeOnce(viewLifecycleOwner){ currentUserProfile ->
            val fotoProfilUrl = currentUserProfile.urlFoto
            val namaPengirim = currentUserProfile.nama
            val asalProgramStudi = if(currentUserProfile.dataDiri?.programStudi.isNullOrEmpty()){
                null
            }else{
                currentUserProfile.dataDiri?.programStudi
            }
            val asalUniversitas = if(currentUserProfile.dataDiri?.asalUniversitas.isNullOrEmpty()){
                null
            }else{
                currentUserProfile.dataDiri?.asalUniversitas
            }
            val tahunAngkatan = if(currentUserProfile.dataDiri?.tahunAngkatan == null){
                null
            }else{
                currentUserProfile.dataDiri.tahunAngkatan
            }
            val deskripsiPengumuman = form_buat_pengumuman_deskpripsi.text.toString()
            val idPengirim = if(currentUserProfile.id.isNullOrEmpty()){
                ""
            }else{
                currentUserProfile.id
            }
            pengumumanViewModel.posterPengumumanUrl.observeOnce(viewLifecycleOwner){ posterUrlValue ->
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                    pengumumanViewModel.addPengumuman(Pengumuman(
                        fotoProfilUrl,
                        namaPengirim,
                        asalProgramStudi,
                        asalUniversitas,
                        DateAndTimeHandler.currentDate(),
                        tahunAngkatan,
                        deskripsiPengumuman,
                        posterUrlValue,
                        idPengirim
                    ))
                }
            }
        }

        pengumumanViewModel.addPengumumanStatus.observeOnce(viewLifecycleOwner){ status ->
            if(status == "OK"){
                Navigation.findNavController(requireView()).navigate(R.id.action_formBuatPengumumanFragment_to_homeFragment)
                showCustomDialog()
            }else{
                Toast.makeText(requireContext(), "Gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleTakeImage() {
        getContext.launch("image/*")
    }

    private val getContext = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        posterImageUri = uri
        val contentResolver: ContentResolver = context!!.contentResolver
        val cursor = contentResolver.query(uri!!, null, null, null, null)
        cursor?.moveToFirst()
        val fileName = cursor?.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        cursor?.close()
        form_buat_pengumuman_nama_file.text = fileName
    }

    private fun checkDeskripsiField() {
        form_buat_pengumuman_button_post.isEnabled = false
        form_buat_pengumuman_deskpripsi.addTextChangedListener ( object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // no action neeeded
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val deskripsiField = form_buat_pengumuman_deskpripsi.text.toString()
                form_buat_pengumuman_button_post.isEnabled = deskripsiField.trim().isNotEmpty()
                if(form_buat_pengumuman_button_post.isEnabled){
                    form_buat_pengumuman_button_post.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_button_enabled_layout, null)
                    form_buat_pengumuman_word_count.text = "${deskripsiField.length}/500"
                }else{
                    form_buat_pengumuman_button_post.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_button_not_enabled_layout, null)
                    form_buat_pengumuman_word_count.text = "0/500"
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                // no action needed
            }

        })
    }

    private fun showCustomDialog(){
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog, null)
        val customDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()
        dialogView.custom_dialog_message.text = "Selamat! kamu telah berhasil membuat post pengumuman"
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customDialog.window!!.attributes.gravity = Gravity.TOP
        customDialog.window!!.attributes.x = 50
        customDialog.window!!.attributes.y = 50
        customDialog.show()
    }
}