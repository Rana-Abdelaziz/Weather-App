package com.example.weatherapp.models.responseDataModel

import com.example.weatherapp.models.AllModels
import com.example.weatherapp.models.Minutely

data class ResponseModel  (
    var lat :Double,
    var lon :Double,
    var timezone: String,
    var timezone_offset :Int,
    var current: Current,
    var minutely: ArrayList<Minutely>,
    var hourly: ArrayList<Hourly>,
    var daily: ArrayList<Daily>,
    var alerts: ArrayList<Alert>,
)