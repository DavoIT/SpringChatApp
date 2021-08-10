package com.dave.springchatapp.model

import org.json.JSONException
import org.json.JSONObject

/**
 * Created by David Hakobyan on 7/30/21.
 */
open class SocketMessage (action: String) {
    private val dataJson = JSONObject()
    init {
        try {
            dataJson.put("action", action)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun addParameter(key: String, value: Any?) {
        if (value != null) {
            try {
                dataJson.put(key, value)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    override fun toString(): String {
        return dataJson.toString()
    }
}