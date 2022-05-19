package com.weatherapp.app.mappers

import com.weatherapp.data.dto.ThreeHourWeather
import com.weatherapp.data.model.ThreeHourModel

fun ThreeHourWeather.toThreeHourModel() : ThreeHourModel{
    return ThreeHourModel(
        hour = dateToHour(this.dt_txt),
        temperature = this.main.temp.kelvinToCelsiusString(),
        weather = this.weather[0].main)
}

fun dateToHour(string: String): String {
    val split = string.split(" ")
    if (split.size==2){
        return split[1]
    }
    return ""
}

fun Double.kelvinToCelsiusString(): String {
    return (this - 273.15).toInt().toString()
}