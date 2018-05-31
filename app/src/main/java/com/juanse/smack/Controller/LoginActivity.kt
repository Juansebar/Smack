package com.juanse.smack.Controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.juanse.smack.R
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

    }

    private fun signUpButtonClicked() {
        val signUpIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(signUpIntent)
        finish()
    }

}
