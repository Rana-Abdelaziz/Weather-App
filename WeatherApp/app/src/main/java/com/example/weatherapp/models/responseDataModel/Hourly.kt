package com.example.weatherapp.models.responseDataModel

data class Hourly (
    var dt :Int,
    var temp :Double,
    var feels_like :Double,
    var pressure :Int,
    var humidity :Int,
    var dew_point :Double,
    var uvi :Double,
    var clouds :Int,
    var visibility :Int,
    var wind_speed :Double,
    var wind_deg :Int,
    var wind_gust :Double,
    var weather: ArrayList<Weather>,
    var pop :Int,
)