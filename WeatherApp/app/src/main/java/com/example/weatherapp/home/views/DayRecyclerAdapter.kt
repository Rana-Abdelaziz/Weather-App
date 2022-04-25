package com.example.weatherapp.home.views

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.models.responseDataModel.Daily
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class DayRecyclerAdapter(var weatherList:List<Daily>) : RecyclerView.Adapter<DayRecyclerAdapter.Holder>() {
 var t:Long=0
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var view: View =itemView
        var descriptionText: TextView =view.findViewById(R.id.day_desc);
        var dayName: TextView = view.findViewById(R.id.day_name)
        var dayTemp: TextView = view.findViewById(R.id.day_temp)
        var dayIcon: ImageView = view.findViewById(R.id.day_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.daily_row,parent,false)
        return DayRecyclerAdapter.Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var sdf = SimpleDateFormat("EEEE")
        t=weatherList.get(position).dt!!
        var dateFormat = Date(t*1000)
        Log.i("kmlds","${weatherList.get(position).dt}")
        var weekday: String = sdf.format(dateFormat)
        Log.i("kmlds","$weekday")

        holder.dayName.text=weekday
        var str =weatherList.get(position).weather.get(0).icon
        var res=getImage(str!!)
        holder.dayIcon.setImageResource(res)

        val tempMark= HomeActivity.viewModel.readSetting("Temperature")
        when(tempMark){

            "f"->  holder.dayTemp.text =((1.8*(weatherList.get(position).temp!!.day!! -457.87))+32).roundToInt().toString()+"℉"
            "c"->  holder.dayTemp.text = (weatherList.get(position).temp!!.day!! - 273.1).roundToInt().toString()+"\u2103"
            "k"->  holder.dayTemp.text = (weatherList.get(position).temp!!.day ).toString()+"k"
            else->holder.dayTemp.text = (weatherList.get(position).temp!!.day!! - 457.87).roundToInt().toString()+"℉"

        }
        //holder.dayTemp.text=(weatherList.get(position).temp!!.day!! -273.15).roundToInt().toString()+" \u2109"
        holder.descriptionText.text=weatherList.get(position).weather.get(0).description

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
            else->res=R.drawable.weather
        }
       return res
    }
}