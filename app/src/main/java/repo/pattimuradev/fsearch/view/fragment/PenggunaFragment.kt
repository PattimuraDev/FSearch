package repo.pattimuradev.fsearch.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_pengguna.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.DaftarPenggunaClickListener
import repo.pattimuradev.fsearch.model.UserProfile
import repo.pattimuradev.fsearch.view.adapter.DaftarPenggunaAdapter
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class PenggunaFragment : Fragment(), DaftarPenggunaClickListener{

    private lateinit var daftarPenggunaAdapter: DaftarPenggunaAdapter
    private val userViewModel: UserViewModel by viewModels()

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

    private fun initView() {
        initAdapter()

        pengguna_action_bar.inflateMenu(R.menu.custom_fragment_toolbar_menu)
        pengguna_action_bar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.search_view -> {
                    val searchView: SearchView = menuItem.actionView as SearchView
                    searchView.maxWidth = Int.MAX_VALUE
                    searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                        override fun onQueryTextSubmit(p0: String?): Boolean {
                            return true
                        }

                        override fun onQueryTextChange(p0: String?): Boolean {
                            // nanti

//                            daftarPenggunaAdapter.filter.filter(p0)
//                            daftarPenggunaAdapter.notifyDataSetChanged()
                            return true
                        }
                    })
                    true
                }
                R.id.go_to_favorit -> {
                    //
                    true
                }
                R.id.go_to_notification -> {
                    // go to notification
                    true
                }
                else -> false
            }
        }
    }

    private fun initAdapter() {
        daftarPenggunaAdapter = DaftarPenggunaAdapter(this@PenggunaFragment)
        pengguna_rv_daftar_pengguna.layoutManager = LinearLayoutManager(requireContext())
        pengguna_rv_daftar_pengguna.adapter = daftarPenggunaAdapter

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
            userViewModel.getAllUser()
        }

        userViewModel.allUser.observe(viewLifecycleOwner){
            daftarPenggunaAdapter.setListPengguna(it)
            daftarPenggunaAdapter.notifyDataSetChanged()
        }
    }

    override fun clickOnLikeButton(item: UserProfile, position: Int) {
        // edit
    }

    override fun clickOnDaftarPenggunaBody(item: UserProfile, position: Int) {
        // edit
    }
}