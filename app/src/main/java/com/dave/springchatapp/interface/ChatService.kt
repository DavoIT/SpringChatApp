package com.dave.springchatapp.`interface`

import com.dave.springchatapp.model.chat.ChatResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by David Hakobyan on 6/25/21.
 */
interface ChatService {
    @GET("all-chats")
    fun getAllChats(@Query("id") id: Long): Call<List<ChatResponse>>
}