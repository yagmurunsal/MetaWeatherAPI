package com.ocp.casesolution.viewmodel

import android.content.ContentValues.TAG
import android.location.Location
import android.telecom.Call
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ocp.casesolution.model.LocationInfo
import com.ocp.casesolution.model.LocationSearchLatLong
import com.ocp.casesolution.service.LocationAPI
import com.ocp.casesolution.service.WeatherAPIService
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class DetailsViewModel : ViewModel() {

    private var callLocationInfo: retrofit2.Call<LocationInfo>? = null
    private val locationInfo = MutableLiveData<LocationInfo>()
    private val progressBar = MutableLiveData<Boolean>()
    private var lastLocationID: Int? = null


    fun getProgressBar(): LiveData<Boolean> {
        return progressBar
    }

    fun getLocationInfo(): LiveData<LocationInfo> {
        return locationInfo
    }

    fun getLocationDetails(mwLocationID: Int) {
        if (callLocationInfo != null) {
            callLocationInfo!!.cancel()
        }
        if (lastLocationID != null && lastLocationID == mwLocationID) {

            return
        }
        progressBar.postValue(true);
        callLocationInfo = LocationAPI.instance.getLocationInfo(mwLocationID)
        callLocationInfo?.enqueue(object : Callback<LocationInfo> {
            override fun onFailure(call: retrofit2.Call<LocationInfo>, t: Throwable) {
                progressBar.postValue(false)
                when {
                    call.isCanceled -> {
                        Log.i(TAG, "request cancelled")
                    }
                    t is IOException -> {
                        Log.i(TAG, "No internet")
                    }
                    else -> {
                        Log.i(TAG, "unknown error")
                    }
                }
            }

            override fun onResponse(

                call: retrofit2.Call<LocationInfo>,
                response: Response<LocationInfo>
            ) {
                Log.d("Responsee", response.toString())
                progressBar.postValue(false)
                locationInfo.postValue(response.body())
                lastLocationID = mwLocationID
            }
        })
    }

    companion object {
        val TAG = DetailsViewModel::class.java.simpleName
    }
}


