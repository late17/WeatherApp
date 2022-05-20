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

    suspend fun getFiveDayWeather(latitude: Double, longitude : Double)
        = getResult { retrofitApi.getFiveDayWeather(latitude, longitude) }

}