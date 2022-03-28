package com.example.weatherapp.models.responseDataModel

data class Alert (
    var sender_name: String,
    var event: String,
    var start :Int,
    var end :Int,
    var description: String,
    var tags: ArrayList<String>
)