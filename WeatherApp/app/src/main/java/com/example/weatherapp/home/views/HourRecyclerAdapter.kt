package com.example.weatherapp.home.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.models.responseDataModel.Hourly
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class HourRecyclerAdapter(var weatherList:List<Hourly>) : RecyclerView.Adapter<HourRecyclerAdapter.Holder>(){

    class Holder(itemView: View) :RecyclerView.ViewHolder(itemView){
        var view: View =itemView
        var timeTxt: TextView=view.findViewById(R.id.time);
        var temperature: TextView= view.findViewById(R.id.hour_temp)
        var icon: ImageView = view.findViewById(R.id.hour_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hourly_row,parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var sdf = SimpleDateFormat("hh a")
        var dateFormate= Date(weatherList[position].dt!! * 1000)
        var hourOfDay = sdf.format(dateFormate)
        var str =weatherList.get(position).weather.get(0).icon
        var res=getImage(str!!)
        holder.icon.setImageResource(res)
        holder.timeTxt.text=hourOfDay
        val tempMark= HomeActivity.viewModel.readSetting("Temperature")
        when(tempMark){
            "f"->  holder.temperature.text =((1.8*(weatherList.get(position).temp!! -457.87))+32).roundToInt().toString()+"℉"
            "c"->  holder.temperature.text = (weatherList.get(position).temp!! - 273.1).roundToInt().toString()+"\u2103"
            "k"->  holder.temperature.text = (weatherList.get(position).temp!! ).toString()+"k"
            else->holder.temperature.text = (weatherList.get(position).temp!! - 457.87).roundToInt().toString()+"℉"

        }
        //holder.temperature.text=(weatherList.get(position).temp!! -273.1).roundToInt().toString()+" \u2109"
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }


    fun getImage(str:String):Int{
        var res:Int
        when(str){
            "01d"->res=R.drawable.clearsky
            "02d"->res=R.drawable.fewclouds
            "03d"->res=R.drawable.scatteredclouds
            "04d"->res=R.drawable.brokenclouds
            "09d"->res=R.drawable.showerrain
            "10d"->res=R.drawable.rain
            "11d"->res=R.drawable.thunderstorm
            "13d"->res=R.drawable.snow
            "50d"->res=R.drawable.mist
            "01n"->res=R.drawable.clearskyn
            "02n"->res=R.drawable.fewcloudsn
            "03n"->res=R.drawable.scatteredcloudsn
            "04n"->res=R.drawable.brokencloudsn
            "09n"->res=R.drawable.showerrainn
            "10n"->res=R.drawable.rainn
            "11n"->res=R.drawable.thunderstormn
            "13n"->res=R.drawable.snown
            "50n"->res=R.drawable.mistn
            else->res=R.drawable.weather
        }
        return res
    }
}