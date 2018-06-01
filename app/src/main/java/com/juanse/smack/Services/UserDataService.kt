package com.juanse.smack.Services

import android.graphics.Color
import com.juanse.smack.Controller.App
import java.util.*

object UserDataService {

    var id = ""
    var avatarColor = ""
    var avatarName = ""
    var email = ""
    var name = ""

    fun logout() {
        id = ""
        avatarName = ""
        avatarColor = ""
        email = ""
        name = ""
        App.sharedPreferences.authToken = ""
        App.sharedPreferences.userEmail = ""
        App.sharedPreferences.isLoggedIn = false
    }

    fun returnAvatarColor(components: String) : Int {
        // [0.9490196078431372, 0.7098039215686275, 0.27450980392156865, 1]

        // Remove all that is not a number
        val strippedColor = components
                .replace("[", "")
                .replace("]", "")
                .replace(",", "")

//        val colorArray = strippedColor.split(" ")
//
//        var color: Int
//
//        if (colorArray.count() == 4) {
//            val red = (colorArray[0].toDouble() * 255).toInt()
//            val green = (colorArray[1].toDouble() * 255).toInt()
//            val blue = (colorArray[2].toDouble() * 255).toInt()
//
//            color = Color.rgb(red, green, blue)
//        }

        var r = 0
        var g = 0
        var b = 0

        val scanner = Scanner(strippedColor)

        if (scanner.hasNext()) {
            r = (scanner.nextDouble() * 255).toInt()
            g = (scanner.nextDouble() * 255).toInt()
            b = (scanner.nextDouble() * 255).toInt()
        }

        return Color.rgb(r, g, b)
    }

}