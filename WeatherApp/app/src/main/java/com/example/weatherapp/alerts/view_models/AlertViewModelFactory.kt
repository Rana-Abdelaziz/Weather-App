package com.example.weatherapp.alerts.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.home.view_model.HomeViewModel
import com.example.weatherapp.models.RepositoryForLocal
import java.lang.IllegalArgumentException

class AlertViewModelFactory(var repo: RepositoryForLocal): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)){
            AlertViewModel(repo) as T
        }
        else{
            throw IllegalArgumentException("This Class Could not br found")
        }    }
}