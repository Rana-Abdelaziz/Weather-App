package com.example.weatherapp.home.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.models.Repository
import com.example.weatherapp.models.WeatherModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(val repository: Repository):ViewModel() {
    private var weatherList: MutableLiveData<List<WeatherModel>> = MutableLiveData()
    val list: LiveData<List<WeatherModel>> get()=weatherList

    fun getWeatherFromApi(){
        CoroutineScope(Dispatchers.IO).launch {
            weatherList.postValue(repository.getWeatherFromRetrofit())
        }
    }
}