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
        initDataAwal()
        checkField()
        form_update_profil_foto_user.setOnClickListener {
            handleTakeImage()
        }
        handleUpdateProfil()
    }

    private fun handleUpdateProfil() {
        var statusBersediaMenerimaAjakan: Boolean? = null
        var jenisKelamin: String? = null

        form_update_profil_radio_group_jenis_kelamin.setOnCheckedChangeListener { _, checkedId ->
            form_update_profil_button_simpan.isEnabled = true
            jenisKelamin = if(checkedId == R.id.form_update_profil_radio_button_pria){
                "Pria"
            }else{
                "Wanita"
            }
        }

        form_update_profil_button_status_bersedia.setOnCheckedChangeListener { _, isChecked ->
            statusBersediaMenerimaAjakan = isChecked
            form_update_profil_button_simpan.isEnabled = true
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
                            id = null,
                            nama = nama,
                            email = "",
                            urlFoto = fotoProfilUrl,
                            jumlahTeman = null,
                            jumlahLike = null,
                            bio = bio,
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
                            ),
                            testimoni = null,
                            ratingKeseluruhan = null,
                            statusBersediaMenerimaAjakan = statusBersediaMenerimaAjakan,
                            likedByUserId = null
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

    private fun checkField() {
        form_update_profil_button_simpan.isEnabled = false
        val fields = listOf(
            form_update_profil_bio,
            form_update_profil_nama,
            form_update_profil_universitas,
            form_update_profil_tahun_angkatan,
            form_update_profil_fakultas,
            form_update_profil_jurusan,
            form_update_profil_prodi,
            form_update_profil_keminatan,
            form_update_profil_umur,
            form_update_profil_kota_asal,
            form_update_profil_kepribadian
        )
        for(field in fields){
            field.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    form_update_profil_button_simpan.isEnabled = false
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    form_update_profil_button_simpan.isEnabled = true
                }

                override fun afterTextChanged(p0: Editable?) {
                    form_update_profil_button_simpan.isEnabled = true
                }

            })
        }
        // edit terakhir
        if(form_update_profil_button_simpan.isEnabled){
            form_update_profil_button_simpan.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_button_enabled_layout, null)
        }else{
            form_update_profil_button_simpan.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_button_not_enabled_layout, null)
        }
    }

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
            form_update_profil_button_status_bersedia.isChecked = userProfileAwal.statusBersediaMenerimaAjakan ?: false
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

    private fun showCustomDialog() {
        val dialogView = layoutInflater.inflate(R.layout.custom_notification_dialog, null)
        val customDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()
        dialogView.custom_notification_dialog_message.text = "Profile kamu berhasil diperbarui"
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customDialog.window!!.attributes.gravity = Gravity.TOP
        customDialog.window!!.attributes.verticalMargin = 0.2F
        customDialog.show()
        customDialog.window!!.setLayout(480, 190)
    }
}