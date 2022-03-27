package com.example.weatherapp.network

import com.example.weatherapp.models.WeatherModel
import retrofit2.http.GET

interface RetrofitInterface {
    ////////////change url///////////
    @GET("json/movies.json")
    suspend fun getWeather(): List<WeatherModel>
}