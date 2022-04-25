package com.example.weatherapp.home.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.home.view_model.HomeViewModel
import com.example.weatherapp.home.view_model.HomeViewModelFactory
import com.example.weatherapp.models.RepositoryForRemote
import com.example.weatherapp.models.responseDataModel.Daily
import com.example.weatherapp.models.responseDataModel.Hourly
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import com.example.weatherapp.favorite.views.FavoriteActivity
import com.example.weatherapp.setting.view.SettingActivity
import com.example.weatherapp.alerts.view.AlertsActivity
import com.example.weatherapp.databinding.ActivityHomeBinding
import com.example.weatherapp.models.RepositoryForLocal
import com.example.weatherapp.network.Connection
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt


class HomeActivity : AppCompatActivity(){
    private lateinit var binding: ActivityHomeBinding

    lateinit var hourRecyclerView: RecyclerView
    lateinit var hourRecyclerAdapter:HourRecyclerAdapter
    lateinit var hourLayoutManager: LinearLayoutManager
    lateinit var hourlyWeatherList:List<Hourly>
    var language=""
    var languageForQuery:String=""



    lateinit var dayRecyclerView: RecyclerView
    lateinit var dayRecyclerAdapter:DayRecyclerAdapter
    lateinit var dayLayoutManager: LinearLayoutManager
    lateinit var dailyWeatherList:List<Daily>

    val APIKEY:String="1315f8a6652682f67bb9d63b93e6c438"
    lateinit var toggel:ActionBarDrawerToggle
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    val PERMISSION_ID=223
    lateinit var viewModelFactory: HomeViewModelFactory
    companion object{
        lateinit var viewModel:HomeViewModel
        var long:Double=0.0
        var lati:Double=0.0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toggel=ActionBarDrawerToggle(this,binding.drawerLayout,binding.toolBar,R.string.open,R.string.close)
        binding.drawerLayout.addDrawerListener(toggel)
        toggel.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.color.white)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        viewModelFactory= HomeViewModelFactory(RepositoryForRemote(), RepositoryForLocal(this))
        viewModel= ViewModelProvider(this,viewModelFactory).get(HomeViewModel::class.java)


        hourRecyclerView=findViewById(R.id.hour_recyclerView)
        dayRecyclerView=findViewById(R.id.daily_recyclerView)
        hourlyWeatherList= listOf()
        dailyWeatherList= listOf()
        dayRecyclerAdapter= DayRecyclerAdapter(dailyWeatherList)
        dayLayoutManager= LinearLayoutManager(this)
        dayLayoutManager.orientation=RecyclerView.VERTICAL
        dayRecyclerView.layoutManager=dayLayoutManager
        dayRecyclerView.adapter=dayRecyclerAdapter


        hourRecyclerAdapter=HourRecyclerAdapter(hourlyWeatherList)
        hourLayoutManager= LinearLayoutManager(this)
        hourLayoutManager.orientation=RecyclerView.HORIZONTAL
        hourRecyclerView.layoutManager=hourLayoutManager
        hourRecyclerView.adapter=hourRecyclerAdapter
        viewModel.myResponesForLocal.observe(this){
                w ->
            if (w != null) {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses: List<Address> = geocoder.getFromLocation(w.lat!!, w.lon!!, 1)
                if(addresses.isEmpty()){
                    val cityName= intent.getStringExtra("city")
                    binding.cityName.text = cityName

                }else{
                    val cityName: String = addresses[0].locality
                    binding.cityName.text = cityName
                }

                val d: Date = Date()
                binding.date.text = d.toString()
                var res = viewModel.getImage(w.current!!.weather[0].icon!!)
                binding.currentIcon.setImageResource(res)
                binding.cloudTxt.text = "${w.current!!.clouds.toString()} %"
                val windSpeed=viewModel.readSetting("WindSpeed")
                when(windSpeed){
                    "m/h"-> binding.windTxt.text = "${w.current!!.windSpeed!! *2.236936} m/h"
                    "m/s"-> binding.windTxt.text = "${w.current!!.windSpeed.toString()} m/s"
                    else->binding.windTxt.text = "${w.current!!.windSpeed.toString()} m/s"

                }
                binding.humidityTxt.text = "${w.current!!.humidity.toString()} %"
                binding.pressureTxt.text = "${w.current!!.pressure.toString()} hpa"
                binding.currentDesc.text = w.current!!.weather[0].description
                val tempMark= viewModel.readSetting("Temperature")
                when(tempMark){
                    "f"->  binding.currentTemp.text =((1.8*(w.current!!.temp!! -457.87))+32).roundToInt().toString()+"℉"
                    "c"-> binding.currentTemp.text = (w.current!!.temp!! - 273.1).roundToInt().toString()+"\u2103"
                    "k"-> binding.currentTemp.text = (w.current!!.temp!! ).toString()+"k"
                    else->binding.currentTemp.text = (w.current!!.temp!! - 457.87).roundToInt().toString()+"℉"

                }

                hourRecyclerAdapter.weatherList = w.hourly.subList(1, 13)
                hourRecyclerAdapter.notifyDataSetChanged()

                dayRecyclerAdapter.weatherList = w.daily.subList(1, 7)
                dayRecyclerAdapter.notifyDataSetChanged()
            }
        }

