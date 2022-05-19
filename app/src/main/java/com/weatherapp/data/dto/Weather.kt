package com.weatherapp.data.dto

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)