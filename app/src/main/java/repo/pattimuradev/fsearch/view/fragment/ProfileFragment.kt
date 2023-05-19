package repo.pattimuradev.fsearch.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_profile.*
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.view.adapter.ProfileViewPagerAdapter
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class ProfileFragment : Fragment() {

    private lateinit var profileViewPagerAdapter: ProfileViewPagerAdapter
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initProfileData()
        initViewPagerAdapter()
    }

    private fun initViewPagerAdapter() {
        profileViewPagerAdapter = ProfileViewPagerAdapter(this)
        profile_view_pager.adapter = profileViewPagerAdapter

        TabLayoutMediator(profile_tab_layout, profile_view_pager){ tab, position ->
            tab.text = when(position){
                0 -> "Data Diri"
                1 -> "Testimoni"
                else -> "Post"
            }
        }.attach()
    }

    private fun initProfileData() {
        userViewModel.currentUserProfile.observe(viewLifecycleOwner){ userProfile ->
            profil_nama_pengguna.text = userProfile.nama
            profil_jumlah_like.text = if(userProfile.jumlahLike == null){
                "0"
            }else{
                userProfile.jumlahLike.toString()
            }
            profil_jumlah_teman.text = if(userProfile.jumlahTeman == null){
                "0"
            }else{
                userProfile.jumlahTeman.toString()
            }
            Glide.with(profil_foto_profil_user.context)
                .load(userProfile.urlFoto)
                .error(R.drawable.standard_user_photo)
                .into(profil_foto_profil_user)
        }

        profile_action_bar.inflateMenu(R.menu.custom_fragment_toolbar_menu_profile)
        profile_action_bar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.more -> {
                    // handle

                    true
                }else -> false
            }
        }
    }


}