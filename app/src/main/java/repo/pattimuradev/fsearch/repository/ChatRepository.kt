package repo.pattimuradev.fsearch.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import repo.pattimuradev.fsearch.misc.ObjectSorter
import repo.pattimuradev.fsearch.model.Chat
import repo.pattimuradev.fsearch.model.ChatRoom

/**
 * Repository berisi transaksi terkait chat dengan layanan firebase
 * @author PattimuraDev (Dwi Satria Patra)
 */
class ChatRepository {
    private val firestoreDb = FirebaseFirestore.getInstance()

    // live data
    private val allChatRoom = MutableLiveData<List<ChatRoom>>()
    val allChatRoomLiveData: LiveData<List<ChatRoom>> = allChatRoom
    private val allMessageFromSpesificChatRoom = MutableLiveData<List<Chat>>()
    val allMessageFromSpesificChatRoomLiveData: LiveData<List<Chat>> = allMessageFromSpesificChatRoom
    private val newEmptyChatRoom = MutableLiveData<ChatRoom>()
    val newEmptyChatRoomLiveData:LiveData<ChatRoom> = newEmptyChatRoom
    private val jumlahPesanBelumDibacaUser = MutableLiveData<Int>()
    val jumlahPesanBelumDibacaUserLiveData: LiveData<Int> = jumlahPesanBelumDibacaUser

    /**
     * Fungsi untuk mendapatkan list semua ruang obrolan/chat room
     * @author PattimuraDev (Dwi Satria Patra)
     * @param currentUserid id dari user yang sedang memakai aplikasi
     */
    suspend fun getAllChatRoom(currentUserid: String){
        firestoreDb.collection("chat_room")
            .addSnapshotListener { value, _ ->
                val listChatRoomResult = mutableListOf<ChatRoom>()
                if(!value?.isEmpty!!){
                    value.forEach { chatRoomSnapshot ->
                        val chatRoom = chatRoomSnapshot.toObject(ChatRoom::class.java)
                        if(chatRoom.personInChat.contains(currentUserid)){
                            listChatRoomResult += chatRoom
                        }
                    }
                }
                allChatRoom.postValue(listChatRoomResult)
            }
    }

    /**
     * Fungsi untuk mendapatkan list semua pesan dari ruang obrolan tertentu
     * @author PattimuraDev (Dwi Satria Patra)
     * @param chatRoomId id dari chat room yang dimaksud
     */
    fun getAllMessageFromAChatRoom(chatRoomId: String){
        firestoreDb.collection("chat_room")
            .document(chatRoomId)
            .get()
            .addOnSuccessListener { snapshot ->
                val chatRoom = snapshot.toObject(ChatRoom::class.java)
                val listChat = ObjectSorter.chatSorter(chatRoom!!.listChat)
                allMessageFromSpesificChatRoom.postValue(listChat)
            }
    }

    /**
     * Fungsi untuk mendapatkan jumlah pesan belum dibaca oleh pengguna saat ini
     * @author PattimuraDev (Dwi Satria Patra)
     * @param currentUserid id dari pengguna yang sedang menggunakan aplikasi
     */
    fun getMessageNotReadByCurrentUser(currentUserid: String){
        firestoreDb.collection("chat_room")
            .get()
            .addOnSuccessListener {
                var notReadCounts = 0
                it.forEach { chatRoomSnapshot ->
                    val chatRoom = chatRoomSnapshot.toObject(ChatRoom::class.java)
                    val currentUserPosition = chatRoom.personInChat.indexOf(currentUserid)
                    notReadCounts += if(currentUserPosition == 0){
                        chatRoom.messageNotReadByPersonOne
                    }else{
                        chatRoom.messageNotReadByPersonTwo
                    }
                }
                jumlahPesanBelumDibacaUser.postValue(notReadCounts)
            }
    }

