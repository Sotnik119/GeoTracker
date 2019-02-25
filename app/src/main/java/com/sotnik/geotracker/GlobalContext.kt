package com.sotnik.geotracker

import android.arch.persistence.room.Room
import android.content.Context
import com.sotnik.geotracker.data.GeoDatabase
import java.text.SimpleDateFormat
import java.util.*


/**
 * синглтон для контекста и тп
 */
object ContextHelper {
    private var database: GeoDatabase? = null

    fun init(context: Context) {
        if (database == null) database = Room.databaseBuilder(
            context,
            GeoDatabase::class.java, "GeoTrackerDatabase"
        ).build()
    }

    fun getDatabase(): GeoDatabase? {
        return database
    }

    fun longToStringTime(long: Long?): String? {
        return try {
            val date = Date(long!!)
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            formatter.format(date)
        } catch (e: Exception) {
            ""
        }

    }

}