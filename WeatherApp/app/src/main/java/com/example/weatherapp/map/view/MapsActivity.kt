package com.example.weatherapp.map.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.alerts.view.AlertFragment
import com.example.weatherapp.alerts.view.AlertsActivity

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.weatherapp.databinding.ActivityMapsBinding
import com.example.weatherapp.favorite.views.FavoriteActivity
import com.example.weatherapp.home.view_model.HomeViewModel
import com.example.weatherapp.home.view_model.HomeViewModelFactory
import com.example.weatherapp.home.views.HomeActivity
import com.example.weatherapp.map.view_models.MapViewModel
import com.example.weatherapp.map.view_models.MapViewModelFactory
import com.example.weatherapp.models.RepositoryForLocal
import com.example.weatherapp.models.RepositoryForRemote
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
     var longitude:Double=0.0
     var latitude:Double=0.0
    lateinit var viewModelFactory: MapViewModelFactory

    lateinit var viewModel: MapViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.i("inside","Inside map")
        viewModelFactory= MapViewModelFactory( RepositoryForLocal(this))
        viewModel = ViewModelProvider(this,viewModelFactory).get(MapViewModel::class.java)
        val activityName=intent.getStringExtra("activity name")
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        binding.saveBtn.setOnClickListener{

            when(activityName){
                "home"->{
                    val intent = Intent(this@MapsActivity, HomeActivity::class.java)
                    intent.putExtra("lon",longitude)
                    intent.putExtra("lat",latitude)
                    viewModel.writeToSetting("lat",latitude.toString())
                    viewModel.writeToSetting("long",longitude.toString())

                    Log.i("kjb","lon $longitude lat:$latitude")
                    startActivity(intent)
                }
               /* "fragment"->{
                    val bundle = Bundle()
                    bundle.putDouble("lat",latitude )
                    bundle.putDouble("lon",longitude)
                    val fragobj = AlertFragment()
                    fragobj.arguments=bundle
                    Log.i("kjb","lon $longitude lat:$latitude")
                    val intent = Intent(this@MapsActivity, AlertsActivity::class.java)
                    startActivity(intent)
                }*/
                "favorite"->{
                    val intent = Intent(this@MapsActivity, FavoriteActivity::class.java)
                    intent.putExtra("lon",longitude)
                    intent.putExtra("lat",latitude)
                    Log.i("kjb","lon $longitude lat:$latitude")
                    startActivity(intent)
                }
            }
        /*    if (activityName.equals("home")){
                val intent = Intent(this@MapsActivity, HomeActivity::class.java)
                intent.putExtra("lon",longitude)
                intent.putExtra("lat",latitude)
                viewModel.writeToSetting("lat",latitude.toString())
                viewModel.writeToSetting("long",longitude.toString())

                Log.i("kjb","lon $longitude lat:$latitude")
                startActivity(intent)
            }else{
                val intent = Intent(this@MapsActivity, FavoriteActivity::class.java)
                intent.putExtra("lon",longitude)
                intent.putExtra("lat",latitude)
                Log.i("kjb","lon $longitude lat:$latitude")
                startActivity(intent)
            }*/

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        Log.i("inside","Inside map ready")

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap = googleMap

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
            return
        }
        mMap.isMyLocationEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.addMarker(
            MarkerOptions().position(LatLng(0.0, 0.0)))



        mMap.setOnCameraMoveListener {
            val mPosition = mMap.cameraPosition.target
            mMap.clear()
            mMap.addMarker(
                MarkerOptions()
                    .position(mPosition)
                    .title(mPosition.toString())
            )
            longitude = mPosition.longitude
            latitude = mPosition.latitude
            //getLocation(latitude,longitude)



        }    }


}