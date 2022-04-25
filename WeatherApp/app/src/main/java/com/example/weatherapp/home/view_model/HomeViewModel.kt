package com.example.weatherapp.home.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.R
import com.example.weatherapp.models.RepositoryForLocal
import com.example.weatherapp.models.RepositoryForRemote
import com.example.weatherapp.models.responseDataModel.ResponseModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(val repositoryForRemote: RepositoryForRemote,val repositoryForLocal:RepositoryForLocal):ViewModel() {
    private var respones: MutableLiveData<ResponseModel> = MutableLiveData()
    val myRespones: LiveData<ResponseModel> get()=respones


    private var responesForLocal: MutableLiveData<ResponseModel> = MutableLiveData()
    val myResponesForLocal: LiveData<ResponseModel> get()=responesForLocal
    fun getWeatherFromApi(lat:Double,lon:Double,key:String,lang:String){
        CoroutineScope(Dispatchers.IO).launch {
            respones.postValue(repositoryForRemote.getWeatherFromRetrofit(lat,lon,key,lang))

        }
    }
    fun readSetting(name:String):String{
       return repositoryForLocal.readSetting(name)
    }
    fun writeToSetting(name: String,value:String){
        repositoryForLocal.saveSetting(name,value)
    }

    fun getImage(str:String):Int{
        var res:Int
        when(str){
            "01d"->res= R.drawable.clearsky
            "02d"->res= R.drawable.fewclouds
            "03d"->res= R.drawable.scatteredclouds
            "04d"->res= R.drawable.brokenclouds
            "09d"->res= R.drawable.showerrain
            "10d"->res= R.drawable.rain
            "11d"->res= R.drawable.thunderstorm
            "13d"->res= R.drawable.snow
            "50d"->res= R.drawable.mist
            else->res= R.drawable.weather
        }
        return res
    }

    suspend fun insertResponse(responseModel: ResponseModel){
        repositoryForLocal.insertResponse(responseModel)
    }
    suspend fun updateResponse(responseModel: ResponseModel){
        repositoryForLocal.updateResponse(responseModel)
    }
    suspend fun getResponse(){
        responesForLocal.postValue(repositoryForLocal.getResponse())

    }
}