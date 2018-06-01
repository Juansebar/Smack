package com.juanse.smack.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.juanse.smack.Controller.App
import com.juanse.smack.Model.Channel
import com.juanse.smack.Utilities.URL_GET_CHANNELS
import org.json.JSONException

object MessageService {

    val channels = ArrayList<Channel>()

    fun getChannels(context: Context, completion: (Boolean)-> Unit) {
        val channelsRequest = object : JsonArrayRequest(Method.GET, URL_GET_CHANNELS, null, Response.Listener { response ->
            try {
                for (channelIndex in 0 until response.length()) {
                    val channel = response.getJSONObject(channelIndex)
                    val id = channel.getString("_id")
                    val name = channel.getString("name")
                    val description = channel.getString("description")

                    this.channels.add(Channel(name, description, id))
                    completion(true)
                }
            } catch (error: JSONException) {
                Log.d("JSON", "EXC: " + error.localizedMessage)
                completion(false)
            }
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Failed to retrieve channels: ${error.localizedMessage}")
            completion(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf("Authorization" to "Bearer ${App.sharedPreferences.authToken}")
            }
        }

        App.sharedPreferences.requestQueue.add(channelsRequest)
    }

}