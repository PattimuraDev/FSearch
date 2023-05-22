package repo.pattimuradev.fsearch.view.fragment.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_view_pager_lomba_favorit.*
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.CustomObserver.observeOnce
import repo.pattimuradev.fsearch.misc.LombaClickListener
import repo.pattimuradev.fsearch.model.Lomba
import repo.pattimuradev.fsearch.view.adapter.LombaAdapter
import repo.pattimuradev.fsearch.viewmodel.LombaViewModel
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class ViewPagerLombaFavoritFragment : Fragment(), LombaClickListener {

    private lateinit var lombaAdapter: LombaAdapter
    private val lombaViewModel: LombaViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_pager_lomba_favorit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        lombaAdapter = LombaAdapter(this)
        viewpager_like_rv_lomba_favorit.layoutManager = LinearLayoutManager(requireContext())
        viewpager_like_rv_lomba_favorit.adapter = lombaAdapter
        userViewModel.currentUser.observe(viewLifecycleOwner){ currentUser ->
            lombaViewModel.listLomba.observe(viewLifecycleOwner){ listLomba ->
                lombaAdapter.setCurrentUserIdInAdapter(currentUser.uid)
                val listLombaFavorit = mutableListOf<Lomba>()
                for(i in listLomba){
                    if(i.likedByUserId != null){
                        if(i.likedByUserId.contains(currentUser.uid)){
                            listLombaFavorit.add(i)
                        }
                    }
                }
                lombaAdapter.setCurrentUserIdInAdapter(currentUser.uid)
                lombaAdapter.setListLomba(listLombaFavorit)
                lombaAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun clickOnLikeButton(item: Lomba, position: Int) {
        // override nanti
    }

    override fun clickOnDaftarLombaBody(item: Lomba, position: Int) {
        // edit nanti
    }
}