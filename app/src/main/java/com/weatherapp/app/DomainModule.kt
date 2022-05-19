package com.weatherapp.app

import com.weatherapp.app.location.FindLocationClass
import com.weatherapp.data.remote.RemoteDataSource
import com.weatherapp.data.remote.RetrofitApi
import com.weatherapp.data.remote.RetrofitClient
import com.weatherapp.domainmodule.weather.WeatherInterface
import com.weatherapp.domainmodule.weather.WeatherUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DomainModule {
    //
    //----------USE CASES----------
    //
    @Singleton
    @Provides
    fun providesWeatherInterface() : WeatherInterface{
        return WeatherUseCases(providesRemoteDataSource())
    }

    //
    //------------TOOLS------------
    //
    @Singleton
    @Provides
    fun providesFindLocationClass():FindLocationClass{
        return FindLocationClass()
    }
    //
    //-----------REMOTE------------
    //
    @Singleton
    @Provides
    fun providesRemoteDataSource(): RemoteDataSource {
        return RemoteDataSource(providesApiClient())
    }

    @Singleton
    @Provides
    fun providesApiClient(): RetrofitApi {
        return RetrofitClient().getClient()
    }
}