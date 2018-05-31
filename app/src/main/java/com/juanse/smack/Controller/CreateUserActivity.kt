package com.juanse.smack.Controller

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.Toast
import com.juanse.smack.R
import com.juanse.smack.Services.AuthService
import com.juanse.smack.Utilities.BROADCAST_USER_DATA_CHANGE
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

        signUpSpinner.visibility = View.INVISIBLE
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
        enableSpinner(true)

        // Extract input
        val username: String? = signUpUsernameText.text.toString().takeIf { it.isNotEmpty() }
        val email: String? = signUpEmailText.text.toString().takeIf { it.isNotEmpty() }
        val password: String? = signUpPasswordText.text.toString().takeIf { it.isNotEmpty() }

        val emailValid = email != null
        val passwordValid = password != null
        val usernameValid = username != null

        // Validation
        if (!emailValid && !passwordValid && !usernameValid) {
            showSnackbarError(signUpCreateUserButton, "Must enter a valid username, email and password")
        } else if (!emailValid) {
            showSnackbarError(signUpCreateUserButton, "Must enter an email")
        } else if (!passwordValid) {
            showSnackbarError(signUpCreateUserButton, "Must enter a password")
        } else if (!usernameValid) {
            showSnackbarError(signUpCreateUserButton, "Must enter a username")
        }

        if (emailValid && passwordValid && usernameValid) {
            AuthService.registerUser(this, email!!, password!!) { success ->
                if (success) {
                    showToast("Succesfully Registered User")

                    AuthService.loginUser(this, email, password) { loginSuccess ->
                        if (loginSuccess) {
                            showToast("Successfully logged in user")

                            AuthService.createUser(this, username!!, email, userAvatar, avatarColor) { success ->
                                if (success) {
                                    showToast("User created!")

                                    // Create Action Intent and Send a Local Broadcast (Notification in iOS)
                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)

                                    enableSpinner(false)
                                    finish() // Dismisses an activity
                                } else {
                                    showToast("User creation FAILED!", true)
                                }
                            }
                        } else {
                            showToast("Couldn't login, please try again", true)
                        }
                    }
                } else {
                    showToast("Something went wrong, please try again", true)
                }
            }
        }

    }

    private fun enableSpinner(enable: Boolean) {
        signUpSpinner.visibility = if (enable) View.VISIBLE else View.INVISIBLE
        signUpCreateUserButton.isEnabled = !enable
        signUpAvatarImage.isEnabled = !enable
        backgroundGeneratorButton.isEnabled = !enable
    }

    private fun showToast(message: String, isError: Boolean = false) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        if (isError) enableSpinner(false)
    }

    private fun showSnackbarError(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAction("Action", null).show()
    }

}
