package repo.pattimuradev.fsearch.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.custom_notification_dialog.view.*
import kotlinx.android.synthetic.main.fragment_form_testimoni.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.CustomObserver.observeOnce
import repo.pattimuradev.fsearch.misc.DateAndTimeHandler
import repo.pattimuradev.fsearch.model.Testimoni
import repo.pattimuradev.fsearch.model.UserProfile
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class FormTestimoniFragment : Fragment() {

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form_testimoni, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkDeskripsiField()
        form_testimoni_button_post.setOnClickListener {
            handlePostTestimoni()
        }
        form_testimoni_button_back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_formTestimoniFragment_to_detailPenggunaLainFagment)
        }
    }

    /**
     * Fungsi untuk menghandle request untuk mengirimkan testimoni
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun handlePostTestimoni() {
        val profilePenggunaLain = arguments!!.getParcelable("profile_pengguna_lain") as UserProfile?
        val deskripsiTestimoni = form_testimoni_deskripsi.text.toString().trim()
        val rating = form_testimoni_rating_bar.rating
        userViewModel.currentUserProfile.observe(viewLifecycleOwner){ currentUserProfile ->
            val riwayatPosting = DateAndTimeHandler.currentDate().time
            val urlFotoPengirim = currentUserProfile.urlFoto
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
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                userViewModel.addTestimoni(profilePenggunaLain!!.id!!, Testimoni(
                    riwayatPosting,
                    urlFotoPengirim,
                    namaPengirim,
                    prodiPengirim,
                    asalUniversitasPengirim,
                    tahunAngkatanPengirim,
                    rating,
                    deskripsiTestimoni
                ))
            }
        }

        userViewModel.addTestimoniStatus.observeOnce(viewLifecycleOwner){
            when (it) {
                "OK" -> {
                    Navigation.findNavController(requireView()).navigate(R.id.action_formTestimoniFragment_to_detailPenggunaLainFagment)
                    showCustomDialog()
                }
                "BELUM PERNAH SATU TEAM" -> Toast.makeText(requireContext(), "Anda belum pernah satu team", Toast.LENGTH_SHORT).show()
                else -> {
                    Toast.makeText(requireContext(), "Gagal", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Fungsi untuk menampilkan dialog ketika berhasil mengirimkan testimoni
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun showCustomDialog(){
        val dialogView = layoutInflater.inflate(R.layout.custom_notification_dialog, null)
        val customDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()
        dialogView.custom_notification_dialog_message.text = "Testimoni kamu telah berhasil disampaikan"
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customDialog.window!!.attributes.gravity = Gravity.TOP
        customDialog.window!!.attributes.verticalMargin = 0.2F
        customDialog.show()
    }

    /**
     * Fungsi untuk mengecek kelengkapan field/data sebelum dapat mengirimkan testimoni
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun checkDeskripsiField() {
        form_testimoni_button_post.isEnabled = false
        form_testimoni_deskripsi.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // no action needed
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val deskripsiField = form_testimoni_deskripsi.text.toString().trim()
                form_testimoni_button_post.isEnabled = form_testimoni_rating_bar.rating != 0.0F
                if(form_testimoni_button_post.isEnabled){
                    form_testimoni_button_post.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_button_enabled_layout, null)
                    form_testimoni_word_count.text = "${deskripsiField.length}/500"
                }else{
                    form_testimoni_button_post.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_button_not_enabled_layout, null)
                    form_testimoni_word_count.text = "0/500"
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                // no action needed
            }

        })
    }
}