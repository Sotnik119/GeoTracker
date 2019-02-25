package com.sotnik.geotracker.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.sotnik.geotracker.R
import com.sotnik.geotracker.data.Route
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var routes = ArrayList<Route>()
    private val mainPresenter = MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        routeList.adapter = RoutesAdapter(routes, this)
        mainPresenter.bindMainActivity(this)
    }


    override fun onResume() {
        super.onResume()

        mainPresenter.loadData()
    }

    override fun onPause() {
        super.onPause()
        mainPresenter.pause()
    }

    fun updateList(items: List<Route>) {
        routes.clear()
        items.forEach { routes.add(it) }
        routeList.adapter?.notifyDataSetChanged()
    }

    fun showNoItems() {
        no_route_view.visibility = View.VISIBLE
    }

    fun hideNoItem() {
        no_route_view.visibility = View.GONE
    }

    fun showLoad() {
        loading.visibility = View.VISIBLE
    }

    fun hideLoad() {
        loading.visibility = View.GONE
    }

    fun showList() {
        routeList.visibility = View.VISIBLE
    }

    fun hideList() {
        routeList.visibility = View.GONE
    }
}
