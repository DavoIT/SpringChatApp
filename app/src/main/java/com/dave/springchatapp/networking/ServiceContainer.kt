package com.dave.springchatapp.networking

import com.dave.springchatapp.`interface`.ChatService
import com.dave.springchatapp.`interface`.MessageService
import com.dave.springchatapp.`interface`.UserService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by David Hakobyan on 7/16/21.
 */
class ServiceContainer private constructor() {
    companion object {
        val instance = ServiceContainer()
        const val BASE_URL = "http://192.168.1.25:8080/"
        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient().newBuilder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build()
            )
            .build()
        private val userApi = retrofit.create(UserService::class.java)
        private val chatApi = retrofit.create(ChatService::class.java)
        private val messageApi = retrofit.create(MessageService::class.java)
    }

    fun getUserApi(): UserService {
        return userApi
    }

    fun getChatApi(): ChatService {
        return chatApi
    }

    fun getMessageApi(): MessageService {
        return messageApi
    }

}