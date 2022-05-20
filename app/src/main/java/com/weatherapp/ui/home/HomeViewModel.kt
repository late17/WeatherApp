package com.weatherapp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weatherapp.app.resource.Resource
import com.weatherapp.data.dto.FiveDayWeather
import com.weatherapp.data.model.Coordinates
import com.weatherapp.domainmodule.weather.WeatherInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val weatherInterface: WeatherInterface) :
    ViewModel() {

     val errorText: String
        get() = errorText()

    private fun errorText(): String {
        return when(fiveDayWeather.value.status){
            Resource.Status.SUCCESS -> ""
            Resource.Status.LOADING -> ""
            Resource.Status.FAILED -> "Connection Problem"
            Resource.Status.PENDING -> "Please enter city or enable GPS"
        }
    }

    private var _coordinates = MutableStateFlow<Resource<Coordinates>>(Resource.pending())

    private val coordinates: StateFlow<Resource<Coordinates>>
        get() = _coordinates

    private var _fiveDayWeather = MutableStateFlow<Resource<FiveDayWeather>>(Resource.pending())

    val fiveDayWeather: StateFlow<Resource<FiveDayWeather>>
        get() = _fiveDayWeather

    fun loadData() = viewModelScope.launch {
        _fiveDayWeather.value = Resource.loading()
        coordinates.value.data?.let {
            val asdf = weatherInterface.getFiveDayWeather(it)
            _fiveDayWeather.value = asdf
        }
    }

    fun updateCoordinates(coordinates: Coordinates) {
        _coordinates.value = Resource.success(coordinates)
    }

    fun deleteCoordinates() {
        _coordinates.value = Resource.pending()
    }
}