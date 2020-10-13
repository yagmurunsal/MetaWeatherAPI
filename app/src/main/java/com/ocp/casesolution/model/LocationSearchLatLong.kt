package com.ocp.casesolution.model

import com.google.gson.annotations.SerializedName

data class LocationSearchLatLong(


    @SerializedName("distance")
    val cityDistance:Long?,
    @SerializedName("title")
    val cityTitle: String?,
    @SerializedName("location_type")
    val location_type: String?,
    @SerializedName("woeid")
    val woeid:Int?,
    @SerializedName("latt_long")
    var lattLong: String?)
