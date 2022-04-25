package com.example.weatherapp.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Alert")
data class AlertForRoomModel(
    @ColumnInfo(name = "City Name")
    @NonNull
    var cityName:String,
    @PrimaryKey
    @NonNull
    var id:String,
    @ColumnInfo(name = "latitude")
    var latitude:Double,
    @ColumnInfo(name = "longitude")
    var longitude:Double,
    @ColumnInfo(name = "Time")
    var time:String )