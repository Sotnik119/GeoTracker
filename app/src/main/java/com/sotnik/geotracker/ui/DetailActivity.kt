package com.sotnik.geotracker.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.sotnik.geotracker.ContextHelper
import com.sotnik.geotracker.R
import com.sotnik.geotracker.data.Point
import com.sotnik.geotracker.data.Route
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.route_list_item.view.*

class DetailActivity : AppCompatActivity() {

    private var points = ArrayList<Point>()
    private val detailPresenter = DetailPresenter()

    var routeId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        points_list.adapter = PointsAdapter(points, this)
        routeId = intent.getLongExtra("routeId", 0L)
        detailPresenter.bind(this)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        detailPresenter.unbind()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun updateList(list: List<Point>) {
        points.clear()
        list.forEach { points.add(it) }
        points_list.adapter?.notifyDataSetChanged()
    }

    fun updateRoute(route: Route) {
        include.uid.text = route.id.toString()
        include.start.text = ContextHelper.longToStringTime(route.startDate)
        include.finish.text = ContextHelper.longToStringTime(route.finishDate)
        include.distance.text = "${route.distance} м"
    }

    fun setStartStopButton(title: String, visible: Boolean, onClick: View.OnClickListener?) {
        start_stop_button.text = title
        start_stop_button.visibility = if (visible) View.VISIBLE else View.GONE
        if (onClick != null) start_stop_button.setOnClickListener(onClick)
    }
}
