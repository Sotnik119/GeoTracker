package com.sotnik.geotracker.data

import android.arch.persistence.room.*
import io.reactivex.*

@Dao
interface DatabaseDao {

    // points

    @Insert
    fun insertPoint(point: Point)

    @Delete
    fun deletePoint(point: Point)

    @Query("Select * from points where routeId = :routeId")
    fun getPointsForRoute(routeId: Long): Flowable<List<Point>>


    ///routes

    @Insert
    fun insertRoute(route: Route): Long

    @Update
    fun updateRoute(route: Route): Int

    @Delete
    fun deleteRoute(route: Route)

    @Query("Select * from routes")
    fun getAllRoutes(): Flowable<List<Route>>

    @Query("Select * from routes where id = :id")
    fun getRoute(id: Long): Flowable<Route>
}