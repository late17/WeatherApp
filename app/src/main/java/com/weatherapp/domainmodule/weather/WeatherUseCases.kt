package com.weatherapp.domainmodule.weather

import com.weatherapp.app.resource.Resource
import com.weatherapp.data.dto.FiveDayWeather
import com.weatherapp.data.model.Coordinates
import com.weatherapp.data.remote.RemoteDataSource
import javax.inject.Inject

class WeatherUseCases @Inject constructor(private val remoteDataSource: RemoteDataSource) : WeatherInterface {
    override suspend fun getFiveDayWeather(coordinates: Coordinates): Resource<FiveDayWeather> {
        return remoteDataSource.getFiveDayWeather(coordinates)
    }
}