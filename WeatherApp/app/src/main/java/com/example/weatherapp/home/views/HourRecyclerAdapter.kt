package com.example.weatherapp.home.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.models.WeatherModel

class HourRecyclerAdapter(var weatherList:List<WeatherModel>) : RecyclerView.Adapter<HourRecyclerAdapter.Holder>(){

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
        /////////////////update here////////////////////////
        holder.icon.setImageResource(weatherList.get(position).icon)
        holder.timeTxt.text=weatherList.get(position).state
        holder.temperature.text=weatherList.get(position).temp
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }
}