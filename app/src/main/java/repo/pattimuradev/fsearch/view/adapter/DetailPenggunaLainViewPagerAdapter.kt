package repo.pattimuradev.fsearch.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import repo.pattimuradev.fsearch.view.fragment.viewpager.*

class DetailPenggunaLainViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val numberOfTabs = 3

    override fun getItemCount(): Int {
        return numberOfTabs
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return ViewPagerDataDiriPenggunaLainFragment()
            1 -> return ViewPagerTestimoniPenggunaLainFragment()
        }
        return ViewPagerPostingPenggunaLainFragment()
    }
}