package com.example.weatherapp.alerts.view

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import com.example.weatherapp.R
import com.example.weatherapp.favorite.views.FavoriteRecyclerAdapter
import com.example.weatherapp.home.views.HomeActivity
import com.example.weatherapp.models.AlertForRoomModel
import com.example.weatherapp.models.LocationModel

class AlertsAdapter(var alertList:List<AlertForRoomModel>, var context: Context) : RecyclerView.Adapter<AlertsAdapter.Holder>() {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var view: View =itemView
        var cityTxt: TextView =view.findViewById(R.id.city_nameA)
        var timeTxt: TextView =view.findViewById(R.id.time_Txt)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alert_row,parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.cityTxt.text= alertList[position].cityName
        holder.timeTxt.text=alertList[position].time
        holder.view.setOnLongClickListener {
            AlertsActivity.viewModel.deleteAlert(alertList[position])
            WorkManager.getInstance().cancelUniqueWork(alertList[position].id)

            return@setOnLongClickListener true

        }

    }

    override fun getItemCount(): Int {
       return alertList.size
    }
}