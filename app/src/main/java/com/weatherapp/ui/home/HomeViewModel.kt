package com.weatherapp.ui.home

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

    private var _coordinates = MutableStateFlow<Resource<Coordinates>>(Resource.pending())

    val coordinates: StateFlow<Resource<Coordinates>>
        get() = _coordinates

    private var _fiveDayWeather = MutableStateFlow<Resource<FiveDayWeather>>(Resource.pending())

    val fiveDayWeather: StateFlow<Resource<FiveDayWeather>>
        get() = _fiveDayWeather

    fun loadData() = viewModelScope.launch {
        _fiveDayWeather.value = Resource.loading()
        coordinates.value.data?.let {
            _fiveDayWeather.value = weatherInterface.getFiveDayWeather(it)
        }
    }

    fun updateCoordinates(coordinates: Coordinates) {
        _coordinates.value = Resource.success(coordinates)
    }

    fun deleteCoordinates() {
        _coordinates.value = Resource.pending()
    }
}