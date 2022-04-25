package com.example.weatherapp.alerts.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.models.AlertForRoomModel
import com.example.weatherapp.models.LocationModel
import com.example.weatherapp.models.RepositoryForLocal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertViewModel (var repo:RepositoryForLocal):ViewModel(){
    private var allStoredAlerts: MutableLiveData<List<AlertForRoomModel>> = MutableLiveData()
    val myAlerts: LiveData<List<AlertForRoomModel>> get()=allStoredAlerts
    fun insertAlert(alert: AlertForRoomModel){
        CoroutineScope(Dispatchers.IO).launch {
            repo.addAlert(alert)
            allStoredAlerts.postValue(repo.getAllAlerts())
        }
    }
    fun getAllAlert(){
        CoroutineScope(Dispatchers.IO).launch {
            allStoredAlerts.postValue(repo.getAllAlerts())
            Log.i("lknl",repo.getAllAlerts().toString())

        }
    }
    fun readSetting(name:String):String{
        return repo.readSetting(name)
    }

    fun deleteAlert(alert: AlertForRoomModel){
        CoroutineScope(Dispatchers.IO).launch {

            repo.deleteAlert(alert)
            allStoredAlerts.postValue(repo.getAllAlerts())

        }
    }
}