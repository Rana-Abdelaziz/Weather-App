package com.example.weatherapp.map.view_models

import androidx.lifecycle.ViewModel
import com.example.weatherapp.models.RepositoryForLocal

class MapViewModel( val repositoryForLocal: RepositoryForLocal):ViewModel() {

    fun writeToSetting(name: String,value:String){
        repositoryForLocal.saveSetting(name,value)
    }
}