package com.juanse.smack.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.juanse.smack.Utilities.URL_REGISTER
import org.json.JSONObject

object AuthService {

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
        Volley.newRequestQueue(context).add(registerRequest)
    }

}