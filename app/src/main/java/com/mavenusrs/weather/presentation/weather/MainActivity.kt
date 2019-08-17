package com.mavenusrs.weather.presentation.weather

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mavenusrs.data.common.BASE_URL
import com.mavenusrs.data.remote.WeaterApi
import com.mavenusrs.data.repository.WeatherRespositoryImpl
import com.mavenusrs.domain.usecase.WeatherUseCase
import com.mavenusrs.weather.R
import com.mavenusrs.weather.common.LOCATION_REQUEST_CODE
import com.mavenusrs.weather.common.addsTo
import com.mavenusrs.weather.location.MyLocation
import com.mavenusrs.weather.permission.PermissionDispatcher
import com.mavenusrs.weather.permission.PermissionHandler
import com.mavenusrs.weather.permission.PermissionHandlerImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.ref.WeakReference
import java.security.Permission

class MainActivity : AppCompatActivity() {

    private val disposableComposite: CompositeDisposable = CompositeDisposable()
    private lateinit var weatherPresenter: WeatherPresenter
    private lateinit var permissionDispatcher: PermissionDispatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherPresenter = getPresenter()

        weatherPresenter.onForecastRequested()

        observeToWeatherRquest()

        observerPermission()

        observeLocation()
    }

    private fun getPresenter(): WeatherPresenter {
        val okHttpClientBuilder = OkHttpClient.Builder()
        val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClientBuilder.build())
            .build()
        val weaterApi = retrofit.create(WeaterApi::class.java)

        val permissionHandler = PermissionHandlerImpl(
            WeakReference(this), arrayOf(
                ACCESS_COARSE_LOCATION,
                ACCESS_FINE_LOCATION
            ), LOCATION_REQUEST_CODE
        )


        permissionDispatcher = PermissionDispatcher(permissionHandler)
        return WeatherPresenter(
            WeatherUseCase(WeatherRespositoryImpl(weaterApi)),
            permissionHandler,
            CompositeDisposable(), MyLocation(WeakReference(this)), Schedulers.io(),
            AndroidSchedulers.mainThread()
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
       if (requestCode == LOCATION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED)
           permissionDispatcher.dispatchResult(true)
        else
           permissionDispatcher.dispatchResult(false)

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


    }

    private fun observeToWeatherRquest(){
        weatherPresenter.loadingSubjectTrigger.observer().subscribe {
            Toast.makeText(this@MainActivity, "Loading", Toast.LENGTH_SHORT).show()
        }.addsTo(disposableComposite)

        weatherPresenter.weatherSubjectTrigger.observer().subscribe {
            Toast.makeText(this@MainActivity, it?.toString()?:"", Toast.LENGTH_SHORT).show()
        }.addsTo(disposableComposite)

        weatherPresenter.errorSubjectTrigger.observer().subscribe {
            Toast.makeText(this@MainActivity, "error code: ${it?.code?:0} error ${it?.message?:""}", Toast.LENGTH_SHORT).show()
        }.addsTo(disposableComposite)
    }

    private fun observerPermission(){
        weatherPresenter.permissionBehaviorSubjectTrigger.observer().subscribe {
            when(it){
                PermissionHandler.PermissionResult.GRANTED ->
                    weatherPresenter.onForecastRequested()
                PermissionHandler.PermissionResult.DENIED_HARD ->
                    Toast.makeText(this@MainActivity, "Location permission Must be enabled to use this feature", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(this@MainActivity, "Please, enable Location permission", Toast.LENGTH_SHORT).show()

            }
        }.addsTo(disposableComposite)

    }

    private fun observeLocation(){
        weatherPresenter.showNotificationProviderDisabled.observer().subscribe {
            Toast.makeText(this@MainActivity, "please, enable location provider ${it?:""}", Toast.LENGTH_SHORT).show()
        }.addsTo(disposableComposite)

    }
}
