package com.ocp.casesolution.model

import com.google.gson.annotations.SerializedName

data class LocationSearchName(
    @SerializedName("title")
    val title: String,
    @SerializedName("location_type")
    val locationType: String,
    @SerializedName("woeid")
    val woeid: Long,
    @SerializedName("latt_long")
    val lattLong: String
)