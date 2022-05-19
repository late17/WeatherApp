package com.weatherapp.domainmodule.weather

import com.weatherapp.app.resource.Resource
import com.weatherapp.data.dto.FiveDayWeather
import com.weatherapp.data.model.Coordinates

interface WeatherInterface {

    suspend fun getFiveDayWeather(coordinates: Coordinates) : Resource<FiveDayWeather>

}