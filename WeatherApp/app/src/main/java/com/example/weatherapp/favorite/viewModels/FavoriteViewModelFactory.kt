package com.example.weatherapp.favorite.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.home.view_model.HomeViewModel
import com.example.weatherapp.models.RepositoryForLocal
import com.example.weatherapp.models.RepositoryForRemote
import java.lang.IllegalArgumentException

class FavoriteViewModelFactory(val localRepo: RepositoryForLocal,val remoteRepo:RepositoryForRemote): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)){
            FavoriteViewModel(remoteRepo,localRepo) as T
        }
        else{
            throw IllegalArgumentException("This Class Could not br found")
        }
    }
}