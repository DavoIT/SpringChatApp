package com.dave.springchatapp.`interface`

import com.dave.springchatapp.model.Message
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by David Hakobyan on 6/25/21.
 */
interface MessageService {
    @GET("messages")
    fun getMessagesForChat(@Query("id") id: Long): Call<List<Message>>

    @POST("messages")
    fun sendMessage(@Body message: Message): Call<Message>
}