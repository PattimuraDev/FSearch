package repo.pattimuradev.fsearch.view.fragment.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_view_pager_daftar_pengguna_favorit.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.CustomObserver.observeOnce
import repo.pattimuradev.fsearch.misc.DaftarPenggunaClickListener
import repo.pattimuradev.fsearch.model.UserProfile
import repo.pattimuradev.fsearch.view.adapter.DaftarPenggunaAdapter
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class ViewPagerDaftarPenggunaFavorit : Fragment(), DaftarPenggunaClickListener {

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var daftarPenggunaAdapter: DaftarPenggunaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_view_pager_daftar_pengguna_favorit,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    /**
     * Fungsi untuk menginisiasi daftar pengguna favorit di halaman favorit
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun initRecyclerView() {
        daftarPenggunaAdapter = DaftarPenggunaAdapter(this)
        viewpager_like_rv_daftar_pengguna_favorit.layoutManager = LinearLayoutManager(requireContext())
        viewpager_like_rv_daftar_pengguna_favorit.adapter = daftarPenggunaAdapter

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
            userViewModel.getAllFavoritedUser()
        }

        userViewModel.currentUser.observe(viewLifecycleOwner){ currentUser ->
            userViewModel.allUserFavorited.observe(viewLifecycleOwner){ listFavoritedUser ->
                daftarPenggunaAdapter.setUserId(currentUser.uid)
                daftarPenggunaAdapter.setListPengguna(listFavoritedUser)
                daftarPenggunaAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun clickOnLikeButton(item: UserProfile, position: Int) {
        // opsional edit
    }

    override fun clickOnDaftarPenggunaBody(item: UserProfile, position: Int) {
        // opsional edit
    }

}