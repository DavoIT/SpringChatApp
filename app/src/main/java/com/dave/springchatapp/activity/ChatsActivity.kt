package com.dave.springchatapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dave.springchatapp.R
import com.dave.springchatapp.adapter.ChatsAdapter
import com.dave.springchatapp.enums.ConnectionStatus
import com.dave.springchatapp.manager.DataManager
import com.dave.springchatapp.networking.SocketConnector
import com.dave.springchatapp.viewmodel.ChatsListViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.snackbar.Snackbar

class ChatsActivity : AppCompatActivity(), SocketConnector.OnConnectionListener {
    private lateinit var rootView: ViewGroup
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingView: LinearProgressIndicator
    private lateinit var swipeLayout: SwipeRefreshLayout
    private lateinit var chatsAdapter: ChatsAdapter
    private lateinit var newChatButton: ImageButton
    private lateinit var socketStatus: TextView

    private lateinit var chatsListListViewModel: ChatsListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)
        DataManager.getSocketConnector().addOnConnectionListener(this)
        setupViews()
        setupViewModels()
        try {
            chatsListListViewModel.updateChats(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupViewModels() {
        chatsListListViewModel = ViewModelProvider(this).get(ChatsListViewModel::class.java)
        chatsListListViewModel.getChats().observe(this) {
            if (swipeLayout.isRefreshing) {
                swipeLayout.isRefreshing = false
            }
            chatsAdapter.setList(it)
            if (it.size > 0) {
                recyclerView.smoothScrollToPosition(it.size - 1)
            }
        }
        chatsListListViewModel.getIsLoading().observe(this) {
            showUsersLoading(it)
        }
    }

    private fun setupViews() {
        rootView = findViewById(R.id.rootView)
        loadingView = findViewById(R.id.loadingView)
        swipeLayout = findViewById(R.id.swipeRefreshLayout)
        newChatButton = findViewById(R.id.newChatButton)
        socketStatus = findViewById(R.id.socketStatus)
        swipeLayout.setOnRefreshListener {
            chatsListListViewModel.updateChats(this)
        }
        newChatButton.setOnClickListener {
            openNewChatCreationPage()
        }
        setupListView()
    }

    private fun setupListView() {
        recyclerView = findViewById(R.id.recyclerView)
        chatsAdapter = ChatsAdapter {
            openChatWithId(it.id)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = chatsAdapter
    }

    private fun showUsersLoading(show: Boolean) {
        recyclerView.visibility = if (show) View.INVISIBLE else View.VISIBLE
        loadingView.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun openNewChatCreationPage() {
        TODO("Not yet implemented")
    }

    private fun openChatWithId(chatId: Long) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chat_id", chatId)
        startActivity(intent)
    }

    override fun onConnectionChange(connectionStatus: ConnectionStatus?) {
        connectionStatus?.name?.let {
            socketStatus.text = connectionStatus.name
            Snackbar.make(this.rootView, it, Snackbar.LENGTH_LONG).show()
        }
    }
}