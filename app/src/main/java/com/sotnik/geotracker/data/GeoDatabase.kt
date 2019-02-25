package com.sotnik.geotracker.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(Route::class, Point::class), version = 1)
abstract class GeoDatabase : RoomDatabase() {
    abstract fun geoDao(): DatabaseDao
}