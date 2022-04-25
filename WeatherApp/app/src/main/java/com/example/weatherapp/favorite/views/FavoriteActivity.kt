package com.example.weatherapp.favorite.views

import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.ActivityFavoriteBinding
import com.example.weatherapp.map.view.MapsActivity
import com.example.weatherapp.favorite.viewModels.FavoriteViewModel
import com.example.weatherapp.favorite.viewModels.FavoriteViewModelFactory
import com.example.weatherapp.models.LocationModel
import com.example.weatherapp.models.RepositoryForLocal
import com.example.weatherapp.models.RepositoryForRemote
import java.io.IOException
import java.util.*

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    lateinit var viewModelFactory: FavoriteViewModelFactory
    lateinit var viewModel: FavoriteViewModel

    lateinit var myAdapter: FavoriteRecyclerAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var locationList:List<LocationModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.addFavorite.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("activity name","favorite")
            startActivity(intent)
        }
        locationList= listOf()
        layoutManager=LinearLayoutManager(this)
        layoutManager.orientation= RecyclerView.VERTICAL
        myAdapter= FavoriteRecyclerAdapter(locationList,this)

        binding.favoriteRecyclerView.adapter=myAdapter
        binding.favoriteRecyclerView.layoutManager=layoutManager

        viewModelFactory= FavoriteViewModelFactory( RepositoryForLocal(this),RepositoryForRemote())
        viewModel= ViewModelProvider(this,viewModelFactory).get(FavoriteViewModel::class.java)
    }

    override fun onStart() {
       viewModel.getAllLocations()
        viewModel.myLocations.observe(this){
            l->
            if(l!=null){
                myAdapter.locationList=l
                myAdapter.notifyDataSetChanged()
            }
        }

        var long=intent.getDoubleExtra("lon",0.0)
        var lati=intent.getDoubleExtra("lat",0.0)
        //Toast.makeText(this,"lon:$long lat:$lati", Toast.LENGTH_SHORT).show()
        var city=getLocation(lati,long)
        val id = UUID.randomUUID().toString().toUpperCase()

        var loc=LocationModel(lati,id,long,city)
        if(lati!=0.0&&long!=0.0){

            viewModel.insertLocation(loc)
        }

        super.onStart()
    }

    fun getLocation(lat: Double, lon: Double):String {
        val geocoder = Geocoder(this, Locale.getDefault())
        var city:String=""
        try {
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if(addresses.size!=0){
                city = addresses[0].locality
                val country = addresses[0].countryName
                Toast.makeText(this,    "city is$city country is$country", Toast.LENGTH_SHORT).show()
            }


        } catch (e: IOException) {
            e.printStackTrace()
        }

        return city
    }
}