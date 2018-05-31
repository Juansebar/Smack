package com.juanse.smack.Controller

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.Editable
import android.view.View
import android.widget.Toast
import com.juanse.smack.R
import com.juanse.smack.Services.AuthService
import com.juanse.smack.Services.UserDataService
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        setupViews()
    }

    private fun setupViews() {
        signUpAvatarImage.setOnClickListener { generateAvatarImage() }
        backgroundGeneratorButton.setOnClickListener { onBackgroundGeneratorButtonClick() }
        signUpCreateUserButton.setOnClickListener { onCreateUserClick() }
    }

    private fun generateAvatarImage() {
        val random = Random()
        val color = random.nextInt(2)  // nums between 0 and 1
        val avatar = random.nextInt(28) // B/c we have 27 images

        if (color == 0) {
            userAvatar = "light$avatar"
        } else {
            userAvatar = "dark$avatar"
        }

        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
        signUpAvatarImage.setImageResource(resourceId)
    }

    private fun onBackgroundGeneratorButtonClick() {
        val random = Random()
        val r = random.nextInt(256)
        val g = random.nextInt(256)
        val b = random.nextInt(256)

        signUpAvatarImage.setBackgroundColor(Color.rgb(r, g, b))
        avatarColor = "[${r/255.0}, ${g/255.0}, ${b/255.0}, 1]"
    }

    private fun onCreateUserClick() {
        // Extract input
        val username: String? = signUpUsernameText.text.toString().takeIf { it.isNotEmpty() }
        val email: String? = signUpEmailText.text.toString().takeIf { it.isNotEmpty() } ?: null
        val password: String? = signUpPasswordText.text.toString().takeIf { it.isNotEmpty() } ?:  null

        val emailValid = email != null
        val passwordValid = password != null
        val usernameValid = username != null

        // Validation
        if (!emailValid && !passwordValid && !usernameValid) {
            showError(signUpCreateUserButton, "Must enter a valid username, email and password")
        } else if (!emailValid) {
            showError(signUpCreateUserButton, "Must enter an email")
        } else if (!passwordValid) {
            showError(signUpCreateUserButton, "Must enter a password")
        } else if (!usernameValid) {
            showError(signUpCreateUserButton, "Must enter a username")
        }

        if (emailValid && passwordValid && usernameValid) {
            AuthService.registerUser(this, email!!, password!!) { success ->
                if (success) {
                    Toast.makeText(this, "Succesfully Registered User", Toast.LENGTH_SHORT).show()

                    AuthService.loginUser(this, email, password) { loginSuccess ->
                        if (loginSuccess) {
                            Toast.makeText(this, "Successfully logged in user", Toast.LENGTH_SHORT).show()

                            AuthService.createUser(this, username!!, email, userAvatar, avatarColor) { success ->
                                if (success) {
                                    Toast.makeText(this, "User created!", Toast.LENGTH_SHORT).show()
                                    finish() // Dismisses an activity
                                } else {
                                    Toast.makeText(this, "User creation FAILED!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(this, "Couldn't login, please try again", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showError(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAction("Action", null).show()
    }

}
