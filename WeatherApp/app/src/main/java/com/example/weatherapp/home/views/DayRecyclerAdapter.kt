package com.example.weatherapp.home.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.models.WeatherModel

class DayRecyclerAdapter(var weatherList:List<WeatherModel>) : RecyclerView.Adapter<DayRecyclerAdapter.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var view: View =itemView
        var descriptionText: TextView =view.findViewById(R.id.day_desc);
        var dayName: TextView = view.findViewById(R.id.day_name)
        var dayIcon: ImageView = view.findViewById(R.id.day_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.daily_row,parent,false)
        return DayRecyclerAdapter.Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        ///////////////update here/////////////////
        holder.dayName.text=weatherList.get(position).name
        holder.dayIcon.setImageResource(weatherList.get(position).icon)
        holder.descriptionText.text=weatherList.get(position).state
    }

    override fun getItemCount(): Int {
       return weatherList.size
    }
}