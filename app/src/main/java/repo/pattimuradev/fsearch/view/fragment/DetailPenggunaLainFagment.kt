package repo.pattimuradev.fsearch.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_detail_pengguna_lain_fagment.*
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.view.adapter.DetailPenggunaLainViewPagerAdapter
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class DetailPenggunaLainFagment : Fragment() {

    private lateinit var detailPenggunaLainViewPagerAdapter: DetailPenggunaLainViewPagerAdapter
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_pengguna_lain_fagment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDetailPengguna()
        initViewPagerAdapter()

        detail_pengguna_lain_button_back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_detailPenggunaLainFagment_to_penggunaFragment)
        }
    }

    private fun initDetailPengguna() {
        userViewModel.spesificUserById.observe(viewLifecycleOwner){ userProfile ->
            detail_pengguna_lain_nama_pengguna.text = userProfile.nama
            detail_pengguna_lain_jumlah_like.text = if(userProfile.jumlahLike == null){
                "0"
            }else{
                userProfile.jumlahLike.toString()
            }
            detail_pengguna_lain_jumlah_teman.text = if(userProfile.jumlahTeman == null){
                "0"
            }else{
                userProfile.jumlahTeman.toString()
            }
            detail_pengguna_lain_bio.text = if(userProfile.bio == null){
                ""
            }else{
                userProfile.bio.toString()
            }
            Glide.with(detail_pengguna_lain_foto_profil_user.context)
                .load(userProfile.urlFoto)
                .error(R.drawable.standard_user_photo)
                .into(detail_pengguna_lain_foto_profil_user)
        }
    }

    private fun initViewPagerAdapter() {
        detailPenggunaLainViewPagerAdapter = DetailPenggunaLainViewPagerAdapter(this)
        detail_pengguna_lain_view_pager.adapter = detailPenggunaLainViewPagerAdapter

        TabLayoutMediator(detail_pengguna_lain_tab_layout, detail_pengguna_lain_view_pager){ tab, position ->
            tab.text = when(position){
                0 -> "Data Diri"
                1 -> "Testimoni"
                else -> "Post"
            }
        }.attach()
    }
}