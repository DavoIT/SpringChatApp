package com.dave.springchatapp.tools

import org.json.JSONObject
import retrofit2.Response


/**
 * Created by David Hakobyan on 7/6/21.
 */
class RetrofitErrorParser {
    companion object {
        fun getErrorMessage(errorResponse: Response<*>): String {
            return try {
                val responseBody = errorResponse.errorBody()
                val jsonObjectError = JSONObject(responseBody!!.string())
                jsonObjectError.getString("message")
            } catch (e: Exception) {
                e.printStackTrace()
                "Something went wrong"
            }
        }
    }
}