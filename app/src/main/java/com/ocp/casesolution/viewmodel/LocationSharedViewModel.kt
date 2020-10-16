package com.ocp.casesolution.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationSharedViewModel : ViewModel() {

    private val locationID = MutableLiveData<Int>()

    fun getLocations(): LiveData<Int> {
        return locationID
    }

    fun selectedLocation(mwLocationID: Int) {
        this.locationID.postValue(mwLocationID);
    }

    companion object {
        val TAG = LocationSharedViewModel::class.java.simpleName
    }

}