package com.weatherapp.data.dto

data class FiveDayWeather(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<ThreeHourWeather>,
    val message: Int
)