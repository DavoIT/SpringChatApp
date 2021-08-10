package com.dave.springchatapp.networking

import android.os.Handler
import android.os.Looper
import com.dave.springchatapp.enums.ConnectionStatus
import com.dave.springchatapp.manager.DataManager
import com.dave.springchatapp.model.SocketMessage
import com.dave.springchatapp.tools.WeakList
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * Created by David Hakobyan on 7/30/21.
 */
class SocketConnector : WebSocketListener() {
    companion object {
        const val SOCKET_HOST_NAME = "my_endpoint"
    }

    private val handler = Handler(Looper.getMainLooper())
    private var connectionStatus: ConnectionStatus = ConnectionStatus.Disconnected
    private val onConnectionListeners: WeakList<OnConnectionListener> = WeakList()
    private val messageQueue: MutableList<SocketMessage> = ArrayList<SocketMessage>()
    private val client: OkHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null

    fun emit(request: SocketMessage) {
        if (connectionStatus === ConnectionStatus.Connected) {
            webSocket?.send(request.toString())
        } else {
            messageQueue.add(request)
        }
    }

    fun connect(host: String) {
        if (connectionStatus !== ConnectionStatus.Disconnected) {
            return
        }
        val request = Request.Builder().url(host).build()
        webSocket = client.newWebSocket(request, this)
        connectionStatus = ConnectionStatus.Connecting
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        handler.post {
            connectionStatus = ConnectionStatus.Connecting
            login()
            sendDataQueue()
            notifyOnConnectionChange(connectionStatus)
        }
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        handler.post {
            try {
                val jsonObject = JSONObject(text)
                val actionType = jsonObject.getString("action")
                if (actionType == "login") {
                    connectionStatus = ConnectionStatus.Connected
                    notifyOnConnectionChange(connectionStatus)
                } else {
//                    EventManager.getInstance().handleEvent(jsonObject, true)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        handler.post {
            connectionStatus = ConnectionStatus.Disconnected
            notifyOnConnectionChange(connectionStatus)
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        handler.post {
            connectionStatus = ConnectionStatus.Disconnected
            notifyOnConnectionChange(connectionStatus)
        }
    }

    fun disconnect() {
        if (webSocket != null) {
            webSocket!!.close(1000, "")
            webSocket = null
            connectionStatus = ConnectionStatus.Disconnected
            notifyOnConnectionChange(connectionStatus)
        }
    }

    private fun sendDataQueue() {
        val localQueue: List<SocketMessage> = ArrayList(messageQueue)
        messageQueue.clear()
        for (request in localQueue) {
            emit(request)
        }
    }

    fun getConnectionStatus(): ConnectionStatus {
        return connectionStatus
    }

    // Listeners
    interface OnConnectionListener {
        fun onConnectionChange(connectionStatus: ConnectionStatus?)
    }

    fun addOnConnectionListener(listener: OnConnectionListener) {
        onConnectionListeners.add(listener)
    }

    fun removeOnConnectionListener(listener: OnConnectionListener?) {
        onConnectionListeners.remove(listener)
    }

    fun notifyOnConnectionChange(connectionStatus: ConnectionStatus?) {
        for (listener in onConnectionListeners) {
            listener.onConnectionChange(connectionStatus)
        }
    }

    @Throws(Exception::class)
    private fun login() {
        DataManager.getUser()?.getId()?.let {
            val loginRequest = LoginSocketMessage(it)
            val req = JSONObject("{\n" +
                    "    \"action\":\"message\",\n" +
                    "    \"sender_id\": 1,\n" +
                    "    \"chat_id\":\"5\",\n" +
                    "    \"text\":\"HELLO ARA DE YAAAAa!\"\n" +
                    "}")
            webSocket?.send(loginRequest.toString())
        } ?: run {
            throw Exception("No user found")
        }
    }

}