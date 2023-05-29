package repo.pattimuradev.fsearch.view.fragment.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_view_pager_posting_pengguna_lain.*
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.CustomObserver.observeOnce
import repo.pattimuradev.fsearch.model.Pengumuman
import repo.pattimuradev.fsearch.view.adapter.PengumumanAdapter
import repo.pattimuradev.fsearch.viewmodel.PengumumanViewModel
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class ViewPagerPostingPenggunaLainFragment : Fragment() {

    private lateinit var pengumumanAdapter: PengumumanAdapter
    private val userViewModel: UserViewModel by viewModels()
    private val pengumumanViewModel: PengumumanViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_view_pager_posting_pengguna_lain,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPostPenggunaLainAdapter()
    }

    /**
     * Fungsi untuk menginisiasi daftar postingan pengguna lain
     * pada halaman profil pengguna lain tab post
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun initPostPenggunaLainAdapter() {
        pengumumanAdapter = PengumumanAdapter {
            // edit
        }

        viewpager_detail_pengguna_lain_rv_postingan_pengguna.layoutManager = LinearLayoutManager(requireContext())
        viewpager_detail_pengguna_lain_rv_postingan_pengguna.adapter = pengumumanAdapter

        userViewModel.spesificUserById.observe(viewLifecycleOwner){ userProfile ->
            pengumumanViewModel.listPengumuman.observeOnce(viewLifecycleOwner){ listPengumuman ->
                val listPostPengguna = mutableListOf<Pengumuman>()
                for(i in listPengumuman){
                    if(i.idPengirim == userProfile.id){
                        listPostPengguna += i
                    }
                }
                pengumumanAdapter.setListPengumuman(listPostPengguna)
                pengumumanAdapter.notifyDataSetChanged()
            }
        }
    }
}