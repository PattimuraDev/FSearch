package repo.pattimuradev.fsearch.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_form_update_profile.*
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.CustomObserver.observeOnce
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class FormUpdateProfileFragment : Fragment() {

    private val userViewModel: UserViewModel by viewModels()

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
                userProfileAwal.dataDiri.tahunAngkatan.toString() ?: ""
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
                userProfileAwal.dataDiri.umur.toString() ?: ""
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
}