package com.example.weatherapp.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Location")
data class LocationModel (
    @ColumnInfo(name = "latitude")
    val lat:Double,
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="ID")
    val id:String,
    @ColumnInfo(name = "longitude")
    val lon :Double,
    @ColumnInfo(name = "city")
    val cityName:String)