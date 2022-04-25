package com.example.weatherapp.models.responseDataModel

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Response")
data class ResponseModel (
  @ColumnInfo(name = "latitude")
  @SerializedName("lat"             )
  var lat            : Double?             = null,
  @ColumnInfo(name = "longitude")
  @SerializedName("lon"             )
  var lon            : Double?             = null,
  @ColumnInfo(name = "timezone")
  @PrimaryKey
  @NonNull
  @SerializedName("timezone")
  var timezone       : String,
  @ColumnInfo(name = "timezone_offset")
  @SerializedName("timezone_offset" )
  var timezoneOffset : Int?                = null,
  @Embedded
  @SerializedName("current"         )
  var current        : Current?            = Current(),
  @ColumnInfo(name = "minutely")
  @SerializedName("minutely"        )
  var minutely       : ArrayList<Minutely> = arrayListOf(),
  @ColumnInfo(name = "hourly")
  @SerializedName("hourly"          )
  var hourly         : ArrayList<Hourly>   = arrayListOf(),
  @ColumnInfo(name = "daily")
  @SerializedName("daily"           )
  var daily          : ArrayList<Daily>    = arrayListOf(),
  /*@ColumnInfo(name = "alerts")
  @SerializedName("alerts"          )
  var alerts         : ArrayList<Alerts>   = arrayListOf()*/

)