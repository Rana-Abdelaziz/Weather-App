package com.example.weatherapp.models.responseDataModel

import com.example.weatherapp.models.AllModels

data class Current (
    var dt :Int,
    var sunrise :Int,
    var sunset :Int,
    var temp :Double,
    var feels_like :Double,
    var pressure :Int,
    var humidity :Int,
    var dew_point :Double,
    var uvi :Double,
    var clouds :Int,
    var visibility :Int,
    var wind_speed :Int,
    var wind_deg :Int,
    var weather: ArrayList<Weather>,
    var rain: Rain
)