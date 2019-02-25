package com.sotnik.geotracker.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.content.ContextCompat


class GeoApi(private val context: Context) : LocationListener {

    private var lastNetworkLocation: Location? = null
    private var lastGpsLocation: Location? = null
    private var locationReturner: LocationReturner? = null
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager


    @SuppressLint("MissingPermission")
    fun startListeners(): Boolean {

        if (isHavePermissions()) {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                100L,
                3f,
                this@GeoApi
            )

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                100L,
                2f,
                this@GeoApi
            )

            return true
        } else {
            return false
        }
    }

    fun stopListeners() {
        locationManager.removeUpdates(this@GeoApi)
    }

    companion object Priority {
        val PROIRITY_GPS = "p_gps"
        val PRIORITY_NETWORK = "p_network"
    }

    fun getLocation(priority: String): Location? {
        return when (priority) {
            PROIRITY_GPS -> lastGpsLocation
            PRIORITY_NETWORK -> lastNetworkLocation
            else -> null
        }
    }

    fun setCallbacks(callbacks: LocationReturner) {
        locationReturner = callbacks
    }

    private fun isHavePermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        )
            return false

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        )
            return false

        return true
    }

    private fun isGpsEnabled() = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    private fun isNetworkEnbled() = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)


    override fun onLocationChanged(location: Location?) {
        if (location?.provider == LocationManager.GPS_PROVIDER) {
            lastGpsLocation = location
            locationReturner?.pushGpsLocation(location)
        } else if (location?.provider == LocationManager.NETWORK_PROVIDER) {
            lastNetworkLocation = location
            locationReturner?.pushNetworkLocation(location)
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }

    interface LocationReturner {
        fun pushGpsLocation(location: Location?)
        fun pushNetworkLocation(location: Location?)
    }
}