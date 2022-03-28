package com.example.weatherapp.models.responseDataModel

data class Weather (
    var id :Int,
    var main: String,
    var description: String,
    var icon: String
)