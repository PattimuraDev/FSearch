package repo.pattimuradev.fsearch.view.fragment

import android.app.AlertDialog
import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.custom_notification_dialog.view.*
import kotlinx.android.synthetic.main.fragment_form_update_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.CustomObserver.observeOnce
import repo.pattimuradev.fsearch.model.DataDiriUser
import repo.pattimuradev.fsearch.model.UserProfile
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class FormUpdateProfileFragment : Fragment() {

    private val userViewModel: UserViewModel by viewModels()
    private var fotoUserUri: Uri? = null
    private var namaFileFoto: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_form_update_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        form_update_profil_button_back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_formUpdateProfileFragment_to_profileFragment)
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_formUpdateProfileFragment_to_profileFragment)
            }
        })
        initDataAwal()
        form_update_profil_foto_user.setOnClickListener {
            handleTakeImage()
        }
        handleUpdateProfil()
    }

    /**
     * Fungsi untuk menghandle request untuk mengupdate profil
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun handleUpdateProfil() {
        var statusBersediaMenerimaAjakan = true
        var jenisKelamin: String? = null

        form_update_profil_radio_group_jenis_kelamin.setOnCheckedChangeListener { _, checkedId ->
            jenisKelamin = if(checkedId == R.id.form_update_profil_radio_button_pria){
                "Pria"
            }else{
                "Wanita"
            }
        }

        form_update_profil_button_status_bersedia.setOnCheckedChangeListener { _, isChecked ->
            statusBersediaMenerimaAjakan = isChecked
        }

        form_update_profil_button_simpan.setOnClickListener {
            if(form_update_profil_nama.text.toString().trim().isEmpty()){
                Toast.makeText(requireContext(), "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            }else{
                val nama = form_update_profil_nama.text.toString().trim()
                val bio = form_update_profil_bio.text.toString().ifEmpty {
                    null
                }
                val universitas = form_update_profil_universitas.text.toString().ifEmpty {
                    null
                }
                val tahunAngkatan = form_update_profil_tahun_angkatan.text.toString().ifEmpty {
                    null
                }
                val fakultas = form_update_profil_fakultas.text.toString().ifEmpty {
                    null
                }
                val jurusan = form_update_profil_jurusan.text.toString().ifEmpty {
                    null
                }
                val prodi = form_update_profil_prodi.text.toString().ifEmpty {
                    null
                }
                val keminatan = form_update_profil_keminatan.text.toString().ifEmpty {
                    null
                }
                val umur = form_update_profil_umur.text.toString().ifEmpty {
                    null
                }
                val kotaAsal = form_update_profil_kota_asal.text.toString().ifEmpty {
                    null
                }
                val kepribadian = form_update_profil_kepribadian.text.toString().ifEmpty {
                    null
                }
                val fotoProfilUri = fotoUserUri
                val namaFileFotoBaru = namaFileFoto

                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                    if(fotoProfilUri != null){
                        userViewModel.getFotoProfilUrl(fotoProfilUri, true, namaFileFotoBaru)
                    }else{
                        userViewModel.getFotoProfilUrl(null, false, null)
                    }
                }

                userViewModel.currentUserFotoProfileUrl.observe(viewLifecycleOwner){ fotoProfilUrl ->
                    lifecycleScope.launch(Dispatchers.IO){
                        userViewModel.updateProfile(UserProfile(
                            nama = nama,
                            email = "",
                            urlFoto = fotoProfilUrl,
                            bio = bio,
                            statusBersediaMenerimaAjakan = statusBersediaMenerimaAjakan,
                            dataDiri = DataDiriUser(
                                asalUniversitas = universitas,
                                tahunAngkatan = tahunAngkatan!!.toInt(),
                                jurusan = jurusan,
                                programStudi = prodi,
                                keminatan = keminatan,
                                jenisKelamin = jenisKelamin,
                                umur = umur!!.toInt(),
                                asalKota = kotaAsal,
                                fakultas = fakultas,
                                kepribadian = kepribadian
                            )
                        ))
                    }
                }

                userViewModel.updateUserStatus.observe(viewLifecycleOwner){status ->
                    if(status == "OK"){
                        Navigation.findNavController(requireView()).navigate(R.id.action_formUpdateProfileFragment_to_profileFragment)
                        showCustomDialog()
                    }else{
                        Toast.makeText(requireContext(), "Gagal mengupdate profil", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    /**
     * Fungsi untuk menghandle aksi untuk mengambil image dari penyimpanan lokal
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun handleTakeImage() {
        getContext.launch("image/*")
    }

    private val getContext = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        fotoUserUri = uri
        Glide.with(form_update_profil_foto_user.context)
            .load(uri)
            .error(R.drawable.standard_user_photo)
            .into(form_update_profil_foto_user)
        val contentResolver: ContentResolver = context!!.contentResolver
        val cursor = contentResolver.query(uri!!, null, null, null, null)
        cursor?.moveToFirst()
        val fileName = cursor?.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        cursor?.close()
        namaFileFoto = fileName!!
    }

    /**
     * Fungsi untuk menginisiasi data user awal pada field-field yang tersedia
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun initDataAwal() {
        userViewModel.currentUserProfile.observeOnce(viewLifecycleOwner){ userProfileAwal ->
            Glide.with(form_update_profil_foto_user.context)
                .load(userProfileAwal.urlFoto)
                .error(R.drawable.standard_user_photo)
                .into(form_update_profil_foto_user)

            form_update_profil_nama.setText(userProfileAwal.nama)
            form_update_profil_bio.setText(userProfileAwal.bio ?: "")
            form_update_profil_universitas.setText(if(userProfileAwal.dataDiri == null){
                ""
            }else{
                userProfileAwal.dataDiri.asalUniversitas ?: ""
            })
            form_update_profil_button_status_bersedia.isChecked = userProfileAwal.statusBersediaMenerimaAjakan
            form_update_profil_tahun_angkatan.setText(if(userProfileAwal.dataDiri == null){
                ""
            }else{
                userProfileAwal.dataDiri.tahunAngkatan.toString().ifEmpty {
                    ""
                }
            })
            form_update_profil_fakultas.setText(if(userProfileAwal.dataDiri == null){
                ""
            }else{
                userProfileAwal.dataDiri.fakultas ?: ""
            })
            form_update_profil_jurusan.setText(if(userProfileAwal.dataDiri == null){
                ""
            }else{
                userProfileAwal.dataDiri.jurusan ?: ""
            })
            form_update_profil_prodi.setText(if(userProfileAwal.dataDiri == null){
                ""
            }else{
                userProfileAwal.dataDiri.programStudi ?: ""
            })
            form_update_profil_keminatan.setText(if(userProfileAwal.dataDiri == null){
                ""
            }else{
                userProfileAwal.dataDiri.keminatan ?: ""
            })
            if(userProfileAwal.dataDiri != null){
                if(userProfileAwal.dataDiri.jenisKelamin == "Pria"){
                    form_update_profil_radio_button_pria.isChecked = true
                }else if(userProfileAwal.dataDiri.jenisKelamin == "Wanita"){
                    form_update_profil_radio_button_wanita.isChecked = true
                }
            }
            form_update_profil_umur.setText(if(userProfileAwal.dataDiri == null){
                ""
            }else{
                userProfileAwal.dataDiri.umur.toString().ifEmpty {
                    ""
                }
            })
            form_update_profil_kota_asal.setText(if(userProfileAwal.dataDiri == null){
                ""
            }else{
                userProfileAwal.dataDiri.asalKota ?: ""
            })
            form_update_profil_kepribadian.setText(if(userProfileAwal.dataDiri == null){
                ""
            }else{
                userProfileAwal.dataDiri.kepribadian ?: ""
            })
        }
    }

    /**
     * Fungsi untuk menampilkan dialog ketika profil berhasil diupdate
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun showCustomDialog() {
        val dialogView = layoutInflater.inflate(R.layout.custom_notification_dialog, null)
        val customDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()
        dialogView.custom_notification_dialog_message.text = "Profile kamu berhasil diperbarui"
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customDialog.window!!.attributes.gravity = Gravity.TOP
        customDialog.window!!.attributes.verticalMargin = 0.1F
        customDialog.show()
    }
}