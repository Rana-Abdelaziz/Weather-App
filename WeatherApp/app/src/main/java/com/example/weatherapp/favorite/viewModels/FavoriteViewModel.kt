package com.example.weatherapp.favorite.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.models.LocationModel
import com.example.weatherapp.models.RepositoryForLocal
import com.example.weatherapp.models.RepositoryForRemote
import com.example.weatherapp.models.responseDataModel.ResponseModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(val repositoryForRemote: RepositoryForRemote,val localRepository:RepositoryForLocal):ViewModel() {
    private var allStoredLocations: MutableLiveData<List<LocationModel>> = MutableLiveData()
    val myLocations: LiveData<List<LocationModel>> get()=allStoredLocations
    fun insertLocation(location:LocationModel){
        CoroutineScope(Dispatchers.IO).launch {
            localRepository.addLocation(location)
            allStoredLocations.postValue(localRepository.getAllLocations())
        }
    }
    fun getAllLocations(){
        CoroutineScope(Dispatchers.IO).launch {
            allStoredLocations.postValue(localRepository.getAllLocations())

        }
    }

   /* private var respones: MutableLiveData<ResponseModel> = MutableLiveData()
    val myRespones: LiveData<ResponseModel> get()=respones
    fun getWeatherFromApi(lat:Double,lon:Double,key:String){
        CoroutineScope(Dispatchers.IO).launch {
            respones.postValue(repositoryForRemote.getWeatherFromRetrofit(lat,lon,key))


        }
    }*/
}