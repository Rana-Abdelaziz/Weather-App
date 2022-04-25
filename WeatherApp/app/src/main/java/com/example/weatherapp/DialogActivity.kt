package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.weatherapp.databinding.ActivityDialogBinding
import com.example.weatherapp.home.views.HomeActivity
import com.example.weatherapp.map.view.MapsActivity
import com.google.android.gms.location.*
import java.util.*

class DialogActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDialogBinding
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val PERMISSION_ID=223
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        binding.gpsBtn.setOnClickListener{
            MainActivity.viewModel.writeToSetting("Location","gps")
            MainActivity.viewModel.writeToSetting("userState","exist")
            RequestPermission()
            getLastLocation()

        }
        binding.mapBtn.setOnClickListener{
            MainActivity.viewModel.writeToSetting("Location","map")
            MainActivity.viewModel.writeToSetting("userState","exist")
            val intent= Intent(this, MapsActivity::class.java)
            intent.putExtra("activity name","home")
            startActivity(intent)
        }
    }




    fun CheckPermission():Boolean{
        //this function will return a boolean
        //true: if we have permission
        //false if not
        if(
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }

        return false

    }
    fun RequestPermission(){
        //this function will allows us to tell the user to requesut the necessary permsiion if they are not garented
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }
    fun isLocationEnabled():Boolean{
        //this function will return to us the state of the location service
        //if the gps or the network provider is enabled then it will return true otherwise it will return false
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }
    @SuppressLint("MissingPermission")
    fun getLastLocation(){
        if(CheckPermission()){
            if(isLocationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {task->
                    var location: Location? = task.result
                    if(location == null){
                        NewLocationData()
                    }else{
                        Log.d("Debug:" ,"Your Location:"+ location.longitude)
                        Log.d("Debug:" ,"Your Location:"+ location.latitude)

                        val intent= Intent(this,HomeActivity::class.java)
                        intent.putExtra("lon",location.longitude)
                        intent.putExtra("lat",location.latitude)
                        startActivity(intent)
                    }
                }
            }else{
                Toast.makeText(this,"Please Turn on Your device Location", Toast.LENGTH_SHORT).show()
            }
        }else{
            RequestPermission()
        }
    }


    @SuppressLint("MissingPermission")
    fun NewLocationData(){
        var locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()!!
        )
    }

    /*private fun getCityName(lat: Double,long: Double):String{

        var geoCoder = Geocoder(this, Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat,long,3)

        val cityName = Adress.get(0).locality
        val countryName = Adress.get(0).countryName
        Log.d("Debug:","Your City: " + cityName + " ; your Country " + countryName)
        return cityName
    }*/


    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            val intent= Intent(this@DialogActivity,HomeActivity::class.java)
            intent.putExtra("lon",lastLocation.longitude)
            intent.putExtra("lat",lastLocation.latitude)
            startActivity(intent)
            Log.d("Debug:","your last last location: "+ lastLocation.longitude.toString())
        }
    }
}