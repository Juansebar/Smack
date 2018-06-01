package com.juanse.smack.Controller

import android.app.Application
import com.juanse.smack.Utilities.SharedPreferences


/**
 * This will be called before anything in the app is created
 */

class App: Application() {

    // Like a singleton but for use inside a class
    companion object {
        // Allows only one instance of this class
        lateinit var sharedPreferences: SharedPreferences
    }

    override fun onCreate() {
        // Initialize SharedPreferences so they are globally available
        sharedPreferences = SharedPreferences(applicationContext)

        super.onCreate()
    }

}