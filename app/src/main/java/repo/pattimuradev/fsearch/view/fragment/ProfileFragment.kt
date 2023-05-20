package repo.pattimuradev.fsearch.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.custom_profile_menu_dialog.view.*
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
                    showCustomDialog()
                    true
                }else -> false
            }
        }
    }


    private fun showCustomDialog() {
        val dialogView = layoutInflater.inflate(R.layout.custom_profile_menu_dialog, null)
        val customDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()
        dialogView.custom_profil_menu_dialog_button_edit_profile.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_formUpdateProfileFragment)
            customDialog.dismiss()
        }
        dialogView.custom_profil_menu_dialog_button_keluar.setOnClickListener {
            // edit nanti
            customDialog.dismiss()
        }
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customDialog.window!!.attributes.gravity = Gravity.TOP
        customDialog.window!!.attributes.verticalMargin = 0.2F
        customDialog.show()
        customDialog.window!!.setLayout(480, 300)
    }

}