package com.dave.springchatapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dave.springchatapp.R
import com.dave.springchatapp.model.chat.Chat

/**
 * Created by David Hakobyan on 6/25/21.
 */
class ChatsAdapter(var itemClickListener: ((Chat) -> Unit)) :
    RecyclerView.Adapter<ChatViewHolder>() {
    private var list = arrayListOf<Chat>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.text_item, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.setupUsingChat(list[position], itemClickListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<Chat>) {
        this.list = list as ArrayList<Chat>
        notifyDataSetChanged()
    }

    fun addChat(chat: Chat) {
        list.add(chat)
        notifyItemInserted(list.size - 1)
    }

}

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var nameTextView: TextView = itemView.findViewById(R.id.name)
    private var itemLayout: View = itemView.findViewById(R.id.itemLayout)

    fun setupUsingChat(chat: Chat, itemClickListener: ((Chat) -> Unit)) {
        var name = chat.name
        if (name == null) {
            name = ""
            chat.usersMap.values.forEach {
                name += it.getName() + " / "
            }
        }
        nameTextView.text = name
        itemLayout.setOnClickListener {
            itemClickListener(chat)
        }
    }

}
