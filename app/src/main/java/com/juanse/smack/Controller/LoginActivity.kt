package com.juanse.smack.Controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.widget.Toast
import com.juanse.smack.R
import com.juanse.smack.Services.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupViews()
    }

    private fun setupViews() {
        loginLoginButton.setOnClickListener { loginButtonClicked() }
        loginSignUpButton.setOnClickListener { signUpButtonClicked() }
    }

    private fun loginButtonClicked() {
        val email = loginEmailText.text.toString()
        val password = loginPasswordText.text.toString()

        AuthService.loginUser(this, email, password) { success ->
            if (success) {
                println("token: ${AuthService.authToken}")
                AuthService.findUserByEmail(this) { success ->
                    if (success) {
                        finish()
                    } else {
                        Toast.makeText(this, "Find user FAILED", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Login FAILED", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signUpButtonClicked() {
        val signUpIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(signUpIntent)
        finish()
    }

}
