package com.example.weatherapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.models.AlertForRoomModel
import com.example.weatherapp.models.LocationModel
import com.example.weatherapp.models.responseDataModel.ResponseModel

@Database(entities = [ResponseModel::class,LocationModel::class,AlertForRoomModel::class], version = 5)
@TypeConverters(TypeConverter::class)
abstract class DataBase : RoomDatabase() {
    abstract fun dataBaseDao(): DataBaseDao
    companion object{
        private var databaseInstance: DataBase? = null
        fun getInstance(context: Context?): DataBase? {
            if (databaseInstance == null) {
                databaseInstance= Room.databaseBuilder(
                    context!!.applicationContext,
                    DataBase::class.java, "MyWeather")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return databaseInstance
        }
    }
}