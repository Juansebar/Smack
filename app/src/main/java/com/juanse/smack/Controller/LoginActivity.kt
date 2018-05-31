package com.juanse.smack.Controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.view.inputmethod.InputMethodManager
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

        loginSpinner.visibility = View.INVISIBLE
    }

    private fun loginButtonClicked() {
        hideKeyboard()
        enableSpinner(true)

        val email = loginEmailText.text.toString().takeIf { it.isNotEmpty() }
        val password = loginPasswordText.text.toString().takeIf { it.isNotEmpty() }

        if (email != null && password != null) {
            AuthService.loginUser(this, email, password) { success ->
                if (success) {
                    println("token: ${AuthService.authToken}")
                    AuthService.findUserByEmail(this) { findSuccess ->
                        if (findSuccess) {
                            enableSpinner(false)
                            finish()
                        } else {
                            showToast("Find user FAILED", true)
                        }
                    }
                } else {
                    showToast("Login FAILED", true)
                }
            }
        } else {
            showSnackbarError(loginSignUpButton, "Please fill in both email and password.")
        }
    }

    private fun signUpButtonClicked() {
        val signUpIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(signUpIntent)
        finish()
    }

    private fun enableSpinner(enable: Boolean) {
        loginSpinner.visibility = if (enable) View.VISIBLE else View.INVISIBLE
        loginLoginButton.isEnabled = !enable
        loginSignUpButton.isEnabled = !enable
    }

    private fun showToast(message: String, isError: Boolean = false) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        if (isError) enableSpinner(false)
    }

    private fun showSnackbarError(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAction("Action", null).show()
    }

    fun hideKeyboard() {
        // Create an InputMethodManager
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText) {
            // Hides input from window, from what is in focus (our keyboard)
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }

}
