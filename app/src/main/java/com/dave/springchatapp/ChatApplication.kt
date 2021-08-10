package com.dave.springchatapp

import android.app.Application
import android.content.Context

/**
 * Created by David Hakobyan on 6/25/21.
 */
class ChatApplication : Application() {
    companion object {
        private lateinit var context: ChatApplication
        fun getContext(): Context {
            return context
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}