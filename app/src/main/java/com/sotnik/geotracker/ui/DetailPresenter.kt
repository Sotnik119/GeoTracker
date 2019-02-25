package com.sotnik.geotracker.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import com.sotnik.geotracker.data.DataProcessor
import com.sotnik.geotracker.data.Point
import com.sotnik.geotracker.data.Route
import com.sotnik.geotracker.location.GeoApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.async


class DetailPresenter() {

    private lateinit var detailActivity: DetailActivity
    private var disposabes = CompositeDisposable()
    private val db = DataProcessor()
    private lateinit var geoApi: GeoApi
    private lateinit var currentRoute: Route

    fun bind(activity: DetailActivity) {
        detailActivity = activity
        geoApi = GeoApi(activity)
        geoApi.setCallbacks(object : GeoApi.LocationReturner {
            override fun pushGpsLocation(location: Location?) {
                Observable.fromCallable {
                    db.createPoint(detailActivity.routeId, location!!)
                }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            }

            override fun pushNetworkLocation(location: Location?) {
                //nothing
            }
        })
        load()
    }

    private fun load() {
        disposabes.add(
            db.getRoute(detailActivity.routeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { route ->
                    detailActivity.updateRoute(route)
                    currentRoute = route
                }
        )
        disposabes.add(
            db.getPointsForRoute(detailActivity.routeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { points ->
                    run {
                        detailActivity.updateList(points)
                        updateRouteDistance(points)
                        when {
                            points.isEmpty() -> {
                                showStartButton(); return@run
                            }
                            currentRoute.finishDate == null -> showStopButton()
                            else -> hideButton()
                        }
                    }
                }
        )

    }

    private fun updateRouteDistance(points: List<Point>?) {
        async {
            var distance = 0f
            var loc1: Location? = null
            var loc2: Location? = null

            points?.forEach {
                if (loc1 == null) loc1 = Location("p").apply {
                    longitude = it.longitude
                    latitude = it.latitude
                } else {
                    loc2 = Location("p").apply {
                        longitude = it.longitude
                        latitude = it.latitude
                    }
                    distance += loc1!!.distanceTo(loc2)
                    loc1 = loc2
                }

            }
            currentRoute.distance = distance.toInt()
            db.updateRoute(currentRoute)
        }
    }

    fun unbind() {
        disposabes.dispose()
        stopTracking()
        if (currentRoute.startDate != null) {
            async { db.finishRoute(currentRoute) }
        }
    }

    private fun startTracking() = geoApi.startListeners()


    private fun stopTracking() {
        geoApi.stopListeners()
    }

    private fun showStartButton() {
        detailActivity.setStartStopButton("START", true, View.OnClickListener {
            if (startTracking()) {
                hideButton()
                async { db.startRoute(currentRoute) }
            } else {
                getPermissions()
            }
        })
    }

    private fun showStopButton() {
        detailActivity.setStartStopButton("STOP", true, View.OnClickListener {
            stopTracking()
            hideButton()
            async { db.finishRoute(currentRoute) }
        })
    }

    private fun hideButton() {
        detailActivity.setStartStopButton("", false, View.OnClickListener {})
    }


    private fun getPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(
                    detailActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        detailActivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                ) {
                    Toast.makeText(
                        detailActivity,
                        "Необходимо дать разрешение на чтение файлов в настройках устройства!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            if (ContextCompat.checkSelfPermission(
                    detailActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        detailActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    Toast.makeText(
                        detailActivity,
                        "Необходимо дать разрешение на чтение файлов в настройках устройства!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        } else {
            Toast.makeText(
                detailActivity,
                "Необходимо дать разрешение на чтение файлов в настройках устройства!",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}