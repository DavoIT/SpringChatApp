package com.dave.springchatapp.activity

import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dave.springchatapp.R
import com.dave.springchatapp.adapter.MessagesAdapter
import com.dave.springchatapp.manager.DataManager
import com.dave.springchatapp.model.chat.Chat
import com.dave.springchatapp.model.Message
import com.dave.springchatapp.tools.DialogShower
import com.dave.springchatapp.viewmodel.MessageListViewModel

class ChatActivity : AppCompatActivity() {
    private lateinit var chatName: TextView
    private lateinit var sendButton: ImageButton
    private lateinit var inputEditText: EditText
    private lateinit var chat: Chat
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messagesViewModel: MessageListViewModel
    private lateinit var adapter: MessagesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        handleIntent()
        initViews()
        makeSetups()
    }

    @Throws(Resources.NotFoundException::class)
    private fun handleIntent() {
        val intentChatId = intent?.getLongExtra("chat_id", -1)
        intentChatId?.let { chatId ->
            val existingChat = DataManager.getUser()?.getChatWithId(chatId)
            if (existingChat == null) {
                finish()
            } else {
                chat = existingChat
            }
        }
    }

    private fun initViews() {
        chatName = findViewById(R.id.name)
        messageRecyclerView = findViewById(R.id.recyclerView)
        sendButton = findViewById(R.id.sendButton)
        inputEditText = findViewById(R.id.input)
    }

    private fun makeSetups() {
        messageRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        setupToolbar()
        setupAdapterUsing()
        setupMessagesViewModel()
        setupInputBar()
    }

    private fun setupToolbar() {
        chat.name?.let {
            chatName.text = it
        } ?: run {
            var chatNameString = ""
            chat.usersMap.values.forEach {
                chatNameString += it.getName() + " / "
            }
        }
    }

    private fun setupAdapterUsing() {
        adapter = MessagesAdapter(chat)
        messageRecyclerView.adapter = adapter
    }

    private fun setupMessagesViewModel() {
        messagesViewModel = MessageListViewModel(chat)
        messagesViewModel.getMessages().observe(this, {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
            messageRecyclerView.smoothScrollToPosition(it.size)
        })
        messagesViewModel.updateMessages(this)
    }

    private fun setupInputBar() {
        inputEditText.addTextChangedListener {
            updateSendButtonStateFor(it)
        }
        sendButton.setOnClickListener {
            sendInputMessage()
        }
    }

    private fun sendInputMessage() {
        val currentUserId = DataManager.getUser()!!.getId()
        val message = Message(null, chat.id, currentUserId, inputEditText.text.toString().trim())
        messagesViewModel.addMessage(this, message)
        inputEditText.setText("")
    }

    private fun updateSendButtonStateFor(inputTextEditable: Editable?) {
        val length = inputTextEditable?.length
        length?.let {
            sendButton.isEnabled = it > 0
        } ?: run {
            sendButton.isEnabled = false
        }
    }

    fun goBack(view: View) {
        finish()
    }

    fun refresh(view: View) {
        messagesViewModel.updateMessages(this)
    }

    fun showMembers(view: View) {
        var membersListString = ""
        chat.usersMap.values.forEach {
            membersListString += "id: " + it.getId() + " name: " + it.getName() + "\n"
        }
        DialogShower.showSimpleOKDialog(
            this,
            "Members",
            membersListString
        ) { dialog, _ -> dialog.dismiss() }
    }
}