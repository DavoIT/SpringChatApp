package com.dave.springchatapp.model.chat

import com.dave.springchatapp.model.Message
import com.dave.springchatapp.model.user.User
import com.google.gson.annotations.SerializedName

/**
 * Created by David Hakobyan on 7/16/21.
 */
data class ChatResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String?,
    @SerializedName("messages") var messages: ArrayList<Message>,
    @SerializedName("users") var users: ArrayList<User>
)