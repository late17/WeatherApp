package com.weatherapp.data.remote

import com.weatherapp.data.model.Coordinates
import javax.inject.Inject

//
//
//ALWAYS USE THIS
//
//

class RemoteDataSource @Inject constructor(private val retrofitApi: RetrofitApi) :
    BaseDataSource() {

    suspend fun getFiveDayWeather(coordinates: Coordinates)
        = getResult { retrofitApi.getFiveDayWeather(coordinates.latitude, coordinates.longitude) }

}