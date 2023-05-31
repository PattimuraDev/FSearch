package repo.pattimuradev.fsearch.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_adapter_chat_ruang_obrolan.view.*
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.model.Chat


class RuangObrolanChatAdapter: RecyclerView.Adapter<RuangObrolanChatAdapter.ViewHolder>() {
    private var listChat: List<Chat>? = null
    fun setListChat(listChat: List<Chat>){
        this.listChat = listChat
        notifyDataSetChanged()
    }

    private var idUser: String? = null
    fun setCurrentUserId(idUser: String){
        this.idUser = idUser
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_adapter_chat_ruang_obrolan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView){
            if(listChat!![position].idSender == idUser){
                rv_chat_layout_sebelah_kanan.visibility = LinearLayout.VISIBLE
                rv_chat_message_sebelah_kanan.text = listChat!![position].message
                rv_chat_layout_sebelah_kiri.visibility = LinearLayout.GONE
            }else{
                rv_chat_layout_sebelah_kiri.visibility = LinearLayout.VISIBLE
                rv_chat_message_sebelah_kiri.text = listChat!![position].message
                rv_chat_layout_sebelah_kanan.visibility = LinearLayout.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return if(listChat.isNullOrEmpty()){
            0
        }else{
            listChat!!.size
        }
    }
}