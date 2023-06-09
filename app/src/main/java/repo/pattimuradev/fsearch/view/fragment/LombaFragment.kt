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
import kotlinx.android.synthetic.main.fragment_lomba.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.LombaClickListener
import repo.pattimuradev.fsearch.model.Lomba
import repo.pattimuradev.fsearch.view.adapter.LombaAdapter
import repo.pattimuradev.fsearch.viewmodel.LombaViewModel
import repo.pattimuradev.fsearch.viewmodel.NotifikasiViewModel
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class LombaFragment : Fragment(), LombaClickListener {
    private lateinit var lombaAdapter: LombaAdapter
    private val lombaViewModel: LombaViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val notifikasiViewModel: NotifikasiViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lomba, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        lomba_button_tambah_lomba.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_lombaFragment_to_formTambahLombaFragment)
        }
    }

    /**
     * Fungsi untuk menginisiasi action bar
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun initView() {
        lomba_action_bar.inflateMenu(R.menu.custom_fragment_toolbar_menu)
        lomba_action_bar.setOnMenuItemClickListener { menuItem ->
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
//                            lombaAdapter.filter.filter(p0)
//                            lombaAdapter.notifyDataSetChanged()
//                            return true
//                        }
//                    })
                    true
                }
                R.id.go_to_favorit -> {
                    Navigation.findNavController(requireView()).navigate(R.id.action_lombaFragment_to_likeFragment)
                    true
                }
                R.id.go_to_notification -> {
                    Navigation.findNavController(requireView()).navigate(R.id.action_lombaFragment_to_notifikasiFragment)
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
        val toolbar = lomba_action_bar
        BadgeUtils.attachBadgeDrawable(badgeDrawable, toolbar, R.id.go_to_notification)

        initLombaAdapter()
    }

    /**
     * Fungsi untuk menginisiasi daftar lomba beserta adapternya
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun initLombaAdapter() {
        lombaAdapter = LombaAdapter(this@LombaFragment)
        lomba_rv_lomba.layoutManager = LinearLayoutManager(requireContext())
        lomba_rv_lomba.adapter = lombaAdapter

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
            lombaViewModel.getAllLomba()
        }

        userViewModel.currentUserProfile.observe(viewLifecycleOwner){
            lombaAdapter.setCurrentUserIdInAdapter(it.id!!)
            lombaAdapter.notifyDataSetChanged()
        }

        lombaViewModel.listLomba.observe(viewLifecycleOwner){
            lombaAdapter.setListLomba(it)
            lombaAdapter.notifyDataSetChanged()
        }
    }

    override fun clickOnLikeButton(item: Lomba, position: Int) {
        userViewModel.currentUser.observe(viewLifecycleOwner){ firebaseUser ->
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                lombaViewModel.addUserLike(firebaseUser.uid, item.idLomba!!)
            }
        }
    }

    override fun clickOnDaftarLombaBody(item: Lomba, position: Int) {
        val bundle = Bundle()
        bundle.putParcelable("lomba", item)
        Navigation.findNavController(requireView()).navigate(R.id.action_lombaFragment_to_detailLombaFragment, bundle)
    }



}