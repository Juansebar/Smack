package com.juanse.smack

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_create_user.*

class CreateUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
    }

    private fun setupViews() {
        signUpAvatarImage.setOnClickListener { onAvatarImageClick() }
        backgroundGeneratorButton.setOnClickListener { onBackgroundGeneratorButtonClick() }
        signUpCreateUserButton.setOnClickListener { onCreateUserClick() }
    }

    private fun onAvatarImageClick() {

    }

    private fun onBackgroundGeneratorButtonClick() {

    }

    private fun onCreateUserClick() {

    }

}
