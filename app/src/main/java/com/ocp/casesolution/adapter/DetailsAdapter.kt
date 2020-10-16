package com.ocp.casesolution.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ocp.casesolution.R
import com.ocp.casesolution.model.ConsolidatedWeather
import com.ocp.casesolution.model.LocationInfo
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.view_min_max_temp.view.*

class DetailsAdapter(val locationInfoList: ArrayList<ConsolidatedWeather>) :
    RecyclerView.Adapter<DetailsAdapter.LocationInfoViewHolder>() {
    class LocationInfoViewHolder(var view: View) : RecyclerView.ViewHolder(view.rootView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationInfoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.view_min_max_temp, parent, false)
        return LocationInfoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return locationInfoList.size
    }

    override fun onBindViewHolder(holder: LocationInfoViewHolder, position: Int) {
        holder.view.datee.text = locationInfoList[position].formattedApplicableDate(position)
        holder.view.max.text = locationInfoList[position].maxTemp.toString().substringBefore(".")
        holder.view.min.text = locationInfoList[position].minTemp.toString().substringBefore(".")
        Glide.with(holder.view.context).load(locationInfoList[position].getImageUrl())
            .into(holder.view.state_image);

        if (position > 0) {
            holder.view.state_image.visibility = View.VISIBLE
        } else {
            holder.view.state_image.visibility = View.INVISIBLE
        }

    }

    fun updateLocationList(newLocationList: List<ConsolidatedWeather>) {
        locationInfoList.clear()
        locationInfoList.addAll(newLocationList)
        notifyDataSetChanged()
    }
}