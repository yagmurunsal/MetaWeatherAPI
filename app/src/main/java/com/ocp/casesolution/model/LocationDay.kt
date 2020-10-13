package com.ocp.casesolution.model

import com.google.gson.annotations.SerializedName
import com.ocp.casesolution.service.LocationAPI
import java.text.SimpleDateFormat
import java.util.*

data class LocationDay(
    @SerializedName("id")
    val id: Long,
    @SerializedName("weather_state_name")
    val weatherStateName: String,
    @SerializedName("weather_state_abbr")
    val weatherStateAbbr: String,
    @SerializedName("wind_direction_compass")
    val windDirectionCompass: String,
    @SerializedName("created")
    val created: String,
    @SerializedName("applicable_date")
    val applicableDate: String,
    @SerializedName("min_temp")
    val minTemp: Double,
    @SerializedName("max_temp")
    val maxTemp: Double,
    @SerializedName("the_temp")
    val theTemp: Double,
    @SerializedName("wind_speed")
    val windSpeed: Double,
    @SerializedName("wind_direction")
    val windDirection: Double,
    @SerializedName("air_pressure")
    val airPressure: Double,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("visibility")
    val visibility: Double?,
    @SerializedName("predictability")
    val predictability: Int


){


    private fun formattedApplicableDate(): String = formattedApplicableDate("EEE d")

    private fun formattedApplicableDate(pattern: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(applicableDate)
        val format = SimpleDateFormat(pattern, Locale.getDefault())
        return format.format(date!!)
    }

    private fun formattedFullApplicableDate(): String = formattedApplicableDate("EEE, dd MMM yyyy")

    fun formattedApplicableDate(index: Int): String {
        return when (index) {
            0 -> {
                "Today"
            }
            1 -> {
                "Tomorrow"
            }
            -1 -> {
                formattedFullApplicableDate()
            }
            else -> formattedApplicableDate()
        }
    }
}