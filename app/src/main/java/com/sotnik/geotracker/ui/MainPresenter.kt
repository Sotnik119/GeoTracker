package com.sotnik.geotracker.ui

import android.content.Intent
import com.sotnik.geotracker.data.DataProcessor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainPresenter {

    private lateinit var activity: MainActivity
    private val db = DataProcessor()
    private var disposable = CompositeDisposable()

    fun bindMainActivity(a: MainActivity) {
        activity = a
        activity.floatingActionButton.setOnClickListener { createNewRoute() }
    }

    fun loadData() {
        disposable.add(db.getAllRoutes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it.isNotEmpty()) {
                        activity.apply {
                            hideList()
                            showLoad()
                            updateList(it)
                            showList()
                            hideLoad()
                            hideNoItem()
                        }
                    } else {
                        activity.apply {
                            showNoItems()
                            hideLoad()
                            hideList()
                        }
                    }
                },
                {
                    activity.apply {
                        showNoItems()
                        hideList()
                        hideLoad()
                    }
                    it.printStackTrace()
                }
            ))
    }

    fun pause() {
        //Пересоздаем обьекты
        disposable.dispose()
        disposable = CompositeDisposable()

    }

    fun createNewRoute() {
        disposable.add(
            Observable.fromCallable { db.createRoute() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { id ->
                    activity.startActivity(Intent(activity, DetailActivity::class.java).putExtra("routeId", id))
                }
        )
    }
}