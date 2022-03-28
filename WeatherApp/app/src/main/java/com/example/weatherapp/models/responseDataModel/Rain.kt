package com.example.weatherapp.models.responseDataModel

import com.google.gson.annotations.SerializedName

data class Rain (
    @SerializedName("1h")
    var _1h:Double = 0.0
)