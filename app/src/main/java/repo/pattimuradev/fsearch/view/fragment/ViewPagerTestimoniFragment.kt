package repo.pattimuradev.fsearch.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_view_pager_testimoni.*
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.view.adapter.TestimoniViewPagerAdapter
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class ViewPagerTestimoniFragment : Fragment() {

    private lateinit var testimoniViewPagerAdapter: TestimoniViewPagerAdapter
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_pager_testimoni, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTestimoniAdapter()
    }

    private fun initTestimoniAdapter() {
        testimoniViewPagerAdapter = TestimoniViewPagerAdapter()
        viewpager_profile_rv_testimoni.layoutManager = LinearLayoutManager(requireContext())
        viewpager_profile_rv_testimoni.adapter = testimoniViewPagerAdapter

        userViewModel.currentUserProfile.observe(viewLifecycleOwner){ userProfile ->
            if(userProfile.testimoni != null){
                testimoniViewPagerAdapter.setListTestimoni(userProfile.testimoni)
                testimoniViewPagerAdapter.notifyDataSetChanged()
            }
        }
    }
}