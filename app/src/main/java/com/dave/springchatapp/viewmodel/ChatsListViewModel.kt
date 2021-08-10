package com.dave.springchatapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dave.springchatapp.manager.DataManager
import com.dave.springchatapp.model.chat.Chat

/**
 * Created by David Hakobyan on 7/13/21.
 */
class ChatsListViewModel : ViewModel() {
    private val chats = MutableLiveData(arrayListOf<Chat>())
    private val isLoading = MutableLiveData(false)

    fun getChats(): LiveData<ArrayList<Chat>> {
        return chats
    }

    fun getIsLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun updateChats(context: Context) {
        isLoading.value = true
        DataManager.getAllChats(context) { response ->
            isLoading.value = false
            response?.let {
                chats.postValue(it)
            }
        }
    }

    fun addChat(chat: Chat) {
        val arr = chats.value
        arr?.add(chat)
        chats.value = arr
    }
}