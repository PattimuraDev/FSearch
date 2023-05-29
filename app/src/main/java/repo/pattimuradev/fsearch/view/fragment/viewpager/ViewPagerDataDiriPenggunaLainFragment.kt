package repo.pattimuradev.fsearch.view.fragment.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.fragment_view_pager_data_diri_pengguna_lain.*
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class ViewPagerDataDiriPenggunaLainFragment : Fragment() {

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_view_pager_data_diri_pengguna_lain,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    /**
     * Fungsi untuk menginisiasi data diri pada profil pengguna lain
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun initView() {
        userViewModel.spesificUserById.observe(viewLifecycleOwner){ userProfile ->
            viewpager_detail_pengguna_lain_angkatan.text = if(userProfile.dataDiri == null){
                ": -"
            }else{
                ": " + userProfile.dataDiri.tahunAngkatan.toString()
            }

            viewpager_detail_pengguna_lain_asal_prodi.text = if(userProfile.dataDiri == null){
                ": -"
            }else{
                ": " + userProfile.dataDiri.programStudi
            }

            viewpager_detail_pengguna_lain_jurusan.text = if(userProfile.dataDiri == null){
                ": -"
            }else{
                ": " + userProfile.dataDiri.jurusan
            }

            viewpager_detail_pengguna_lain_asal_universitas.text = if(userProfile.dataDiri == null){
                ": -"
            }else{
                ": " + userProfile.dataDiri.asalUniversitas
            }

            viewpager_detail_pengguna_lain_lokasi.text = if(userProfile.dataDiri == null){
                ""
            }else{
                " " + userProfile.dataDiri.asalKota
            }

            viewpager_detail_pengguna_lain_jenis_kelamin.text = if(userProfile.dataDiri == null){
                ""
            }else{
                " " + userProfile.dataDiri.jenisKelamin
            }

            viewpager_detail_pengguna_lain_umur.text = if(userProfile.dataDiri == null){
                ""
            }else{
                " " + userProfile.dataDiri.umur.toString()
            }

            viewpager_detail_pengguna_lain_keminatan.text = if(userProfile.dataDiri == null){
                ": -"
            }else{
                ": " + userProfile.dataDiri.keminatan
            }
        }
    }

}