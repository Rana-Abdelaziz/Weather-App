package com.example.weatherapp.models

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weatherapp.database.DataBase
import com.example.weatherapp.database.DataBaseDao
import android.content.Context.MODE_PRIVATE

import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE
import android.content.Context.MODE_PRIVATE
import com.example.weatherapp.models.responseDataModel.ResponseModel


class RepositoryForLocal(var context: Context) {
    var db= DataBase.getInstance(context)
    var dataBaseDao:DataBaseDao= db!!.dataBaseDao()
    suspend fun addLocation(location:LocationModel) {
        dataBaseDao.insertLocation(location)
        Log.i("lknl","successfully Added to room")

    }

    suspend fun getAllLocations():List<LocationModel>{
        var list=dataBaseDao.getAllLocations()
        return list
    }
    suspend fun addAlert(alert:AlertForRoomModel) {
        dataBaseDao.insertAlerts(alert)
        Log.i("lknl","successfully Added to room")

    }

    suspend fun getAllAlerts():List<AlertForRoomModel>{
        var list=dataBaseDao.getAllAlerts()
        return list
    }
    suspend fun deleteAlert(alert:AlertForRoomModel){
        dataBaseDao.deleteAlerts(alert)

    }

    fun saveSetting(name:String,value:String){
        val sharedPrefs="Setting";

        val write: SharedPreferences = context.getSharedPreferences(sharedPrefs, MODE_PRIVATE)
        val editor = write.edit()
        editor.putString(name, value)
        editor.commit()

    }
    fun readSetting(name:String):String
    {
        val sharedPrefs="Setting";
        val read: SharedPreferences = context.getSharedPreferences(sharedPrefs, MODE_PRIVATE)
        val res=read.getString(name, "Nothing Yet")
        return res!!
    }

    suspend fun getResponse():ResponseModel{
        return dataBaseDao.getResponse()
    }

    suspend fun insertResponse(responseModel: ResponseModel){
        dataBaseDao.insertResponse(responseModel)
    }

    suspend fun updateResponse(responseModel: ResponseModel){
        dataBaseDao.updateResponse(responseModel)
    }
}