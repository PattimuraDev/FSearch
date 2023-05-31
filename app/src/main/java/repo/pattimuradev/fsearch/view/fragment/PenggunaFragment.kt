package repo.pattimuradev.fsearch.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import kotlinx.android.synthetic.main.fragment_pengguna.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.DaftarPenggunaClickListener
import repo.pattimuradev.fsearch.model.UserProfile
import repo.pattimuradev.fsearch.view.adapter.DaftarPenggunaAdapter
import repo.pattimuradev.fsearch.viewmodel.NotifikasiViewModel
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class PenggunaFragment : Fragment(), DaftarPenggunaClickListener{

    private lateinit var daftarPenggunaAdapter: DaftarPenggunaAdapter
    private val userViewModel: UserViewModel by viewModels()
    private val notifikasiViewModel: NotifikasiViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pengguna, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    /**
     * Fungsi untuk menginisiasi action bar pada halaman pengguna
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun initView() {
        pengguna_action_bar.inflateMenu(R.menu.custom_fragment_toolbar_menu)
        pengguna_action_bar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.search_view -> {
//                    val searchView: SearchView = menuItem.actionView as SearchView
//                    searchView.maxWidth = Int.MAX_VALUE
//                    searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
//                        override fun onQueryTextSubmit(p0: String?): Boolean {
//                            return true
//                        }
//
//                        override fun onQueryTextChange(p0: String?): Boolean {
//                            // nanti
//
//                            daftarPenggunaAdapter.filter.filter(p0)
//                            daftarPenggunaAdapter.notifyDataSetChanged()
//                            return true
//                        }
//                    })
                    true
                }
                R.id.go_to_favorit -> {
                    Navigation.findNavController(requireView()).navigate(R.id.action_penggunaFragment_to_likeFragment)
                    true
                }
                R.id.go_to_notification -> {
                    Navigation.findNavController(requireView()).navigate(R.id.action_penggunaFragment_to_notifikasiFragment)
                    true
                }
                else -> false
            }
        }

        val badgeDrawable = BadgeDrawable.create(requireContext())
        userViewModel.currentUser.observe(viewLifecycleOwner){ currentUser ->
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                notifikasiViewModel.getJumlahNotifikasiBelumDirespon(currentUser.uid)
            }
            notifikasiViewModel.jumlahNotifikasiBelumDirespon.observe(viewLifecycleOwner){ jumlah ->
                if(jumlah == 0){
                    badgeDrawable.isVisible = false
                }else{
                    badgeDrawable.isVisible = true
                    badgeDrawable.backgroundColor = resources.getColor(R.color.secondary_one, null)
                    badgeDrawable.number = jumlah
                }
            }
        }
        val toolbar = pengguna_action_bar
        BadgeUtils.attachBadgeDrawable(badgeDrawable, toolbar, R.id.go_to_notification)

        initAdapter()
    }

    /**
     * Fungsi untuk menginisiasi daftar pengguna
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun initAdapter() {
        daftarPenggunaAdapter = DaftarPenggunaAdapter(this@PenggunaFragment)
        pengguna_rv_daftar_pengguna.layoutManager = LinearLayoutManager(requireContext())
        pengguna_rv_daftar_pengguna.adapter = daftarPenggunaAdapter

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
            userViewModel.getAllUser()
        }

        userViewModel.currentUserProfile.observe(viewLifecycleOwner){ currentUserProfile ->
            daftarPenggunaAdapter.setUserId(currentUserProfile.id!!)
            daftarPenggunaAdapter.notifyDataSetChanged()
            userViewModel.allUser.observe(viewLifecycleOwner){ listUser ->
                val result = mutableListOf<UserProfile>()
                for(i in listUser){
                    if(i.id!! != currentUserProfile.id){
                        result += i
                    }
                }
                daftarPenggunaAdapter.setListPengguna(result)
                daftarPenggunaAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun clickOnLikeButton(item: UserProfile, position: Int) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
            userViewModel.addUserLike(item.id!!)
        }
    }

    override fun clickOnDaftarPenggunaBody(item: UserProfile, position: Int) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
            userViewModel.getSpesificUserById(item.id!!)
        }

        userViewModel.spesificUserById.observe(viewLifecycleOwner){ spesificUser ->
            if(spesificUser != null){
                Navigation.findNavController(requireView()).navigate(R.id.action_penggunaFragment_to_detailPenggunaLainFagment)
            }
        }
    }
}