package com.weatherapp.data.remote

import com.weatherapp.data.dto.FiveDayWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {

    @GET("forecast?")
    suspend fun getFiveDayWeather(
        @Query("lat")
        latitude : String,
        @Query("lon")
        longitude : String,
    ):Response<FiveDayWeather>

}