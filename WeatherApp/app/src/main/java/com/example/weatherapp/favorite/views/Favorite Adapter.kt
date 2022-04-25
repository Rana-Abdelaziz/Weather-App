package com.example.weatherapp.favorite.views

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.home.views.HomeActivity
import com.example.weatherapp.home.views.HourRecyclerAdapter
import com.example.weatherapp.models.LocationModel
import com.example.weatherapp.models.responseDataModel.Hourly
import com.example.weatherapp.network.Connection

class FavoriteRecyclerAdapter(var locationList:List<LocationModel>,var context: Context) : RecyclerView.Adapter<FavoriteRecyclerAdapter.Holder>() {


    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var view: View =itemView
        var cityTxt: TextView =view.findViewById(R.id.city_nameF)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_row,parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.cityTxt.text= locationList[position].cityName
        /////////////update here/////////////////
        holder.view.setOnClickListener{
            if(Connection.isConnected(context)){
                var intent=Intent(context,HomeActivity::class.java)
                intent.putExtra("flag","favorite")
                intent.putExtra("lat",locationList[position].lat)
                intent.putExtra("long",locationList[position].lon)
                intent.putExtra("city",locationList[position].cityName)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent)
            }else{
                Toast.makeText(context,"You have to connect to the network at first",Toast.LENGTH_LONG).show()
            }

        }
    }

    override fun getItemCount(): Int { return locationList.size    }
}