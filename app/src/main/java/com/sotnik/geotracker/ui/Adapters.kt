package com.sotnik.geotracker.ui

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sotnik.geotracker.ContextHelper
import com.sotnik.geotracker.R
import com.sotnik.geotracker.data.Point
import com.sotnik.geotracker.data.Route
import kotlinx.android.synthetic.main.point_list_item.view.*
import kotlinx.android.synthetic.main.route_list_item.view.*

class RoutesAdapter(val items: ArrayList<Route>, val context: Context) : RecyclerView.Adapter<RouteViewHolder>() {

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        holder.apply {
            uid?.text = items[position].id.toString()
            start?.text = ContextHelper.longToStringTime(items[position].startDate)
            finish?.text = ContextHelper.longToStringTime(items[position].finishDate)
            distance.text = "${items[position].distance} м"
            itemView.setOnClickListener {
                context.startActivity(
                    Intent(context, DetailActivity::class.java).putExtra("routeId", items[position].id)
                )
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RouteViewHolder {
        return RouteViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.route_list_item,
                p0,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return items.size
    }

}

class RouteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val uid = view.uid
    val distance = view.distance
    val start = view.start
    val finish = view.finish
}

class PointsAdapter(val items: ArrayList<Point>, val context: Context) : RecyclerView.Adapter<PointViewHolder>() {

    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
        holder.apply {
            pId?.text = (position + 1).toString()
            lantitude?.text = items[position].latitude.toString()
            longitude?.text = items[position].longitude.toString()
            date?.text = ContextHelper.longToStringTime(items[position].time)
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PointViewHolder {
        return PointViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.point_list_item,
                p0,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return items.size
    }

}

class PointViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val pId = view.point_uid
    val lantitude = view.lan
    val longitude = view.lon
    val date = view.date_time
}