    /**
     * Fungsi untuk mendapatkan id dari chat room sementara yang masih kosong, digunakan untuk
     * membuat chat room ketika 2 user belum pernah bertukar pesan
     * @see addEmptyChatRoom
     * @author PattimuraDev (Dwi Satria Patra)
     * @param chatRoomId id dari user yang sedang memakai aplikasi
     */
    private fun getEmptyChatRoom(chatRoomId: String){
        firestoreDb.collection("chat_room")
            .document(chatRoomId)
            .get()
            .addOnSuccessListener { snapshot ->
                val chatRoom = snapshot.toObject(ChatRoom::class.java)
                newEmptyChatRoom.postValue(chatRoom)
            }
    }

    /**
     * Fungsi untuk membuat chat room sementara yang masih kosong, digunakan untuk
     * membuat chat room ketika 2 user belum pernah bertukar pesan
     * @see getEmptyChatRoom
     * @author PattimuraDev (Dwi Satria Patra)
     * @param idPersonOne id dari user yang sedang memakai aplikasi dan akan mengirimkan pesan
     * @param idPersonTwo id dari user yang akan dikirimi pesan
     * @param fotoUrlPersonOne url dari foto profil user yang sedang memakai aplikasi dan
     * ingin mengirimkan pesan
     * @param fotoUrlPersonTwo url dari foto profil user yang akan dikirimi pesan
     * @param namaPersonOne nama dari  user yang sedang memakai aplikasi dan ingin
     * mengirimkan pesan
     * @param namaPersonTwo nama dari user yang akan dikirimi pesan
     * @param chatSendingTime waktu pesan terakhir dari obrolan, disesuaikan ke waktu saat
     * fungsi ini dipanggil
     */
    suspend fun addEmptyChatRoom(idPersonOne: String, idPersonTwo: String, fotoUrlPersonOne: String?, fotoUrlPersonTwo: String?, namaPersonOne: String?, namaPersonTwo: String?, chatSendingTime: Long){
        val document = firestoreDb.collection("chat_room").document()
        val documentId = document.id
        val chatRoom = ChatRoom(
            documentId,
            arrayListOf(idPersonOne, idPersonTwo),
            arrayListOf(),
            "",
            chatSendingTime,
            0,
            0,
            fotoUrlPersonOne!!,
            fotoUrlPersonTwo!!,
            namaPersonOne!!,
            namaPersonTwo!!
        )
        firestoreDb.collection("chat_room")
            .document(documentId)
            .set(chatRoom)
            .addOnSuccessListener {
                getEmptyChatRoom(documentId)
            }
    }

