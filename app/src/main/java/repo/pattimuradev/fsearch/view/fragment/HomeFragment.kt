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
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.view.adapter.PengumumanAdapter
import repo.pattimuradev.fsearch.viewmodel.PengumumanViewModel

class HomeFragment : Fragment() {

    private lateinit var pengumumanAdapter: PengumumanAdapter
    private val pengumumanViewModel: PengumumanViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        home_button_tambah_pengumuman.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_formBuatPengumumanFragment)
        }
    }

    /**
     * Fungsi untuk menginisiasi action bar
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun initView() {
        initPengumumanAdapter()
        home_action_bar.inflateMenu(R.menu.custom_fragment_toolbar_menu)
        home_action_bar.setOnMenuItemClickListener { menuItem ->
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
//                            pengumumanAdapter.filter.filter(p0)
//                            pengumumanAdapter.notifyDataSetChanged()
//                            return true
//                        }
//                    })
                    true
                }
                R.id.go_to_notification -> {
                    Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_notifikasiFragment)
                    true
                }
                R.id.go_to_favorit -> {
                    Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_likeFragment)
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Fungsi untuk menginisiasi daftar pengumuman beserta adapternya
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun initPengumumanAdapter(){
        pengumumanAdapter = PengumumanAdapter { pengumuman ->
            val bundle = Bundle()
            bundle.putParcelable("pengumuman", pengumuman)
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_formPengajuanDiriFragment, bundle)
        }

        home_rv_pengumuman.layoutManager = LinearLayoutManager(requireContext())
        home_rv_pengumuman.adapter = pengumumanAdapter

        lifecycleScope.launch(Dispatchers.IO){
            pengumumanViewModel.getAllPengumuman()
        }

        pengumumanViewModel.listPengumuman.observe(viewLifecycleOwner){
            pengumumanAdapter.setListPengumuman(it)
            pengumumanAdapter.notifyDataSetChanged()
        }

    }
}