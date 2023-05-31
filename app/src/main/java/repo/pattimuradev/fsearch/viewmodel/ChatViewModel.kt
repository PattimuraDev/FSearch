package repo.pattimuradev.fsearch.viewmodel

import androidx.lifecycle.ViewModel
import repo.pattimuradev.fsearch.model.Chat
import repo.pattimuradev.fsearch.repository.ChatRepository

class ChatViewModel: ViewModel() {
    private val chatRepository = ChatRepository.getInstance()
    val allChatRoom = chatRepository.allChatRoomLiveData
    val allMessageFromAChatRoom = chatRepository.allMessageFromSpesificChatRoomLiveData
    val newEmptyChatRoom = chatRepository.newEmptyChatRoomLiveData
    val jumlahPesanBelumDibacaUser = chatRepository.jumlahPesanBelumDibacaUserLiveData

    suspend fun getAllChatRoom(idCurrentUser: String) = chatRepository.getAllChatRoom(idCurrentUser)
    suspend fun sendMessage(
        chatRoomId: String?,
        chat: Chat,
        fotoUrlPersonOne: String?,
        fotoUrlPersonTwo: String?,
        namaPersonOne: String?,
        namaPersonTwo: String?
    ) = chatRepository.sendChatMessage(chatRoomId, chat, fotoUrlPersonOne, fotoUrlPersonTwo, namaPersonOne, namaPersonTwo)
    suspend fun getAllMessageFromSpesificChatRoom(idChatRoom: String) = chatRepository.getAllMessageFromAChatRoom(idChatRoom)
    suspend fun addEmptyChatRoom(
        idPersonOne: String,
        idPersonTwo: String,
        fotoUrlPersonOne: String?,
        fotoUrlPersonTwo: String?,
        namaPersonOne: String,
        namaPersonTwo: String,
        chatSendingTime: Long
    ) = chatRepository.addEmptyChatRoom(idPersonOne, idPersonTwo, fotoUrlPersonOne, fotoUrlPersonTwo, namaPersonOne, namaPersonTwo, chatSendingTime)
    suspend fun getJumlahPesanBelumDibacaUser(idCurrentUser: String) = chatRepository.getMessageNotReadByCurrentUser(idCurrentUser)

}