package com.example.weatherapp.models

import com.example.weatherapp.models.responseDataModel.ResponseModel
import com.example.weatherapp.network.RetrofitClass
import com.example.weatherapp.network.RetrofitInterface

class RepositoryForRemote {
    suspend fun getWeatherFromRetrofit(lat:Double,lon:Double,key:String,lang:String):ResponseModel{
        //////////change URL////////////////
        val URL="https://api.openweathermap.org/data/2.5/"
        val retrofitApi: RetrofitInterface = RetrofitClass.getClient(URL)!!.create(
            RetrofitInterface::class.java
        )
        return retrofitApi.getWeather(lat,lon,key,lang)
    }


}