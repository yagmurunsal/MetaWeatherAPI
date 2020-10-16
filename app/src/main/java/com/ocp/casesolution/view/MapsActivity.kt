package com.ocp.casesolution.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import  androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.ocp.casesolution.R
import com.ocp.casesolution.adapter.LocationSearchLatLongAdapter
import com.ocp.casesolution.viewmodel.MapsActivityViewModel
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private var snackBar: Snackbar? = null
    private lateinit var viewModel: MapsActivityViewModel
    private var locationAdapter = LocationSearchLatLongAdapter(arrayListOf())
    private lateinit var mMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    lateinit var intConnectivity: TextView
    lateinit var latLong : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
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
                            val isWifi:Boolean = cm.getNetworkCapabilities(network).hasTransport(
                                NetworkCapabilities.TRANSPORT_WIFI)

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
        viewModel = ViewModelProviders.of(this).get(MapsActivityViewModel::class.java)
        viewModel.refreshData()
        locationSearchLatLong.layoutManager = LinearLayoutManager(this)
        locationSearchLatLong.adapter = locationAdapter
        swipeRefreshLayout.setOnRefreshListener {
            locationSearchLatLong.visibility = View.GONE
            locationError.visibility = View.GONE
            locationLoading.visibility = View.VISIBLE
            viewModel.refreshData()
            swipeRefreshLayout.isRefreshing = false
        }

        observeLiveData()

    }
    private suspend fun doSomething(isConnected:Boolean, isWifi:Boolean= false){
        withContext(Dispatchers.Main){
            if(isConnected) {
                intConnectivity.text = "Connected "+(if(isWifi)"WIFI" else "MOBILE")
                intConnectivity.setBackgroundColor(-0x8333da)
            }else {
                intConnectivity.text = "Not Connected"
                intConnectivity.setBackgroundColor(-0x10000)
            }
        }
    }
    private fun observeLiveData() {

        viewModel.locations.observe(this, Observer { locations ->
            locations?.let {
                locationSearchLatLong.visibility = View.VISIBLE
                    locationAdapter.updateLocationList(locations)

                


            }
        })
        viewModel.locationError.observe(this, Observer { error ->
            error?.let {
                if (it) {
                    locationError.visibility = View.VISIBLE
                } else {
                    locationError.visibility = View.GONE
                }
            }
        })
        viewModel.locationLoading.observe(this, Observer { loading ->
            loading?.let {
                if (it) {
                    locationLoading.visibility = View.VISIBLE
                    locationSearchLatLong.visibility = View.GONE
                    locationError.visibility = View.GONE
                } else {
                    locationLoading.visibility = View.GONE
                }
            }
        })
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {

                if (location != null) {

                   mMap.clear()
                    val userLocation= LatLng(location.latitude,location.longitude)
                    mMap.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15f))
                    //Toast.makeText(getBaseContext(), "Latitude: " +   location.getLatitude() + "Longitude: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                    viewModel.getLatLng(location.getLatitude(),location.getLongitude())
                    viewModel.lattLong=location.getLatitude().toString() + "," +location.getLongitude().toString()
                    latLong= location.getLatitude().toString() + "," +location.getLongitude().toString()
                    //locationAdapter.latLong=latLong
                }

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String?) {
            }

            override fun onProviderDisabled(provider: String?) {
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2,2f,locationListener)
            var lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if(lastLocation != null){
                val lastKnownLatLng = LatLng(lastLocation.latitude,lastLocation.longitude)
                mMap.addMarker(MarkerOptions().position(lastKnownLatLng).title("Your Location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLatLng,15f))
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0) {
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2,2f,locationListener)
                }
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}