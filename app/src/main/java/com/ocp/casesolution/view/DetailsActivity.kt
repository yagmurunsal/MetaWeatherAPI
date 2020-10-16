package com.ocp.casesolution.view

import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ocp.casesolution.R
import com.ocp.casesolution.adapter.DetailsAdapter
import com.ocp.casesolution.viewmodel.DetailsViewModel
import com.ocp.casesolution.viewmodel.LocationSharedViewModel
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    private lateinit var locationInfoViewModel: DetailsViewModel
    private lateinit var locationSharedViewModel: LocationSharedViewModel

    private var consolidetWeatherAdapter = DetailsAdapter(arrayListOf())
    private lateinit var locationManager: LocationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val woeid = intent.extras?.getInt("woeid")
        consInfo.adapter=consolidetWeatherAdapter
        consInfo.layoutManager = LinearLayoutManager(this)
        locationInfoViewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        locationSharedViewModel = ViewModelProviders.of(this).get(LocationSharedViewModel::class.java)
        if (woeid != null) {
            locationSharedViewModel.selectedLocation(woeid)
        }
        if (woeid != null) {
            observeLiveData()
        }

    }
    private fun observeLiveData(){

        locationSharedViewModel.getLocations()
            .observe(this, Observer { mwLocationID ->

                locationInfoViewModel.getLocationDetails(mwLocationID)
            })

        locationInfoViewModel.getLocationInfo()
            .observe(this, Observer { mwLocationInfo ->
                consolidetWeatherAdapter.updateLocationList(mwLocationInfo.consolidatedWeather)
                titleee.text= mwLocationInfo.title
                weather_state_name.text=mwLocationInfo.consolidatedWeather[0].weatherStateName
                Glide.with(this).load(mwLocationInfo.consolidatedWeather[0].getImageUrl()).into(state_image);
                the_temp.text= mwLocationInfo.consolidatedWeather[0].theTemp.toString().substringBefore(".") + "°C"
                wind_val.text=mwLocationInfo.consolidatedWeather[0].windSpeed.toString().substringBefore(".")
                humidity_val.text=mwLocationInfo.consolidatedWeather[0].humidity.toString()
                visibility_val.text=mwLocationInfo.consolidatedWeather[0].visibility.toString().substringBefore(".")
                pressure_val.text=mwLocationInfo.consolidatedWeather[0].airPressure.toString()
                confidence_val.text=mwLocationInfo.consolidatedWeather[0].predictability.toString()
                cd.text=mwLocationInfo.title


            })
    }

}