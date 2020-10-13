package com.ocp.casesolution.model

import android.util.Log
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class LocationInfo(
    @SerializedName("consolidated_weather")
    val consolidatedWeather: List<ConsolidatedWeather>,
    @SerializedName("time")
    val time: String,
    @SerializedName("sun_rise")
    val sunRise: String,
    @SerializedName("sun_set")
    val sunSet: String,
    @SerializedName("timezone_name")
    val timezoneName: String,
    @SerializedName("parent")
    val parent: Parent,
    @SerializedName("sources")
    val sources: List<Source>,
    @SerializedName("title")
    val title: String,
    @SerializedName("location_type")
    val locationType: String,
    @SerializedName("woeid")
    val woeid: Long,
    @SerializedName("latt_long")
    val lattLong: String,
    @SerializedName("timezone")
    val timezone: String

){
    private fun format(str: String): String {
        // expected input format "2020-05-24T08:19:40.807726-05:00"
        try {
            val slits = str.split(".")
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            inputFormat.timeZone = TimeZone.getTimeZone(timezone/*"Europe/London"*/)

            val outputFormat = SimpleDateFormat("h:mm a")
            outputFormat.timeZone = inputFormat.timeZone
            return outputFormat.format(inputFormat.parse(slits[0])!!)
        } catch (e: Exception) {
            Log.e(LocationInfo::class.java.simpleName, e.stackTrace.toString());
        }
        return "-"
    }

    fun formattedTime(): String {
        return format(time)
    }

    fun formattedSunriseTime(): String {
        return format(sunRise)
    }

    fun formattedSunsetTime(): String {
        return format(sunSet)
    }
}