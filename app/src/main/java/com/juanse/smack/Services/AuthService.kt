package com.juanse.smack.Services

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.juanse.smack.Controller.App
import com.juanse.smack.Utilities.*
import org.json.JSONException
import org.json.JSONObject

object AuthService {

//    var isLoggedIn = false
//    var userEmail = ""
//    var authToken = ""

    fun registerUser(context: Context, email: String, password: String, completion: (Boolean) -> Unit) {

        // Create JSON Object
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)

        // Must convert the JSON obj to String so it can be converted to a byte array
        val requestBody = jsonBody.toString()

        // Create the request
        val registerRequest = object : StringRequest(Method.POST, URL_REGISTER, Response.Listener { response ->
            println(response)
            completion(true)
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not register user: $error")
            completion(false)
        }) {
            // Add headers and body
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                // Must convert to ByteArray
                return requestBody.toByteArray()
            }
        }

        // Add new request to Queue
        App.sharedPreferences.requestQueue.add(registerRequest)
    }

    fun loginUser(context: Context, email: String, password: String, completion: (Boolean) -> Unit) {
        val jsonObject = JSONObject()
        jsonObject.put("email", email)
        jsonObject.put("password", password)
        val requestBody = jsonObject.toString()
        
        val loginRequest = object : JsonObjectRequest(Method.POST, URL_LOGIN, null, Response.Listener { response ->
            try {
                // Parse Object
                App.sharedPreferences.authToken = response.getString("token")
                App.sharedPreferences.userEmail = response.getString("user")
                App.sharedPreferences.isLoggedIn = true
                completion(true)
            } catch (error: JSONException) {
                Log.d("ERROR", "EXC: " + error.localizedMessage)
                completion(false)
            }
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not login user: $error")
            completion(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        App.sharedPreferences.requestQueue.add(loginRequest)
    }

    fun createUser(context: Context, username: String, email: String, avatarName: String, avatarColor: String, completion: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("username", username)
        jsonBody.put("email", email)
        jsonBody.put("avatarName", avatarName)
        jsonBody.put("avatarColor", avatarColor)
        val requestBody = jsonBody.toString()

        val createUserRequest = object : JsonObjectRequest(Method.POST, URL_CREATE_USER, null, Response.Listener { response ->  
            println("RESPONSE: $response")
            try {
                UserDataService.id = response.getString("_id")
                UserDataService.email = response.getString("email")
                UserDataService.name = username
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.avatarColor = response.getString("avatarColor")

                completion(true)
            } catch (error: JSONException) {
                Log.d("ERROR", "EXC: " + error.localizedMessage)
                completion(false)
            }
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not create user: $error")
            completion(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
//                val headers = HashMap<String, String>()
//                headers.put("Authorization", "Bearer $authToken")

                return hashMapOf("Authorization" to "Bearer ${App.sharedPreferences.authToken}")
            }
        }

        App.sharedPreferences.requestQueue.add(createUserRequest)
    }

    fun findUserByEmail(context: Context, completion: (Boolean) -> Unit) {
        val findUserRequest = object : JsonObjectRequest(Method.GET, "$URL_GET_USER${App.sharedPreferences.userEmail}", null, Response.Listener { response ->
            try {
                UserDataService.email = response.getString("email")
                UserDataService.name = UserDataService.email.split("@").first()
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.id = response.getString("_id")

                val foundUserIntent = Intent(BROADCAST_USER_DATA_CHANGE)
                LocalBroadcastManager.getInstance(context).sendBroadcast(foundUserIntent)

                completion(true)
            } catch (error: JSONException) {
                Log.d("JSON", "EXC: " + error.localizedMessage)
                completion(false)
            }
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not find user: $error")
            completion(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf("Authorization" to "Bearer ${App.sharedPreferences.authToken}")
            }
        }

        /**
         * Volley automatically handles background thread executions for you
         */
        App.sharedPreferences.requestQueue.add(findUserRequest)
    }

}