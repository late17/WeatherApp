package com.weatherapp.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

const val baseUrl = "https://api.openweathermap.org/data/2.5/"
const val apiKey = "651a84ece70db9067eeba410af52bcaf"

@HiltAndroidApp
class App :Application(){}