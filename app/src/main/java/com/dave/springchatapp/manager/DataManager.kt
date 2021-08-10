package com.dave.springchatapp.manager

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.dave.springchatapp.model.*
import com.dave.springchatapp.model.chat.Chat
import com.dave.springchatapp.model.chat.ChatResponse
import com.dave.springchatapp.model.user.User
import com.dave.springchatapp.model.user.UserResponse
import com.dave.springchatapp.networking.ServiceContainer
import com.dave.springchatapp.networking.SocketConnector
import com.dave.springchatapp.tools.DialogShower
import com.dave.springchatapp.tools.RetrofitErrorParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by David Hakobyan on 6/25/21.
 */
object DataManager {
    private var user: User? = null
    private val socketConnector = SocketConnector()

    fun getUser(): User? {
        return user
    }

    fun getSocketConnector(): SocketConnector {
        return socketConnector
    }

    fun login(context: Context, nickname: String, closure: (String?) -> Unit) {
        val loginRequest = ServiceContainer.instance.getUserApi().login(nickname)
        loginRequest.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                val isSuccessful = response.isSuccessful
                val responseBody = response.body()
                if (isSuccessful && responseBody != null) {
                    user = User.fromUserResponse(responseBody)
                    socketConnector.connect("ws://192.168.1.25:8080/" +  SocketConnector.SOCKET_HOST_NAME)
                    closure(null)
                } else {
                    clearUserData()
                    closure(RetrofitErrorParser.getErrorMessage(response))
                }

            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                showFailure(context)
                closure("Something went wrong")
            }
        })
    }

    fun signUp(context: Context, nickname: String, closure: (String?) -> Unit) {
        val signUpRequest = ServiceContainer.instance.getUserApi().signUp(nickname)
        signUpRequest.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                val isSuccessful = response.isSuccessful
                val responseBody = response.body()
                if (isSuccessful && responseBody != null) {
                    user = User.fromUserResponse(responseBody)
                    socketConnector.connect("ws://192.168.1.25:8080/" +  SocketConnector.SOCKET_HOST_NAME)
                    closure(null)
                } else {
                    clearUserData()
                    closure(RetrofitErrorParser.getErrorMessage(response))
                }

            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                showFailure(context)
                closure(null)
            }
        })
    }

    fun getAllUsers(context: Context, closure: (List<User>?) -> Unit) {
        val call = ServiceContainer.instance.getUserApi().getAllUsers(user!!.getId())
        Handler(Looper.getMainLooper()).postDelayed({
            call.enqueue(object : Callback<List<UserResponse>> {
                override fun onResponse(call: Call<List<UserResponse>>, response: Response<List<UserResponse>>) {
                    val isSuccessful = response.isSuccessful
                    val responseBody = response.body()
                    if(isSuccessful && responseBody!= null) {
                        val users = arrayListOf<User>()
                        responseBody.forEach {
                            val user = User.fromUserResponse(it)
                            users.add(user)
                        }
                        closure(users)
                    } else {
                        closure(null)
                    }

                }

                override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                    showFailure(context)
                    closure(null)
                }
            })
        }, 1000)
    }

    fun getAllChats(context: Context, response: (ArrayList<Chat>?) -> Unit) {
        val call = ServiceContainer.instance.getChatApi().getAllChats(user!!.getId())
        Handler(Looper.getMainLooper()).postDelayed({
            call.enqueue(object : Callback<List<ChatResponse>> {
                override fun onResponse(
                    call: Call<List<ChatResponse>>,
                    serverResponse: Response<List<ChatResponse>>
                ) {
                    val chats: ArrayList<Chat> = arrayListOf()
                    if (serverResponse.isSuccessful) {
                        serverResponse.body()?.let {
                            it.forEach { chat ->
                                val chatModel = Chat(chat)
                                chats.add(chatModel)
                            }
                            user!!.setChats(chats)
                        }
                    }
                    response(chats)
                }

                override fun onFailure(call: Call<List<ChatResponse>>, t: Throwable) {
                    showFailure(context)
                    response(null)
                }
            })
        }, 1000)
    }

    fun getMessagesForChat(
        context: Context,
        chatId: Long,
        response: (Response<List<Message>>?) -> Unit
    ) {
        val call = ServiceContainer.instance.getMessageApi().getMessagesForChat(chatId)
        Handler(Looper.getMainLooper()).postDelayed({
            call.enqueue(object : Callback<List<Message>> {
                override fun onResponse(
                    call: Call<List<Message>>,
                    serverResponse: Response<List<Message>>
                ) {
                    if (serverResponse.isSuccessful) {
                        serverResponse.body()?.let {
                            user!!.getChatWithId(chatId).messages = it as ArrayList<Message>
                        }
                    }
                    response(serverResponse)
                }

                override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                    showFailure(context)
                    response(null)
                }
            })
        }, 1000)
    }

    fun sendMessage(context: Context, message: Message, response: (Message?) -> Unit) {
        val call = ServiceContainer.instance.getMessageApi().sendMessage(message)
        Handler(Looper.getMainLooper()).postDelayed({
            call.enqueue(object : Callback<Message> {
                override fun onResponse(call: Call<Message>, serverResponse: Response<Message>) {
                    if (serverResponse.isSuccessful) {
                        serverResponse.body()?.let {
//                            user?.getChatWithId(message.chatId)?.messages?.add(it)
                            response(it)
                            return
                        }
                    }
                    response(message)
                }

                override fun onFailure(call: Call<Message>, t: Throwable) {
                    showFailure(context)
                    response(null)
                }

            })
        }, 1000)
    }

    private fun showFailure(context: Context, text: String = "Something went wrong") {
        DialogShower.showSimpleOKDialog(
            context, "Warning", text
        ) { dialog, _ -> dialog?.dismiss() }
    }

    private fun clearUserData() {
        user = null
    }
}