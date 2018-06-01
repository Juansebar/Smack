package com.juanse.smack.Utilities

import android.content.Context
import android.content.SharedPreferences
import com.android.volley.toolbox.Volley

class SharedPreferences(context: Context) {

    val PREF_FILENAME = "preferences"
    val preferences = context.getSharedPreferences(PREF_FILENAME, 0) // ZERO means "content private"

    val IS_LOGGED_IN = "IS_LOGGED_IN"
    val AUTH_TOKEN = "AUTH_TOKEN"
    val USER_EMAIL = "USER_EMAIL"

    /**
     * Custom GETTER's and SETTER's
     */

    var isLoggedIn: Boolean
        get() = preferences.getBoolean(IS_LOGGED_IN, false)
        set(value) = preferences.edit().putBoolean(IS_LOGGED_IN, value).apply()

    var authToken: String
        get() = preferences.getString(AUTH_TOKEN, "")
        set(value) = preferences.edit().putString(AUTH_TOKEN, value).apply()

    var userEmail: String
        get() = preferences.getString(USER_EMAIL, "")
        set(value) = preferences.edit().putString(USER_EMAIL, value).apply()

    val requestQueue = Volley.newRequestQueue(context)

}