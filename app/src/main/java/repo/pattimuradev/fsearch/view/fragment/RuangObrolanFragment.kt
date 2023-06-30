package repo.pattimuradev.fsearch.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_ruang_obrolan.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.DateAndTimeHandler
import repo.pattimuradev.fsearch.model.Chat
import repo.pattimuradev.fsearch.model.ChatRoom
import repo.pattimuradev.fsearch.view.adapter.RuangObrolanChatAdapter
import repo.pattimuradev.fsearch.viewmodel.ChatViewModel
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class RuangObrolanFragment : Fragment() {

    private lateinit var ruangObrolanChatAdapter : RuangObrolanChatAdapter
    private val chatViewModel: ChatViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ruang_obrolan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        ruang_obrolan_button_back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_ruangObrolanFragment_to_chatFragment)
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_ruangObrolanFragment_to_chatFragment)
            }
        })
        ruang_obrolan_button_send.setOnClickListener {
            if(ruang_obrolan_input_msg.text.toString().trim().isNotEmpty()){
                handleSendMessage()
            }
        }
    }

    /**
     * Fungsi untuk menghandle button send message
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun handleSendMessage() {
        val chatRoomData = arguments?.getParcelable("chat_room_data") as ChatRoom?
        val posisiUser = arguments?.getInt("chat_posisi_user")
        val message = ruang_obrolan_input_msg.text.toString()

        userViewModel.currentUserProfile.observe(viewLifecycleOwner){ currentUser ->
            val idUserReceiver = if(posisiUser == 0){
                chatRoomData!!.personInChat[1]
            }else{
                chatRoomData!!.personInChat[0]
            }
            val urlFotoReceiver = if(posisiUser == 0){
                chatRoomData.fotoUrlPersonTwo
            }else{
                chatRoomData.fotoUrlPersonOne
            }
            val namaReceiver = if(posisiUser == 0){
                chatRoomData.namaPersonTwo
            }else{
                chatRoomData.namaPersonTwo
            }

            val myChat = Chat(
                DateAndTimeHandler.currentDate().time,
                currentUser.id!!,
                idUserReceiver,
                message
            )
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                chatViewModel.sendMessage(
                    chatRoomData.chatRoomId,
                    myChat,
                    currentUser.urlFoto,
                    urlFotoReceiver,
                    currentUser.nama,
                    namaReceiver)
            }
        }
        ruang_obrolan_input_msg.text!!.clear()
    }

    /**
     * Fungsi untuk menginisiasi data utama yang ditampilkan dalam ruang obrolan
     * termasuk semua riwayat chat kedua pengguna
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun initView() {
        ruangObrolanChatAdapter = RuangObrolanChatAdapter()
        ruang_obrolan_rv_chat.layoutManager = LinearLayoutManager(requireContext())
        ruang_obrolan_rv_chat.adapter = ruangObrolanChatAdapter

        val chatRoomData = arguments?.getParcelable("chat_room_data") as ChatRoom?
        val posisiUser = arguments?.getInt("chat_posisi_user")
        val idRoomChat = chatRoomData!!.chatRoomId
        if(posisiUser == 0){
            ruang_obrolan_nama.text = chatRoomData.namaPersonTwo
            Glide.with(ruang_obrolan_foto_user.context)
                .load(chatRoomData.fotoUrlPersonTwo)
                .error(R.drawable.standard_user_photo)
                .into(ruang_obrolan_foto_user)
        }else{
            ruang_obrolan_nama.text = chatRoomData.namaPersonOne
            Glide.with(ruang_obrolan_foto_user.context)
                .load(chatRoomData.fotoUrlPersonOne)
                .error(R.drawable.standard_user_photo)
                .into(ruang_obrolan_foto_user)
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
            chatViewModel.getAllMessageFromSpesificChatRoom(idRoomChat!!)
        }

        userViewModel.currentUserProfile.observe(viewLifecycleOwner){ currentUser ->
            ruangObrolanChatAdapter.setCurrentUserId(currentUser.id!!)
            chatViewModel.allMessageFromAChatRoom.observe(viewLifecycleOwner){
                ruangObrolanChatAdapter.setListChat(it)
                ruangObrolanChatAdapter.notifyDataSetChanged()
            }
        }
//        ruang_obrolan_rv_chat.smoothScrollToPosition(ruangObrolanChatAdapter.itemCount - 1)
//        ruangObrolanChatAdapter.notifyDataSetChanged()
    }
}