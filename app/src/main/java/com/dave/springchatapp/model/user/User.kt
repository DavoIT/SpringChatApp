package com.dave.springchatapp.model.user

import android.content.res.Resources
import com.dave.springchatapp.model.chat.Chat
import com.google.gson.annotations.SerializedName
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by David Hakobyan on 6/25/21.
 */

class User(@SerializedName("username") private var username: String) {
    @SerializedName("id")
    private var id: Long = 0

    private var chats = hashMapOf<Long, Chat>()

    companion object {
        @Throws(JSONException::class)
        fun fromJSON(jsonObject: JSONObject): User {
            val username = jsonObject.getString("username")
            val userObject = User(username)
            userObject.id = jsonObject.getLong("id")
            return userObject
        }

        fun fromUserResponse(userResponse: UserResponse): User {
            val user = User(userResponse.username)
            user.id = userResponse.id
            return user
        }
    }

    fun getId(): Long {
        return id
    }

    fun getName(): String {
        return username
    }

    fun getChats(): Map<Long, Chat> {
        return chats
    }

    @Throws(Resources.NotFoundException::class)
    fun getChatWithId(chatId: Long): Chat {
        val chat = chats[chatId]
        if (chat == null) {
            throw Resources.NotFoundException("Chat is not found")
        } else {
            return chat
        }
    }

    fun setChats(list: List<Chat>) {
        chats.clear()
        list.forEach { chat ->
            chats[chat.id] = chat
        }
    }

}