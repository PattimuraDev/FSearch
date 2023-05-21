package repo.pattimuradev.fsearch.view.fragment.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.fragment_view_pager_data_diri.*
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class ViewPagerDataDiriFragment : Fragment() {

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_pager_data_diri, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        userViewModel.currentUserProfile.observe(viewLifecycleOwner){ userProfile ->
            viewpager_profile_angkatan.text = if(userProfile.dataDiri == null){
                ": -"
            }else{
                ": " + userProfile.dataDiri.tahunAngkatan.toString()
            }

            viewpager_profile_asal_prodi.text = if(userProfile.dataDiri == null){
                ": -"
            }else{
                ": " + userProfile.dataDiri.programStudi
            }

            viewpager_profile_jurusan.text = if(userProfile.dataDiri == null){
                ": -"
            }else{
                ": " + userProfile.dataDiri.jurusan
            }

            viewpager_profile_asal_universitas.text = if(userProfile.dataDiri == null){
                ": -"
            }else{
                ": " + userProfile.dataDiri.asalUniversitas
            }

            viewpager_profile_lokasi.text = if(userProfile.dataDiri == null){
                ""
            }else{
                " " + userProfile.dataDiri.asalKota
            }

            viewpager_profile_jenis_kelamin.text = if(userProfile.dataDiri == null){
                ""
            }else{
                " " + userProfile.dataDiri.jenisKelamin
            }

            viewpager_profile_umur.text = if(userProfile.dataDiri == null){
                ""
            }else{
                " " + userProfile.dataDiri.umur.toString()
            }

            viewpager_profile_keminatan.text = if(userProfile.dataDiri == null){
                ": -"
            }else{
                ": " + userProfile.dataDiri.keminatan
            }
        }
    }
}