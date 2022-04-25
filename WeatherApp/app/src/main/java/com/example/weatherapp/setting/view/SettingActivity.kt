package com.example.weatherapp.setting.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.map.view.MapsActivity
import com.example.weatherapp.R
import com.example.weatherapp.models.RepositoryForLocal
import com.example.weatherapp.setting.viewModel.SettingViewModel
import com.example.weatherapp.setting.viewModel.SettingViewModelFactory
import java.util.*

class SettingActivity : AppCompatActivity() {
    lateinit var locationGroup:RadioGroup
    lateinit var windSpeedGroup:RadioGroup
    lateinit var temperatureGroup:RadioGroup
    lateinit var languageGroup:RadioGroup
    lateinit var viewModelFactory: SettingViewModelFactory
    lateinit var viewModel: SettingViewModel
    lateinit var mapBtn:RadioButton
    lateinit var gpsBtn:RadioButton
    lateinit var arabicBtn:RadioButton
    lateinit var englishBtn:RadioButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        locationGroup=findViewById(R.id.locationGroup)
        windSpeedGroup=findViewById(R.id.windSpeedGroup)
        temperatureGroup=findViewById(R.id.temperatureGroup)
        languageGroup=findViewById(R.id.languageGroup)

        mapBtn=findViewById(R.id.map_radio_btn)
        gpsBtn=findViewById(R.id.gps_radio_btn)

        arabicBtn=findViewById(R.id.arabic_radio_btn)
        englishBtn=findViewById(R.id.english_radio_btn)

        viewModelFactory= SettingViewModelFactory( RepositoryForLocal(this))
        viewModel= ViewModelProvider(this,viewModelFactory).get(SettingViewModel::class.java)
        gpsBtn.setOnClickListener{
            Log.i("maappp","inside gps button")
            viewModel.writeToSharedPreference("Location","gps")
        }


        mapBtn.setOnClickListener{
            Log.i("maappp","inside map button")
            viewModel.writeToSharedPreference("Location","map")
            val int= Intent(this@SettingActivity, MapsActivity::class.java)
            int.putExtra("activity name","home")
            startActivity(int)
        }
        arabicBtn.setOnClickListener {
            Log.i("maappp","english")
            viewModel.writeToSharedPreference("Language","arabic")
            languageSettings(1)
        }
        englishBtn.setOnClickListener {
            Log.i("maappp","english")
            viewModel.writeToSharedPreference("Language","english")
            languageSettings(0)
        }


    }


    override fun onPause() {
        super.onPause()

        var windSpeedId=windSpeedGroup.checkedRadioButtonId
        when(windSpeedId){
            R.id.m_h_btn->viewModel.writeToSharedPreference("WindSpeed","m/h")
            R.id.m_s_btn->viewModel.writeToSharedPreference("WindSpeed","m/s")
        }
        var temperatureId=temperatureGroup.checkedRadioButtonId
        when(temperatureId){
            R.id.celsius_radio_btn->viewModel.writeToSharedPreference("Temperature","c")
            R.id.kelvin_radio_btn->viewModel.writeToSharedPreference("Temperature","k")
            R.id.fahrenheit_radio_btn->viewModel.writeToSharedPreference("Temperature","f")
        }
    }
    fun languageSettings(languageValue:Int) {
        val config = resources.configuration
        val lang = when (languageValue) {
            0 -> "en"
            1 -> "ar"
            else -> "en"
        }
        var locale = Locale(lang)
        Locale.setDefault(locale)
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            this.createConfigurationContext(config)
        }

       /* if (TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL) {
            requireActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            requireActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }*/

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
    }
}