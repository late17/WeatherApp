package com.weatherapp.data.model

data class ThreeHourModel(
    val hour : String,
    val temperature : String,
    val weather : String,
    ) {
    fun areItemsTheSame(newItem : ThreeHourModel): Boolean {
        return (this.hour == newItem.hour)&&(this.weather == newItem.weather)&&(this.hour == newItem.temperature)
    }
}
