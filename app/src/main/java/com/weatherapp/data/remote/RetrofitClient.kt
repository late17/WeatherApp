package com.weatherapp.data.remote

import com.weatherapp.app.apiKey
import com.weatherapp.app.baseUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    fun getClient(): RetrofitApi {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor() { chain ->
                val request = chain.request().newBuilder()
                val originalHttpUrl = chain.request().url
                val url =
                    originalHttpUrl.newBuilder().addQueryParameter("appid", apiKey)
                        .build()
                request.url(url)
                return@addInterceptor chain.proceed(request.build())
            }
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitApi::class.java)

    }
}