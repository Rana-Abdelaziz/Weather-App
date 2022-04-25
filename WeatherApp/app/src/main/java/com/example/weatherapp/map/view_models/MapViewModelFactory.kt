package com.example.weatherapp.map.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.models.RepositoryForLocal
import java.lang.IllegalArgumentException

class MapViewModelFactory(var localRepo: RepositoryForLocal): ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MapViewModel::class.java)){
            MapViewModel(localRepo) as T
        }
        else{
            throw IllegalArgumentException("This Class Could not br found")
        }
    }
}