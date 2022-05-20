package com.weatherapp.app.location

import android.app.Activity
import android.location.Geocoder
import com.weatherapp.data.model.Coordinates
import java.util.*

class FindLocationClass {

    fun search(string: String, activity: Activity): Coordinates {
        val geocoder = Geocoder(activity, Locale.getDefault())
        val fromLocationName = geocoder.getFromLocationName(string, 1)
        if (fromLocationName.isNotEmpty()) {
            return Coordinates(
                fromLocationName[0].latitude,
                fromLocationName[0].longitude
            )
        } else throw Exception("Did not find location :(")
    }
}