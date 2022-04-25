package com.example.weatherapp.setting.viewModel

import androidx.lifecycle.ViewModel
import com.example.weatherapp.models.RepositoryForLocal

class SettingViewModel(val localRepository: RepositoryForLocal): ViewModel() {
    fun readFromSharedPreference(name: String):String{
        val res=localRepository.readSetting(name)
        return res
    }
    fun writeToSharedPreference(name:String,value:String){
        localRepository.saveSetting(name,value)
    }

}