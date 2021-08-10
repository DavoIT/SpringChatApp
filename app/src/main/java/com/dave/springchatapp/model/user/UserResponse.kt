package com.dave.springchatapp.model.user

import com.google.gson.annotations.SerializedName

/**
 * Created by David Hakobyan on 6/25/21.
 */

class UserResponse {
    @SerializedName("username")
    lateinit var username: String

    @SerializedName("id")
    var id: Long = 0

}