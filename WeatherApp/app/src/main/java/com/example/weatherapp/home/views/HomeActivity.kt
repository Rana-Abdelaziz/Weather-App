package com.example.weatherapp.home.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.home.view_model.HomeViewModel
import com.example.weatherapp.home.view_model.HomeViewModelFactory
import com.example.weatherapp.models.Repository
import com.example.weatherapp.models.WeatherModel

class HomeActivity : AppCompatActivity() {
    lateinit var hourRecyclerView: RecyclerView
    lateinit var hourRecyclerAdapter:HourRecyclerAdapter
    lateinit var dayRecyclerView: RecyclerView
    lateinit var dayRecyclerAdapter:DayRecyclerAdapter
    lateinit var dayLayoutManager: LinearLayoutManager
    lateinit var weatherList:List<WeatherModel>
    lateinit var hourLayoutManager: LinearLayoutManager
    lateinit var viewModelFactory: HomeViewModelFactory

    companion object{
        lateinit var viewModel:HomeViewModel
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        viewModelFactory= HomeViewModelFactory(Repository())
        viewModel= ViewModelProvider(this,viewModelFactory).get(HomeViewModel::class.java)
        hourRecyclerView=findViewById(R.id.hour_recyclerView)
        dayRecyclerView=findViewById(R.id.daily_recyclerView)
        weatherList= listOf()

        dayRecyclerAdapter= DayRecyclerAdapter(weatherList)
        dayLayoutManager= LinearLayoutManager(this)
        dayLayoutManager.orientation=RecyclerView.VERTICAL
        dayRecyclerView.layoutManager=dayLayoutManager
        dayRecyclerView.adapter=dayRecyclerAdapter


        hourRecyclerAdapter=HourRecyclerAdapter(weatherList)
        hourLayoutManager= LinearLayoutManager(this)
        hourLayoutManager.orientation=RecyclerView.HORIZONTAL
        hourRecyclerView.layoutManager=hourLayoutManager
        hourRecyclerView.adapter=hourRecyclerAdapter




        viewModel.getWeatherFromApi()
        viewModel.list.observe(this,){
                w->
            if (w!=null){
                hourRecyclerAdapter.weatherList=w
                hourRecyclerAdapter.notifyDataSetChanged()

                dayRecyclerAdapter.weatherList=w
                dayRecyclerAdapter.notifyDataSetChanged()
            }

        }
    }
}