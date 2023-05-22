package repo.pattimuradev.fsearch.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_like.*
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.view.adapter.DetailPenggunaLainViewPagerAdapter
import repo.pattimuradev.fsearch.view.adapter.LikeViewPagerAdapter

class LikeFragment : Fragment() {

    private lateinit var likeViewPagerAdapter: LikeViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_like, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        like_button_back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_likeFragment_to_homeFragment)
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        likeViewPagerAdapter = LikeViewPagerAdapter(this)
        like_view_pager.adapter = likeViewPagerAdapter

        TabLayoutMediator(like_tab_layout, like_view_pager){ tab, position ->
            tab.text = when(position){
                0 -> "Pengguna"
                else -> "Lomba"
            }
        }.attach()
    }

}