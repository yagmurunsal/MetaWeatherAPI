package com.ocp.casesolution.service


import com.ocp.casesolution.BuildConfig
import com.ocp.casesolution.model.ConsolidatedWeather
import com.ocp.casesolution.model.LocationInfo
import com.ocp.casesolution.model.LocationSearchLatLong
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class WeatherAPIService {

    private val BASE_URL ="https://www.metaweather.com/"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(LocationAPI::class.java)

    fun getData(lattLong : String): Single<List<LocationSearchLatLong>>{
        return api.searchLocationByLattLong(lattLong)
    }

    fun getWoeid(woeid: Int): Call<LocationInfo> {
        return api.getLocationInfo(woeid)
    }


}