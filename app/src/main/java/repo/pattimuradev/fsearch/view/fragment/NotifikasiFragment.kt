package repo.pattimuradev.fsearch.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_notifikasi.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.view.adapter.NotifikasiAdapter
import repo.pattimuradev.fsearch.viewmodel.NotifikasiViewModel
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class NotifikasiFragment : Fragment() {

    private lateinit var notifikasiAdapter: NotifikasiAdapter
    private val notifikasiViewModel: NotifikasiViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notifikasi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNotifikasiAdapter()
        notifikasi_button_back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_notifikasiFragment_to_homeFragment)
        }
    }

    private fun initNotifikasiAdapter() {
        notifikasiAdapter = NotifikasiAdapter{ notifikasi ->
            if(notifikasi.jenisNotifikasi == "permintaan_pertemanan"){
                // edit
            }

            //edit untuk elsenya
        }
        notifikasi_rv_notifikasi.layoutManager = LinearLayoutManager(requireContext())
        notifikasi_rv_notifikasi.adapter = notifikasiAdapter

        userViewModel.currentUser.observe(viewLifecycleOwner){
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                notifikasiViewModel.getAllNotifikasi(it.uid)
            }
        }

        notifikasiViewModel.listNotifikasi.observe(viewLifecycleOwner){
            notifikasiAdapter.setListNotifikasi(it)
            notifikasiAdapter.notifyDataSetChanged()
        }
    }

}