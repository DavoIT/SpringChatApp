package com.dave.springchatapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dave.springchatapp.R
import com.dave.springchatapp.manager.DataManager
import com.dave.springchatapp.model.chat.Chat
import com.dave.springchatapp.model.Message

/**
 * Created by David Hakobyan on 6/25/21.
 */
class MessagesAdapter(private val chat: Chat) : RecyclerView.Adapter<MessageViewHolder>() {
    private var messages = arrayListOf<Message>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = if (viewType == 0) {
            layoutInflater.inflate(R.layout.received_message_item, parent, false)
        } else {
            layoutInflater.inflate(R.layout.sent_message_item, parent, false)
        }
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.setupUsingMessage(chat, messages[position])
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun setList(messages: ArrayList<Message>) {
        this.messages = messages
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        val isReceived = message.senderId != DataManager.getUser()?.getId()
        return if (isReceived) {
            0
        } else {
            1
        }
    }

}

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var authorNameTextView: TextView = itemView.findViewById(R.id.authorName)
    private var messageTextView: TextView = itemView.findViewById(R.id.message)

    fun setupUsingMessage(chat: Chat, message: Message) {
        val senderId = message.senderId
        val senderUser = chat.usersMap[senderId]
        val authorName = senderUser?.getName()
        val text = message.text
        authorName?.let {
            authorNameTextView.text = it
        }?: run {
            authorNameTextView.text = "Unknown user"
        }
        messageTextView.text = text
    }
}
