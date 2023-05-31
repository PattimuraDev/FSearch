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
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.view.adapter.ChatRoomAdapter
import repo.pattimuradev.fsearch.viewmodel.ChatViewModel
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class ChatFragment : Fragment() {

    private lateinit var chatRoomAdapter: ChatRoomAdapter
    private val chatViewModel: ChatViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
    }

    /**
     * Fungsi untuk menginisiasi action yang dapat dilakukan di toolbar halaman chat
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun initToolbar() {
        initChatRoomAdapter()
        chat_action_bar.inflateMenu(R.menu.custom_fragment_toolbar_menu_chat)
        chat_action_bar.setOnMenuItemClickListener { menuItem ->
            if(menuItem.itemId == R.id.toolbar_button_add){
                Navigation.findNavController(requireView()).navigate(R.id.action_chatFragment_to_chatDaftarPenggunaFragment)
                true
            }else{
                // edit
                true
            }
        }
    }

    /**
     * Fungsi untuk menginisiasi data ruang obrolan yang akan ditampilkan
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun initChatRoomAdapter() {
        chatRoomAdapter = ChatRoomAdapter { chatRoomData, posisiUser ->
            val bundle = Bundle()
            bundle.putParcelable("chat_room_data", chatRoomData)
            bundle.putInt("chat_posisi_user", posisiUser)
            Navigation.findNavController(requireView()).navigate(R.id.action_chatFragment_to_ruangObrolanFragment, bundle)
        }

        chat_rv_chat_room.layoutManager = LinearLayoutManager(requireContext())
        chat_rv_chat_room.adapter = chatRoomAdapter

        userViewModel.currentUserProfile.observe(viewLifecycleOwner){ userProfile ->
            chatRoomAdapter.setCurrentUserId(userProfile.id!!)
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                chatViewModel.getAllChatRoom(userProfile.id)
            }
        }

        chatViewModel.allChatRoom.observe(viewLifecycleOwner){ listChatRoom ->
            chatRoomAdapter.setListChatRoom(listChatRoom)
            chatRoomAdapter.notifyDataSetChanged()
        }
    }
}