package com.example.weatherapp.alerts.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.weatherapp.R
import com.example.weatherapp.worker.ReminderWorker
import com.example.weatherapp.home.views.HomeActivity
import com.example.weatherapp.models.AlertForRoomModel
import java.util.*
import java.util.concurrent.TimeUnit


class AlertFragment : Fragment() {

    var txtStartDate: TextView? = null
    var txtReminderTimes: TextView? = null
    var cityTxt: TextView? = null
    lateinit var numOfDays:EditText
    lateinit var saveBtn:Button
    lateinit var mapBtn:Button
    lateinit var alert:AlertForRoomModel
    var requests= mutableListOf<OneTimeWorkRequest>()
    var hour=0
    var minute=0
    var alertTime=""
    var cityName=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        var view=inflater.inflate(R.layout.fragment_alert, container, false)
        txtStartDate = view!!.findViewById(R.id.txtStartDate)
        txtReminderTimes=view!!.findViewById(R.id.txtReminderTimes)
        cityTxt=view!!.findViewById(R.id.choosenPlaceText)
        numOfDays=view!!.findViewById(R.id.numOfDayesTxt)
        saveBtn=view!!.findViewById(R.id.saveButton)
        Log.i("lknl","lati: ${HomeActivity.lati} long:${HomeActivity.long}")

        //mapBtn=view!!.findViewById(R.id.mapSelect)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setCityName()
       /* if (savedInstanceState!=null){
            val lat = arguments?.getDouble("lat")
            val long=arguments?.getDouble("lon")
            Log.i("nfdk","lat $lat long $long")
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address> = geocoder.getFromLocation(lat!!, long!!, 1)
            if (!addresses.isEmpty()){
                val cityName=addresses.get(0).countryName
                cityTxt!!.text=cityName
            }

        }*/
        txtStartDate!!.setOnClickListener { createDatePickDialogForStartDate() }
        txtReminderTimes!!.setOnClickListener {
            ////////////update/////////////
            var s=getResources().getString(R.string.selectStartDate)
            if(numOfDays.text.isEmpty()|| txtStartDate!!.text.equals(s)){
                Toast.makeText(context,"Please select start date and number of days at first",Toast.LENGTH_LONG).show()
            }else{
                createTimePickerDialog()
            }
        }
        saveBtn.setOnClickListener {
            var s=getResources().getString(R.string.selectStartDate)
            var t=getResources().getString(R.string.selectTime)
            val id = UUID.randomUUID().toString().toUpperCase()
            if(!(numOfDays.text.isEmpty()||txtStartDate!!.text.equals(s)||txtReminderTimes!!.text.equals(t))){
                alert= AlertForRoomModel(cityName,id,HomeActivity.lati,HomeActivity.long,alertTime)
                AlertsActivity.viewModel.insertAlert(alert)
                requireFragmentManager().beginTransaction()
                    .remove(this).commit()
               numOfDays.setText("")
                WorkManager.getInstance().enqueueUniqueWork(id, ExistingWorkPolicy.REPLACE, requests)
                Log.i("sblks","${requests.size}")


            }else{
                Toast.makeText(context,"Please complete all required data at first",Toast.LENGTH_LONG).show()

            }

        }
      /*  mapBtn.setOnClickListener {

            var intent=Intent(view.context,MapsActivity::class.java)
            intent.putExtra("activity name","fragment")
            startActivity(intent)
        }*/
    }

    private fun createTimePickerDialog( ) {
        val timePickerDialog = TimePickerDialog(context,
            { timePicker, hourOfDay, minutes ->
                hour = hourOfDay
                minute = minutes
                val calendar = Calendar.getInstance()
                calendar[0, 0, 0, hourOfDay] = minutes
                    val startDate: String = txtStartDate!!.text.toString()
                ////24/5/2020///////////
                    val s = startDate.split("/".toRegex()).toTypedArray()
                    val today = Calendar.getInstance()
                    val cStartDate = Calendar.getInstance()
                    cStartDate[s[2].toInt(), s[1].toInt(), s[0].toInt(), hourOfDay] = minutes
                    var diffInMinutes = (cStartDate.timeInMillis - today.timeInMillis) / 60000
                    diffInMinutes -= 44640
                    val language=AlertsActivity.viewModel.readSetting("Language")
                    var languageForQuery=""
                if (language=="arabic"){
                    languageForQuery="ar"
                }else{
                    languageForQuery="en"
                }
                    val inputData = Data.Builder()
                    .putString("City", cityName)
                    .putString("language", languageForQuery)
                    .putDouble("lat", HomeActivity.lati)
                    .putDouble("Long", HomeActivity.long)
                    .build()
                    var workRequest: OneTimeWorkRequest = OneTimeWorkRequest.Builder(ReminderWorker::class.java)
                        .setInitialDelay(diffInMinutes, TimeUnit.MINUTES)
                        .setInputData(inputData)
                        .build()
                    if (diffInMinutes > 0) {
                        requests.add(workRequest)
                        Log.i("Tagg", "done")
                    }
                    for (i in 1..(numOfDays.text.toString()).toInt()) {
                        val duration = Math.abs(diffInMinutes + 1440 * i)
                        workRequest = OneTimeWorkRequest.Builder(ReminderWorker::class.java)
                            .setInitialDelay(duration, TimeUnit.MINUTES)
                            .setInputData(inputData)
                            .build()
                        requests.add(workRequest)
                    }
                txtReminderTimes!!.text = DateFormat.format("hh:mm aa", calendar)
                alertTime=txtReminderTimes!!.text.toString()

            }, 12, 0, false
        )
        timePickerDialog.updateTime(hour, minute)
        timePickerDialog.show()    }

    private fun setCityName() {
        val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(HomeActivity.lati, HomeActivity.long, 1)
                Log.i("lknl","lati: $HomeActivity.lati long:$HomeActivity.long")
                cityName = addresses[0].locality

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createDatePickDialogForStartDate() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { datePicker, year, month, day ->
                var month = month
                month = month + 1
                var date2 = "$day/$month/$year"
                txtStartDate?.text=date2
            }, year, month, day
        )
        datePickerDialog.datePicker.minDate = calendar.timeInMillis - 1000
        datePickerDialog.show()
    }


}