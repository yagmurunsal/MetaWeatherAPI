package com.ocp.casesolution.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ocp.casesolution.R
import com.ocp.casesolution.model.LocationInfo
import com.ocp.casesolution.model.LocationSearchLatLong
import kotlinx.android.synthetic.main.adaptericin.view.*

class LocationInfoAdapter(val locationInfoList: ArrayList<LocationInfo>) :
    RecyclerView.Adapter<LocationInfoAdapter.LocationInfoViewHolder>() {

    class LocationInfoViewHolder(var view: View) : RecyclerView.ViewHolder(view.rootView) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocationInfoAdapter.LocationInfoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.adaptericin, parent, false)
        return LocationInfoAdapter.LocationInfoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return locationInfoList.size
    }

    override fun onBindViewHolder(
        holder: LocationInfoAdapter.LocationInfoViewHolder,
        position: Int
    ) {
        holder.view.sunsetTitle.text =locationInfoList[position].sunSet
        Log.d("YAGMUR", locationInfoList[position].woeid.toString())
        Log.d("SUNSET", locationInfoList[position].sunSet)
        Log.d("SUNRISE", locationInfoList[position].sunRise)
    }
}