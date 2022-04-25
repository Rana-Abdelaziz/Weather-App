package com.example.weatherapp.setting.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.home.view_model.HomeViewModel
import com.example.weatherapp.models.RepositoryForLocal
import java.lang.IllegalArgumentException

class SettingViewModelFactory(val repo: RepositoryForLocal): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SettingViewModel::class.java)){
            SettingViewModel(repo) as T
        }
        else{
            throw IllegalArgumentException("This Class Could not br found")
        }    }
}