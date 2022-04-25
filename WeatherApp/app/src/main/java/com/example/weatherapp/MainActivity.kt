package com.example.weatherapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.home.view_model.HomeViewModel
import com.example.weatherapp.home.view_model.HomeViewModelFactory
import com.example.weatherapp.home.views.HomeActivity
import com.example.weatherapp.map.view.MapsActivity
import com.example.weatherapp.models.RepositoryForLocal
import com.example.weatherapp.models.RepositoryForRemote
import com.google.android.gms.location.*
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var btn: Button

    lateinit var viewModelFactory: HomeViewModelFactory
    companion object{
        lateinit var viewModel: HomeViewModel
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModelFactory= HomeViewModelFactory(RepositoryForRemote(), RepositoryForLocal(this))
        viewModel = ViewModelProvider(this,viewModelFactory).get(HomeViewModel::class.java)
        btn=findViewById(R.id.btn)
        btn.setOnClickListener{
           /* var intent:Intent= Intent(this, MapsActivity::class.java)
            intent.putExtra("activity name","home")
            startActivity(intent)
            finish()*/
            createChannel("id",this)
            newRequestNotification(this,"id",1,"hello your weather is good")
        }
        var state= viewModel.readSetting("userState")
        if (state.equals("Nothing Yet")){
            val intent= Intent(this,DialogActivity::class.java)
            startActivity(intent)
            finish()
        }else{
             val intent= Intent(this,HomeActivity::class.java)
            intent.putExtra("activity name","home")
            startActivity(intent)
            finish()
        }



        /*btn.setOnClickListener {
            Log.d("Debug:",CheckPermission().toString())
            Log.d("Debug:",isLocationEnabled().toString())
            RequestPermission()
            getLastLocation()
        }*/





    }






    fun newRequestNotification(current: Context, channelId: String, notificationId: Int,content:String) {
        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext,channelId)
                .setContentTitle("Weather App")
                .setContentText(content)
                .setSmallIcon(R.drawable.weather)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
        val notificationManager = NotificationManagerCompat.from(current)
        notificationManager.notify(notificationId, notification.build())
    }

    fun createChannel(channelId: String?, context: Context) {
        val name: CharSequence = "Alert"
        val description = "Weather Alert"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            val notificationManager = context.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }




}