        viewModel.myRespones.observe(this) { w ->
            if (w != null) {
                if(binding.homeTitle.text!="favorite"){
                    if(viewModel.readSetting("respones")=="Nothing Yet"){
                        CoroutineScope(Dispatchers.IO).launch {
                            viewModel.insertResponse(w)
                            viewModel.writeToSetting("respones","Exist")
                        }
                    }else{
                        CoroutineScope(Dispatchers.IO).launch {
                            viewModel.updateResponse(w)
                        }
                    }
                }

                Log.i("jdnk","inside Observer")
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses: List<Address> = geocoder.getFromLocation(w.lat!!, w.lon!!, 1)
                if(addresses.isEmpty()){
                    val cityName= intent.getStringExtra("city")
                    binding.cityName.text = cityName

                }else{
                    val cityName: String = addresses[0].locality
                    binding.cityName.text = cityName
                }

                val d: Date = Date()
                binding.date.text = d.toString()
                var res = viewModel.getImage(w.current!!.weather[0].icon!!)
                binding.currentIcon.setImageResource(res)
                binding.cloudTxt.text = "${w.current!!.clouds.toString()} %"
                binding.windTxt.text = "${w.current!!.windSpeed.toString()} m/s"
                binding.humidityTxt.text = "${w.current!!.humidity.toString()} %"
                binding.pressureTxt.text = "${w.current!!.pressure.toString()} hpa"
                binding.currentDesc.text = w.current!!.weather[0].description
                val tempMark= viewModel.readSetting("Temperature")
                when(tempMark){
                    "f"->  binding.currentTemp.text =((1.8*(w.current!!.temp!! -457.87))+32).roundToInt().toString()+"℉"
                    "c"-> binding.currentTemp.text = (w.current!!.temp!! - 273.1).roundToInt().toString()+"\u2103"
                    "k"-> binding.currentTemp.text = (w.current!!.temp!! ).toString()+"\u2109"
                    else->binding.currentTemp.text = (w.current!!.temp!! - 457.87).roundToInt().toString()+"℉"

                }

                hourRecyclerAdapter.weatherList = w.hourly.subList(1, 13)
                hourRecyclerAdapter.notifyDataSetChanged()

                dayRecyclerAdapter.weatherList = w.daily.subList(1, 7)
                dayRecyclerAdapter.notifyDataSetChanged()
            }

        }


        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.setting -> {
                    val intent = Intent(this@HomeActivity, SettingActivity::class.java)
                    startActivity(intent)
                }
                R.id.favorite -> {
                    val intent = Intent(this@HomeActivity, FavoriteActivity::class.java)
                    startActivity(intent)
                }
                R.id.alerts -> {
                    val intent = Intent(this@HomeActivity, AlertsActivity::class.java)
                    startActivity(intent)
                }
                R.id.home -> {
                    if(binding.homeTitle.text.equals("Favorite")){
                        if (language=="arabic"){
                            viewModel.getWeatherFromApi(lati,long,APIKEY,"ar")

                        }else{
                            viewModel.getWeatherFromApi(lati,long,APIKEY,"en")

                        }
                        binding.homeTitle.text=getResources().getString(R.string.home)
                        /*val intent = Intent(this@HomeActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()*/
                    }else{
                        binding.drawerLayout.close()
                        //binding.drawerLayout.closeDrawer()

                    }
                }

            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggel.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onStart() {

         language=viewModel.readSetting("Language")
        if(language == "arabic"){
            languageSettings(1)
            languageForQuery="ar"
        }else{
            languageSettings(0)
            languageForQuery="en"
        }


        var flag=intent.getStringExtra("flag")
        if (flag.equals("favorite")){
            var long=intent.getDoubleExtra("lon",0.0)
            var lati=intent.getDoubleExtra("lat",0.0)
            val city=intent.getStringExtra("city")
            viewModel.getWeatherFromApi(lati,long,APIKEY,languageForQuery)
            binding.cityName.text=city
            binding.homeTitle.text= getResources().getString(R.string.favorite)


        }else{
            if(Connection.isConnected(this)) {
                var locationSetting= viewModel.readSetting("Location")
                if(locationSetting.equals("map")){

                    lati= viewModel.readSetting("lat").toDouble()
                    long= viewModel.readSetting("long").toDouble()
                    viewModel.getWeatherFromApi(lati, long, APIKEY,languageForQuery)

                }
                else{
                    RequestPermission()
                    getLastLocation()
                }
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                viewModel.getResponse()
                }

                Toast.makeText(this,"you are not connected", Toast.LENGTH_SHORT).show()
            }
        }



        super.onStart()
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
                        lati=location.latitude
                        long=location.longitude
                        viewModel.getWeatherFromApi(location.latitude,location.longitude,APIKEY,languageForQuery)
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

    fun languageSettings(languageValue:Int) {
        val config = resources.configuration
        val lang = when (languageValue) {
            0 -> "en"
            1 -> "ar"
            else -> "en"
        }
        var locale = Locale(lang)
        Locale.setDefault(locale)
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            this.createConfigurationContext(config)
        }

         if (TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL) {
             this.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
         } else {
             this.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
         }

    }



    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            lati=lastLocation.latitude
            long=lastLocation.longitude
            viewModel.getWeatherFromApi(lastLocation.latitude,lastLocation.longitude,APIKEY,languageForQuery)

            Log.d("Debug:","your last last location: "+ lastLocation.longitude.toString())
        }
    }
}