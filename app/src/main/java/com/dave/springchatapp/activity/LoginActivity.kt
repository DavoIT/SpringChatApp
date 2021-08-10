package com.dave.springchatapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.dave.springchatapp.R
import com.dave.springchatapp.enums.ConnectionStatus
import com.dave.springchatapp.manager.DataManager
import com.dave.springchatapp.networking.SocketConnector
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    private lateinit var loginInputLayout: TextInputLayout
    private lateinit var loginEditText: TextInputEditText
    private lateinit var loginButton: Button
    private lateinit var signUpButton: Button
    private lateinit var rootView: ViewGroup
    private lateinit var loadingView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupViews()
    }

    private fun setupViews() {
        findViewsByIds()
        setupLoginInput()
        setupLoginButton()
    }

    private fun findViewsByIds() {
        loginInputLayout = findViewById(R.id.loginInputLayout)
        loginEditText = findViewById(R.id.loginInputEditText)
        loginButton = findViewById(R.id.loginButton)
        signUpButton = findViewById(R.id.signUpButton)
        rootView = findViewById(R.id.rootView)
        loadingView = findViewById(R.id.loadingView)
    }

    private fun setupLoginInput() {
        loginEditText.doAfterTextChanged {
            loginInputLayout.error = null
        }
    }

    private fun setupLoginButton() {
        loginButton.setOnClickListener {
            loadingView.visibility = View.VISIBLE
            val username = loginEditText.text.toString().trim()
            DataManager.login(
                this@LoginActivity,
                username
            ) { errorMessage: String? ->
                loadingView.visibility = View.GONE
                errorMessage?.let {
                    loginInputLayout.error = errorMessage
                } ?: run {
                    val intent = Intent(this, ChatsActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        signUpButton.setOnClickListener {
            loadingView.visibility = View.VISIBLE
            val username = loginEditText.text.toString().trim()
            DataManager.signUp(
                this,
                username
            ) { response: String? ->
                loadingView.visibility = View.GONE
                response?.let {
                    loginInputLayout.error = response
                } ?: run {
                    val intent = Intent(this, ChatsActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}