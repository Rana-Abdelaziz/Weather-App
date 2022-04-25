package com.example.weatherapp.worker


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkerParameters
import com.example.weatherapp.R
import androidx.work.CoroutineWorker
import com.example.weatherapp.models.RepositoryForRemote
import com.example.weatherapp.models.responseDataModel.ResponseModel
import com.example.weatherapp.network.Connection

import kotlinx.coroutines.runBlocking


class ReminderWorker(var context : Context, params: WorkerParameters): CoroutineWorker(context,params)
{
    lateinit var r:ResponseModel
    val data = inputData
    val lat=data.getDouble("lat",0.0)
    var language=data.getString("language")
    val long=data.getDouble("Long",0.0)
    val city= data.getString("City")
    val APIKEY:String="1315f8a6652682f67bb9d63b93e6c438"

    fun createNotification(current: Context, channelId: String, notificationId: Int,content:String?) {
        val notification: NotificationCompat.Builder =NotificationCompat.Builder(applicationContext,channelId)
        .setContentTitle("Weather Alert")
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

    override suspend fun doWork(): Result {

        if(Connection.isConnected(context)){
            val repo=RepositoryForRemote()

            runBlocking {
                 r= repo.getWeatherFromRetrofit(lat,long,APIKEY, language!!)
            }
            var content=r.hourly[0].weather[0].description.toString()

            createChannel("channel",context)
            createNotification(context,"channel",1,content)

            return Result.success()
        }else{
            val content="Please connect to the internet to see the current weather is state"
            createChannel("channel",context)
            createNotification(context,"channel",1,content)

           return Result.success()
        }
    }


}