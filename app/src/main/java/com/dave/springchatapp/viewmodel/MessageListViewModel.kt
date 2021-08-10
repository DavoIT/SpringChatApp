package com.dave.springchatapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dave.springchatapp.manager.DataManager
import com.dave.springchatapp.model.chat.Chat
import com.dave.springchatapp.model.Message

/**
 * Created by David Hakobyan on 7/13/21.
 */
class MessageListViewModel(private val chat: Chat) : ViewModel() {
    private val messages = MutableLiveData(arrayListOf<Message>())
    private val isLoading = MutableLiveData(false)

    fun getIsLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun getMessages(): LiveData<ArrayList<Message>> {
        return messages
    }

    fun updateMessages(context: Context) {
        isLoading.value = true
        DataManager.getMessagesForChat(context, chat.id) { response ->
            isLoading.value = false
            response?.let {
                if (response.isSuccessful) {
                    messages.postValue(response.body() as ArrayList<Message>?)
                }
            }
        }
    }

    fun addMessage(context: Context, message: Message) {
        DataManager.sendMessage(context, message) {
            it?.let {
                val existingMessages = messages.value
                existingMessages?.add(it)
                messages.postValue(existingMessages)
            }
        }
    }
}