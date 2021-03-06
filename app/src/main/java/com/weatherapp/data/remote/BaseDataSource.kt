package com.weatherapp.data.remote

import android.util.Log
import com.weatherapp.app.resource.Resource
import retrofit2.Response

//
//
//ALWAYS USE THIS
//
//
@Suppress("UNREACHABLE_CODE")
abstract class BaseDataSource{

    //Function that handles exceptions and return data wrapped in resource
    protected suspend fun <T> getResult(call: suspend () -> Response<T>) : Resource<T> {
        try {
            val response = call()
            Log.e("tag", "${response.isSuccessful} ${response.body()}  ${response.errorBody()}")

            if (response.isSuccessful){
                val body = response.body()
                if(body!=null) return Resource.success(body)
            }
            return error("${response.code()} ${response.message()}")
        } catch (e: Exception){
            return Resource.error(data = null, message = e.message)
        }
    }
}