package com.mavenusrs.weather.location

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import com.mavenusrs.weather.common.BehaviorSubjectTrigger
import io.reactivex.Single
import io.reactivex.subjects.AsyncSubject
import java.lang.ref.WeakReference

class MyLocation(private val activityRef: WeakReference<Activity>) : LocationListener {


    lateinit var locationSubject: AsyncSubject<Location>
    val disabledNotificationSubjectTrigger = BehaviorSubjectTrigger<String>()

    private lateinit var locationManager: LocationManager

    @SuppressLint("MissingPermission")
    fun requestLocation(): Single<Location>? {
        locationSubject = AsyncSubject.create()

        var location: Location? = null
        activityRef.get()?.apply {
            locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val bestProvider = locationManager.getBestProvider(Criteria(), false)
            location = locationManager.getLastKnownLocation(bestProvider)
            locationManager.requestLocationUpdates(bestProvider, 2000, 5f, this@MyLocation)
        }

        if (location != null)
            return Single.just(location)

        return locationSubject.singleOrError()
    }

    override fun onLocationChanged(location: Location?) {
        location?.apply {
            locationManager.removeUpdates(this@MyLocation)

            locationSubject.onNext(this)
            locationSubject.onComplete()
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.d("onStatusChanged", "provider $provider status $status")
    }

    override fun onProviderEnabled(provider: String?) {
        Log.d("onStatusChanged", "" + provider)
    }

    override fun onProviderDisabled(provider: String?) {
        provider?.apply {
            disabledNotificationSubjectTrigger.trigger(this)
        }
        Log.d("onStatusChanged", "" + provider)
    }
}