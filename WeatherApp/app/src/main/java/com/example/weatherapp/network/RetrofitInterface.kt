package com.example.weatherapp.network

import com.example.weatherapp.models.responseDataModel.ResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {
    @GET("onecall")
    suspend fun getWeather(
    @Query ("lat") lat:Double,
    @Query("lon") lon:Double,
    @Query("appid")apiKey:String,@Query("lang")lang:String): ResponseModel

}