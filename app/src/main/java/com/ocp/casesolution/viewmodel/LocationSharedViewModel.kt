package com.ocp.casesolution.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationSharedViewModel : ViewModel() {

    private val mwLocationID = MutableLiveData<Int>()

    fun getLocations(): LiveData<Int> {
        return mwLocationID
    }

    fun selectedLocation(mwLocationID: Int) {
        this.mwLocationID.postValue(mwLocationID);
    }

    companion object {
        val TAG = LocationSharedViewModel::class.java.simpleName
    }

}