package com.example.weatherapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherapp.models.AlertForRoomModel
import com.example.weatherapp.models.LocationModel
import com.example.weatherapp.models.responseDataModel.ResponseModel

@Dao
interface DataBaseDao {
    @Query("SELECT * From Location")
    fun getAllLocations(): List<LocationModel>
    @Insert
    fun insertLocation(location: LocationModel)

    @Insert
    fun insertResponse(response: ResponseModel)
    @Update
    fun updateResponse(response: ResponseModel)

    @Query("SELECT * From Response")
    fun getResponse(): ResponseModel

    @Query("SELECT * From Alert")
    fun getAllAlerts(): List<AlertForRoomModel>
    @Insert
    fun insertAlerts(alerts: AlertForRoomModel)

    @Delete
    fun deleteAlerts(alerts: AlertForRoomModel)
}