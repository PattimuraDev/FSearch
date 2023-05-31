package repo.pattimuradev.fsearch.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_adapter_chat_room.view.*
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.DateAndTimeHandler
import repo.pattimuradev.fsearch.model.ChatRoom

class ChatRoomAdapter(private val onClick : (ChatRoom, Int) -> Unit) : RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>() {
    private var listChatRoom: List<ChatRoom>? = null
    private var currentUserId: String? = null

    fun setListChatRoom(listChatRoom: List<ChatRoom>){
        this.listChatRoom = listChatRoom
        notifyDataSetChanged()
    }
    fun setCurrentUserId(currentUserId: String){
        this.currentUserId = currentUserId
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_adapter_chat_room, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView){
            val posisiCurrentUser = listChatRoom!![position].personInChat.indexOf(currentUserId)
            if(posisiCurrentUser == 0){
                rv_chat_room_nama_pengirim.text = listChatRoom!![position].namaPersonTwo
                if(listChatRoom!![position].messageNotReadByPersonOne == 0){
                    rv_chat_room_badge_message_not_read.isInvisible = true
                }else{
                    rv_chat_room_badge_message_not_read.text = listChatRoom!![position].messageNotReadByPersonOne.toString()
                }
                Glide.with(rv_chat_room_foto_user_pengirim.context)
                    .load(listChatRoom!![position].fotoUrlPersonTwo)
                    .error(R.drawable.standard_user_photo)
                    .into(rv_chat_room_foto_user_pengirim)
            }else{
                rv_chat_room_nama_pengirim.text = listChatRoom!![position].namaPersonOne
                if(listChatRoom!![position].messageNotReadByPersonTwo == 0){
                    rv_chat_room_badge_message_not_read.isInvisible = true
                }else{
                    rv_chat_room_badge_message_not_read.text = listChatRoom!![position].messageNotReadByPersonTwo.toString()
                }
                Glide.with(rv_chat_room_foto_user_pengirim.context)
                    .load(listChatRoom!![position].fotoUrlPersonOne)
                    .error(R.drawable.standard_user_photo)
                    .into(rv_chat_room_foto_user_pengirim)
            }

            rv_chat_room_last_message.text = listChatRoom!![position].lastChatMessage
            rv_chat_room_waktu_kirim.text = DateAndTimeHandler.formatWaktuChatDikirim(listChatRoom!![position].lastChatTime)

            rv_chat_room_container.setOnClickListener {
                onClick(listChatRoom!![position], posisiCurrentUser)
            }
        }
    }

    override fun getItemCount(): Int {
        return if(listChatRoom.isNullOrEmpty()){
            0
        }else{
            listChatRoom!!.size
        }
    }
}