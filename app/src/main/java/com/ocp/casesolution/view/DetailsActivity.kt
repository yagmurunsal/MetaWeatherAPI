package com.ocp.casesolution.view

import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ocp.casesolution.R
import com.ocp.casesolution.adapter.DetailsAdapter
import com.ocp.casesolution.viewmodel.DetailsViewModel
import com.ocp.casesolution.viewmodel.LocationSharedViewModel
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsActivity : AppCompatActivity() {

    private lateinit var locationInfoViewModel: DetailsViewModel
    private lateinit var locationSharedViewModel: LocationSharedViewModel
    lateinit var intConnectivity: TextView
    private var consolidetWeatherAdapter = DetailsAdapter(arrayListOf())
    private lateinit var locationManager: LocationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val woeid = intent.extras?.getInt("woeid")
        intConnectivity = findViewById<TextView>(R.id.intText)
        val cm: ConnectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val builder: NetworkRequest.Builder = NetworkRequest.Builder()
            cm.registerNetworkCallback(

                builder.build(),
                object : ConnectivityManager.NetworkCallback() {

                    override fun onAvailable(network: Network) {
                        lifecycleScope.launch {
                            Log.i("MapsActivity", "onAvailable!")

                            // check if NetworkCapabilities has TRANSPORT_WIFI
                            val isWifi: Boolean = cm.getNetworkCapabilities(network).hasTransport(
                                NetworkCapabilities.TRANSPORT_WIFI
                            )

                            doSomething(true, isWifi)
                        }
                    }

                    override fun onLost(network: Network) {
                        lifecycleScope.launch {
                            Log.i("MapsActivity", "onLost!")
                            doSomething(false)
                        }
                    }
                }
            )
        }
        consInfo.adapter = consolidetWeatherAdapter
        consInfo.layoutManager = LinearLayoutManager(this)
        locationInfoViewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        locationSharedViewModel =
            ViewModelProviders.of(this).get(LocationSharedViewModel::class.java)
        if (woeid != null) {
            locationSharedViewModel.selectedLocation(woeid)
        }
        if (woeid != null) {
            observeLiveData()
        }

    }

    private suspend fun doSomething(isConnected: Boolean, isWifi: Boolean = false) {
        withContext(Dispatchers.Main) {
            if (isConnected) {
                intConnectivity.text = "Connected " + (if (isWifi) "WIFI" else "MOBILE")
                intConnectivity.setBackgroundColor(-0x8333da)
            } else {
                intConnectivity.text = "Not Connected"
                intConnectivity.setBackgroundColor(-0x10000)
            }
        }
    }

    private fun observeLiveData() {

        locationSharedViewModel.getLocations()
            .observe(this, Observer { mwLocationID ->

                locationInfoViewModel.getLocationDetails(mwLocationID)
            })

        locationInfoViewModel.getLocationInfo()
            .observe(this, Observer { mwLocationInfo ->
                consolidetWeatherAdapter.updateLocationList(mwLocationInfo.consolidatedWeather)
                titleee.text = mwLocationInfo.title
                weather_state_name.text = mwLocationInfo.consolidatedWeather[0].weatherStateName
                Glide.with(this).load(mwLocationInfo.consolidatedWeather[0].getImageUrl())
                    .into(state_image);
                the_temp.text = mwLocationInfo.consolidatedWeather[0].theTemp.toString()
                    .substringBefore(".") + "°C"
                wind_val.text =
                    mwLocationInfo.consolidatedWeather[0].windSpeed.toString().substringBefore(".")
                humidity_val.text = mwLocationInfo.consolidatedWeather[0].humidity.toString()
                visibility_val.text =
                    mwLocationInfo.consolidatedWeather[0].visibility.toString().substringBefore(".")
                pressure_val.text = mwLocationInfo.consolidatedWeather[0].airPressure.toString()
                confidence_val.text =
                    mwLocationInfo.consolidatedWeather[0].predictability.toString()
                cd.text = mwLocationInfo.title


            })
    }

}