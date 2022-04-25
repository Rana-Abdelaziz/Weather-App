package com.example.weatherapp.home.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.models.RepositoryForLocal
import com.example.weatherapp.models.RepositoryForRemote
import java.lang.IllegalArgumentException

class HomeViewModelFactory(val repo: RepositoryForRemote,var localRepo:RepositoryForLocal): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(repo,localRepo) as T
        }
        else{
            throw IllegalArgumentException("This Class Could not br found")
        }
    }
}