package com.ocp.casesolution.service

import com.ocp.casesolution.BuildConfig
import com.ocp.casesolution.model.LocationInfo
import com.ocp.casesolution.model.LocationSearchLatLong
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LocationAPI {
    companion object {

        // singleton instance
        val instance: LocationAPI by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.metaweather.com/" + "api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create(LocationAPI::class.java)
        }
        fun getImageUrl(abbr: String): String {
            return "https://www.metaweather.com/" + "static/img/weather/png/" + abbr + ".png"
        }

    }
    @GET("api/location/search")
    fun searchLocationByLattLong(@Query("lattlong") lattlong: String): Single<List<LocationSearchLatLong>>

    @GET("location/{woeid}")
    fun getLocationInfo(@Path("woeid") woeid: Int): Call<LocationInfo>
}