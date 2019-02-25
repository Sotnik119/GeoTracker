package com.sotnik.geotracker.data

import android.location.Location
import com.sotnik.geotracker.ContextHelper
import io.reactivex.Flowable
import io.reactivex.Single

class DataProcessor {
    private val db = ContextHelper.getDatabase()!!.geoDao()


    fun createRoute(): Long {
        return db.insertRoute(Route())
    }

    fun startRoute(route: Route): Int {
        route.startDate = System.currentTimeMillis()
        return updateRoute(route)
    }

    fun finishRoute(route: Route): Int {
        route.finishDate = System.currentTimeMillis()
        return updateRoute(route)
    }

    fun updateRoute(route: Route): Int{
        return db.updateRoute(route)
    }

    fun getAllRoutes(): Flowable<List<Route>> {
        return db.getAllRoutes()
    }

    fun getRoute(id: Long): Flowable<Route> {
        return db.getRoute(id)
    }

    fun createPoint(route: Long, location: Location) {
        val point = Point(
            routeId = route.toInt(),
            time = location.time,
            latitude = location.latitude,
            longitude = location.longitude
        )
        return db.insertPoint(point)
    }

    fun getPointsForRoute(route: Long): Flowable<List<Point>> {
        return db.getPointsForRoute(route)
    }
}