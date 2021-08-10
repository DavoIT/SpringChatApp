package com.dave.springchatapp.networking

import com.dave.springchatapp.model.SocketMessage

/**
 * Created by David Hakobyan on 7/30/21.
 */
class LoginSocketMessage(userId: Long) : SocketMessage("login") {
    init {
        addParameter("user_id", userId)
    }
}