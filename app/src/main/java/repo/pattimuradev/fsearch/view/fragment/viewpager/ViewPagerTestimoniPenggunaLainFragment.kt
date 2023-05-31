package repo.pattimuradev.fsearch.view.fragment.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_view_pager_testimoni_pengguna_lain.*
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.view.adapter.TestimoniAdapter
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class ViewPagerTestimoniPenggunaLainFragment : Fragment() {

    private lateinit var testimoniAdapter: TestimoniAdapter
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_view_pager_testimoni_pengguna_lain,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTestimoniAdapter()
    }

    /**
     * Fungsi untuk menginisiasi daftar testimoni pada profile pengguna lain
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun initTestimoniAdapter() {
        testimoniAdapter = TestimoniAdapter()
        viewpager_detail_pengguna_lain_rv_testimoni.layoutManager = LinearLayoutManager(requireContext())
        viewpager_detail_pengguna_lain_rv_testimoni.adapter = testimoniAdapter

        userViewModel.spesificUserById.observe(viewLifecycleOwner){ userProfile ->
            testimoniAdapter.setListTestimoni(userProfile.testimoni)
            testimoniAdapter.notifyDataSetChanged()
        }
    }
}