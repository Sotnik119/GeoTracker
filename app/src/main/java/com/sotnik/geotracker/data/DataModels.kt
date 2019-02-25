package com.sotnik.geotracker.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "routes")
data class Route(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var startDate: Long? = null,
    var finishDate: Long? = null,
    var distance: Int = 0,
    @Ignore
    var points: List<Point> = ArrayList()
)

@Entity(tableName = "points")
data class Point(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var routeId: Int,
    var time: Long,
    var latitude: Double,
    var longitude: Double
)