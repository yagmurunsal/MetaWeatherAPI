package com.ocp.casesolution.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ocp.casesolution.service.WeatherAPIService
import com.ocp.casesolution.adapter.LocationSearchLatLongAdapter
import com.ocp.casesolution.model.LocationSearchLatLong
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MapsActivityViewModel : ViewModel() {
    private val weatherAPIService = WeatherAPIService()
    private val disposable = CompositeDisposable()
    var lattLong: String = "36.96,-122.02"
    val locations = MutableLiveData<List<LocationSearchLatLong>>()
    val locationError = MutableLiveData<Boolean>()
    val locationLoading = MutableLiveData<Boolean>()

    fun refreshData() {
        getDataFromAPI()
    }

    fun getLatLng(lat: Double, long: Double) {
        val lattLong = lat.toString() + "," + long.toString()
        gettDataFromAPI(lattLong)
        Log.d("PRINT", lat.toString() + long.toString())


    }

    private fun getDataFromAPI() {
        locationLoading.value = true
        disposable.add(
            weatherAPIService.getData(lattLong).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<LocationSearchLatLong>>() {
                    override fun onSuccess(t: List<LocationSearchLatLong>) {
                        locations.value = t
                        locationLoading.value = false
                        locationError.value = false

                    }

                    override fun onError(e: Throwable) {
                        locationLoading.value = false
                        locationError.value = true
                        e.printStackTrace()
                    }

                })
        )

    }

    private fun gettDataFromAPI(lok: String) {
        locationLoading.value = true
        disposable.add(
            weatherAPIService.getData(lok).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<LocationSearchLatLong>>() {
                    override fun onSuccess(t: List<LocationSearchLatLong>) {
                        locations.value = t
                        locationLoading.value = false
                        locationError.value = false

                    }

                    override fun onError(e: Throwable) {
                        locationLoading.value = false
                        locationError.value = true
                        e.printStackTrace()
                    }

                })
        )

    }

}