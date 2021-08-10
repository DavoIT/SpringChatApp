package com.dave.springchatapp.model

import com.google.gson.annotations.SerializedName

/**
 * Created by David Hakobyan on 7/7/21.
 */

class Message(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("chatId")
    val chatId: Long,
    @SerializedName("senderId")
    val senderId: Long,
    @SerializedName("text")
    val text: String
)