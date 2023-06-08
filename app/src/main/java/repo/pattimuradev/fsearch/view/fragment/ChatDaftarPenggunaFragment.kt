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
import kotlinx.android.synthetic.main.fragment_chat_daftar_pengguna.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.DaftarPenggunaClickListener
import repo.pattimuradev.fsearch.misc.DateAndTimeHandler
import repo.pattimuradev.fsearch.model.UserProfile
import repo.pattimuradev.fsearch.view.adapter.DaftarPenggunaAdapter
import repo.pattimuradev.fsearch.viewmodel.ChatViewModel
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class ChatDaftarPenggunaFragment : Fragment(), DaftarPenggunaClickListener {

    private lateinit var daftarPenggunaAdapter: DaftarPenggunaAdapter
    private val userViewModel: UserViewModel by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_daftar_pengguna, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chat_daftar_pengguna_button_back.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_chatDaftarPenggunaFragment_to_chatFragment)
        }
        initAdapter()
    }

    /**
     * Fungsi untuk menginisiasi data pengguna yang tampil di halaman chat
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun initAdapter() {
        daftarPenggunaAdapter = DaftarPenggunaAdapter(this)
        rv_daftar_pengguna_halaman_chat.layoutManager = LinearLayoutManager(requireContext())
        rv_daftar_pengguna_halaman_chat.adapter = daftarPenggunaAdapter

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
            userViewModel.getAllUser()
        }

        userViewModel.currentUserProfile.observe(viewLifecycleOwner){ currentUserProfile ->
            daftarPenggunaAdapter.setUserId(currentUserProfile.id!!)
            daftarPenggunaAdapter.notifyDataSetChanged()
            userViewModel.allUser.observe(viewLifecycleOwner){ listUser ->
                val result = mutableListOf<UserProfile>()
                for(i in listUser){
                    if(i.id!! != currentUserProfile.id){
                        result += i
                    }
                }
                daftarPenggunaAdapter.setListPengguna(result)
                daftarPenggunaAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun clickOnLikeButton(item: UserProfile, position: Int) {
        // nothing to do
    }

    override fun clickOnDaftarPenggunaBody(item: UserProfile, position: Int) {
        userViewModel.currentUserProfile.observe(viewLifecycleOwner){ currentUsserProfile ->
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                chatViewModel.addEmptyChatRoom(
                    currentUsserProfile.id!!,
                    item.id!!,
                    currentUsserProfile.urlFoto,
                    item.urlFoto,
                    currentUsserProfile.nama,
                    item.nama,
                    DateAndTimeHandler.currentDate().time
                )
            }
            chatViewModel.newEmptyChatRoom.observe(viewLifecycleOwner){ chatRoom ->
                val posisiCurrentUser = chatRoom.personInChat.indexOf(currentUsserProfile.id)
                val bundle = Bundle()
                bundle.putParcelable("chat_room_data", chatRoom)
                bundle.putInt("chat_posisi_user", posisiCurrentUser)
                Navigation.findNavController(requireView()).navigate(R.id.action_chatDaftarPenggunaFragment_to_ruangObrolanFragment, bundle)
            }
        }
    }
}