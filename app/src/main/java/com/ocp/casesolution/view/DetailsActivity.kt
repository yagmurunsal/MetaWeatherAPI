package com.ocp.casesolution.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.ocp.casesolution.R
import com.ocp.casesolution.adapter.LocationInfoAdapter
import com.ocp.casesolution.adapter.LocationSearchLatLongAdapter
import com.ocp.casesolution.service.WeatherAPIService
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.adaptericin.view.*

class DetailsActivity : AppCompatActivity() {
    private val weatherAPIService= WeatherAPIService()
    private var locationInfoAdapter = LocationInfoAdapter(arrayListOf())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val woeid = intent.extras?.getInt("woeid")
        locatonInfoRec.adapter=locationInfoAdapter


        if (woeid != null) {
            weatherAPIService.getWoeid(woeid)

        }

    }
}