    /**
     * Fungsi untuk mengirimkan pesan kepada seseorang
     * @author PattimuraDev (Dwi Satria Patra)
     * @param chatRoomId id dari chat room atau ruang obrolan yang digunakan
     * @param chat objek chat yaitu pesan yang ingin dikirimkan
     * @param fotoUrlPersonOne url foto profil dari user yang berada di urutan pertama dari
     * list person yang tergabung dalam chat room
     * @param fotoUrlPersonTwo url foto profil dari user yang berada di urutan kedua dari
     * list person yang tergabung dalam chat room
     * @param namaPersonOne nama dari user yang berada di urutan pertama dari
     * list person yang tergabung dalam chat room
     * @param namaPersonTwo nama dari user yang berada di urutan kedua dari
     * list person yang tergabung dalam chat room
     */
    suspend fun sendChatMessage(chatRoomId: String?, chat: Chat, fotoUrlPersonOne: String?, fotoUrlPersonTwo: String?, namaPersonOne: String?, namaPersonTwo: String?){
        if(chatRoomId == null){
            firestoreDb.collection("chat_room")
                .addSnapshotListener { value, _ ->
                    if(!value?.isEmpty!!){
                        var chatRoomAvailable = false
                        value.forEach { chatRoomSnapshot ->
                            val chatRoom = chatRoomSnapshot.toObject(ChatRoom::class.java)
                            if(chatRoom.personInChat.contains(chat.idReceiver) && chatRoom.personInChat.contains(chat.idSender)){
                                chatRoomAvailable = true
                                val posisiUser = chatRoom.personInChat.indexOf(chat.idSender)
                                if(posisiUser == 0){
                                    firestoreDb.collection("chat_room")
                                        .document(chatRoom.chatRoomId!!)
                                        .update(mapOf(
                                            "lastChatMessage" to chat.message,
                                            "lastChatTime" to chat.sendingTime,
                                            "listChat" to FieldValue.arrayUnion(chat),
                                            "messageNotReadByPersonTwo" to FieldValue.increment(1)
                                        ))
                                        .addOnSuccessListener {
                                            getAllMessageFromAChatRoom(chatRoom.chatRoomId!!)
                                        }
                                }else{
                                    firestoreDb.collection("chat_room")
                                        .document(chatRoom.chatRoomId!!)
                                        .update(mapOf(
                                            "lastChatMessage" to chat.message,
                                            "lastChatTime" to chat.sendingTime,
                                            "listChat" to FieldValue.arrayUnion(chat),
                                            "messageNotReadByPersonOne" to FieldValue.increment(1)
                                        ))
                                        .addOnSuccessListener {
                                            getAllMessageFromAChatRoom(chatRoom.chatRoomId!!)
                                        }
                                }

                            }
                        }
                        if(!chatRoomAvailable){
                            val document = firestoreDb.collection("chat_room").document()
                            val documentId = document.id
                            val chatRoom = ChatRoom(
                                documentId,
                                arrayListOf(chat.idSender, chat.idReceiver),
                                arrayListOf(chat),
                                chat.message,
                                chat.sendingTime,
                                0,
                                1,
                                fotoUrlPersonOne!!,
                                fotoUrlPersonTwo!!,
                                namaPersonOne!!,
                                namaPersonTwo!!
                            )
                            firestoreDb.collection("chat_room")
                                .document(documentId)
                                .set(chatRoom)
                                .addOnSuccessListener {
                                    getAllMessageFromAChatRoom(documentId)
                                }
                        }
                    }else{
                        val document = firestoreDb.collection("chat_room").document()
                        val documentId = document.id
                        val chatRoom = ChatRoom(
                            documentId,
                            arrayListOf(chat.idSender, chat.idReceiver),
                            arrayListOf(chat),
                            chat.message,
                            chat.sendingTime,
                            0,
                            1,
                            fotoUrlPersonOne!!,
                            fotoUrlPersonTwo!!,
                            namaPersonOne!!,
                            namaPersonTwo!!
                        )
                        firestoreDb.collection("chat_room")
                            .document(documentId)
                            .set(chatRoom)
                            .addOnSuccessListener {
                                getAllMessageFromAChatRoom(documentId)
                            }
                    }
                }
        }else{
            firestoreDb.collection("chat_room")
                .document(chatRoomId)
                .get()
                .addOnSuccessListener { snapshot ->
                    val chatRoom = snapshot.toObject(ChatRoom::class.java)
                    val posisiUser = chatRoom!!.personInChat.indexOf(chat.idSender)
                    if(posisiUser == 0){
                        firestoreDb.collection("chat_room")
                            .document(chatRoomId)
                            .update(mapOf(
                                "lastChatMessage" to chat.message,
                                "lastChatTime" to chat.sendingTime,
                                "listChat" to FieldValue.arrayUnion(chat),
                                "messageNotReadByPersonTwo" to FieldValue.increment(1)
                            ))
                            .addOnSuccessListener {
                                getAllMessageFromAChatRoom(chatRoomId)
                            }
                    }else{
                        firestoreDb.collection("chat_room")
                            .document(chatRoomId)
                            .update(mapOf(
                                "lastChatMessage" to chat.message,
                                "lastChatTime" to chat.sendingTime,
                                "listChat" to FieldValue.arrayUnion(chat),
                                "messageNotReadByPersonOne" to FieldValue.increment(1)
                            ))
                            .addOnSuccessListener {
                                getAllMessageFromAChatRoom(chatRoomId)
                            }
                    }
                }

        }
    }

    companion object {
        @Volatile
        private var instance: ChatRepository? = null
        fun getInstance(): ChatRepository{
            return instance ?: synchronized(this) {
                if (instance == null) {
                    instance = ChatRepository()
                }
                return instance as ChatRepository
            }
        }
    }
}