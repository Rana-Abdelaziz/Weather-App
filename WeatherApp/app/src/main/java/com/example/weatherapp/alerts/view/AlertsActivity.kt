package com.example.weatherapp.alerts.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.alerts.view_models.AlertViewModel
import com.example.weatherapp.alerts.view_models.AlertViewModelFactory
import com.example.weatherapp.databinding.ActivityAlertsBinding
import com.example.weatherapp.models.AlertForRoomModel
import com.example.weatherapp.models.RepositoryForLocal

class AlertsActivity : AppCompatActivity() {
    var dynamicFragment: AlertFragment=AlertFragment()
    var mgr: FragmentManager? = null
    var transaction: FragmentTransaction? = null

    lateinit var viewModelFactory: AlertViewModelFactory
    private lateinit var binding: ActivityAlertsBinding
    companion object{
        lateinit var viewModel: AlertViewModel
    }

    lateinit var myAdapter: AlertsAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var alertList:List<AlertForRoomModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlertsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelFactory= AlertViewModelFactory( RepositoryForLocal(this))
        viewModel= ViewModelProvider(this,viewModelFactory).get(AlertViewModel::class.java)

        alertList= listOf()
        layoutManager=LinearLayoutManager(this)
        layoutManager.orientation= RecyclerView.VERTICAL
        myAdapter= AlertsAdapter(alertList,this)
        binding.alertsRecyclerView.adapter=myAdapter
        binding.alertsRecyclerView.layoutManager=layoutManager

        mgr = supportFragmentManager
        transaction = mgr!!.beginTransaction()

        binding.addAlerts.setOnClickListener{
            if (savedInstanceState == null) {
                transaction!!.setReorderingAllowed(true)
                transaction!!.add(R.id.alertFragment, dynamicFragment!!, "alert_fragment")
                transaction!!.commit()
                transaction = mgr!!.beginTransaction()

            } else {
                dynamicFragment = mgr!!.findFragmentByTag("alert_fragment") as AlertFragment
            }
        }
    }

    override fun onStart() {
        viewModel.getAllAlert()
        viewModel.myAlerts.observe(this){
                l->
            if(l!=null){
                myAdapter.alertList=l
                myAdapter.notifyDataSetChanged()
            }
        }
        super.onStart()
    }
}