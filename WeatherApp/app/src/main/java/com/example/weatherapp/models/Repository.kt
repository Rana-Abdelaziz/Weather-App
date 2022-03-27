package com.example.weatherapp.models

import com.example.weatherapp.network.RetrofitClass
import com.example.weatherapp.network.RetrofitInterface

class Repository {
    suspend fun getWeatherFromRetrofit():List<WeatherModel>{
        //////////change URL////////////////
        val URL="https://api.androidhive.info/"
        val retrofitApi: RetrofitInterface = RetrofitClass.getClient(URL)!!.create(
            RetrofitInterface::class.java
        )
        return retrofitApi.getWeather()
    }
}