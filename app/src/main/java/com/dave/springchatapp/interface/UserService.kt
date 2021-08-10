package com.dave.springchatapp.`interface`

import com.dave.springchatapp.model.user.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by David Hakobyan on 6/25/21.
 */
interface UserService {
    @GET("all-users")
    fun getAllUsers(@Query("id") id: Long): Call<List<UserResponse>>

    @GET("login")
    fun login(@Query("username") username: String): Call<UserResponse>

    @POST("sign-up")
    fun signUp(@Body username: String): Call<UserResponse>
}