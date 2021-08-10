package com.dave.springchatapp.model.chat

import com.dave.springchatapp.model.Message
import com.dave.springchatapp.model.user.User

/**
 * Created by David Hakobyan on 7/7/21.
 */
class Chat(chatResponse: ChatResponse) {
    val id: Long = chatResponse.id
    val name: String? = chatResponse.name
    var messages: ArrayList<Message> = chatResponse.messages
    var usersMap = hashMapOf<Long, User>()

    init {
        chatResponse.users.forEach {
            usersMap[it.getId()] = it
        }
    }

}