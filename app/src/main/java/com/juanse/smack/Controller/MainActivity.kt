package com.juanse.smack.Controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.juanse.smack.R
import com.juanse.smack.Services.AuthService
import com.juanse.smack.Services.UserDataService
import com.juanse.smack.Utilities.BROADCAST_USER_DATA_CHANGE
import com.juanse.smack.Utilities.SOCKET_URL
import io.socket.client.IO
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

    val socket = IO.socket(SOCKET_URL)

    // Called when it receives broadcast
    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (AuthService.isLoggedIn) {
                // update elements in nav header
                userNameNavHeader.text = UserDataService.name
                userEmailNavHeader.text = UserDataService.email

                val resourceId = resources.getIdentifier(UserDataService.avatarName, "drawable", packageName)
                userImageNavHeader.setImageResource(resourceId)

                userImageNavHeader.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))

                loginButtonNavHeader.text = "Logout"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        setupViews()
    }

    override fun onResume() {
        super.onResume()

        // Register a broadcast Receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver, IntentFilter(BROADCAST_USER_DATA_CHANGE))

        socket.connect()
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReceiver)

        super.onPause()
    }

    override fun onDestroy() {
        socket.disconnect()

        super.onDestroy()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setupViews() {
        loginButtonNavHeader.setOnClickListener { loginButtonNavClicked() }
        addChannelButtonNavHeader.setOnClickListener { addChannelClicked() }
        sendMessageButton.setOnClickListener { sendMessageButtonClicked() }
    }

    private fun loginButtonNavClicked() {
        if (AuthService.isLoggedIn) {
            UserDataService.logout()
            userNameNavHeader.text = ""
            userEmailNavHeader.text = ""
            userImageNavHeader.setImageResource(R.drawable.profiledefault)
            userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
            loginButtonNavHeader.text = "Login"
        } else {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    private fun addChannelClicked() {
        if (AuthService.isLoggedIn) {
            val builder = AlertDialog.Builder(this)

            // Initialize views using Inflator
            val dialogView = layoutInflater.inflate(R.layout.add_channel_dialog, null)

            // Build dialog
            builder.setView(dialogView)
                    .setPositiveButton("Add") { dialog, which ->
                        // Perform some logic when clicked

                        // For Dialogs must access the view the old way by ID's
                        val nameTextField = dialogView.findViewById<EditText>(R.id.addChannelNameText)
                        val descriptionTextField = dialogView.findViewById<EditText>(R.id.addChannelDescriptionText)

                        val channleName = nameTextField.text.toString()
                        val channleDescription = nameTextField.text.toString()

                        // Create Channel with the channel name and description
                        socket.emit("newChannel", channleName, channleDescription)
                    }
                    .setNegativeButton("Cancel") { dialog, which ->
                        // Cancel and close dialog

                    }
                    .show()
        } else {
            Toast.makeText(this, "Must be Logged in to create a channel", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendMessageButtonClicked() {
        hideKeyboard()
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


