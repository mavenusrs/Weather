package com.mavenusrs.weather.presentation.weather

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.mavenusrs.weather.R
import com.mavenusrs.weather.common.LOCATION_REQUEST_CODE
import com.mavenusrs.weather.common.addsTo
import com.mavenusrs.weather.model.ForecastdayViewModel
import com.mavenusrs.weather.permission.PermissionDispatcher
import com.mavenusrs.weather.permission.PermissionHandler
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.mavenusrs.domain.errorChecker.WeatherException
import com.mavenusrs.weather.MyApplication
import com.mavenusrs.weather.common.ErrorHandler
import com.mavenusrs.weather.common.getDegreeWithCelsiusSympol
import com.mavenusrs.weather.di.DomainModule
import com.mavenusrs.weather.di.EndpointModule
import com.mavenusrs.weather.di.HandlerModule
import com.mavenusrs.weather.model.WeatherViewModel
import kotlinx.android.synthetic.main.erorr_view.*
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var weatherPresenter: WeatherPresenter
    @Inject
    lateinit var permissionDispatcher: PermissionDispatcher

    private val disposableComposite: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        injectionDI()

        weatherPresenter.onForecastRequested()

        observeToWeatherRquest()

        observerPermission()

        observeLocation()
    }

    private fun injectionDI() {
        val mainComponent = (application as MyApplication).getComponent()
            .plus(DomainModule(), EndpointModule(), HandlerModule(this))
        mainComponent.inject(this)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == LOCATION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            permissionDispatcher.dispatchResult(true)
        else
            permissionDispatcher.dispatchResult(false)

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


    }

    private fun observeToWeatherRquest() {

        weatherPresenter.loadingSubjectTrigger.observer().subscribe {
            showProgress(true)
            showErrorLayout(false)

        }.addsTo(disposableComposite)

        weatherPresenter.weatherSubjectTrigger.observer().subscribe {

            showProgress(false)
            showErrorLayout(false)

            setupLayoutForWeather(it)

        }.addsTo(disposableComposite)

        weatherPresenter.errorSubjectTrigger.observer().subscribe {
            showProgress(false)
            showError(it)
        }.addsTo(disposableComposite)
    }

    private fun setupLayoutForWeather(it: WeatherViewModel) {
        country.text = it.locationName
        weatherDegreeTV.text = it.currentTempC.getDegreeWithCelsiusSympol()
        setupAdapter(it.forecastday)
    }

    private fun showError(weatherException: WeatherException?) {
        showErrorLayout(true)
        retry.setOnClickListener { weatherPresenter.onForecastRequested() }
        weatherErrorTV.text = weatherException?.let { ErrorHandler(it).getErrorMessage(this@MainActivity) }
    }

    private fun showErrorLayout(show: Boolean) {
        errorLyt.visibility = if (show) View.VISIBLE else View.GONE
    }


    fun showProgress(show: Boolean) {
        loadingProgressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun observerPermission() {
        weatherPresenter.permissionBehaviorSubjectTrigger.observer().subscribe {
            when (it) {
                PermissionHandler.PermissionResult.GRANTED ->
                    weatherPresenter.onForecastRequested()
                PermissionHandler.PermissionResult.DENIED_HARD ->
                    Toast.makeText(
                        this@MainActivity,
                        "Location permission Must be enabled to use this feature",
                        Toast.LENGTH_SHORT
                    ).show()
                else -> Toast.makeText(
                    this@MainActivity,
                    "Please, enable Location permission",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }.addsTo(disposableComposite)

    }

    private fun observeLocation() {
        weatherPresenter.showNotificationProviderDisabled.observer().subscribe {
            Toast.makeText(this@MainActivity, "please, enable location provider ${it ?: ""}", Toast.LENGTH_SHORT).show()
        }.addsTo(disposableComposite)

    }

    private fun setupAdapter(forcastDays: List<ForecastdayViewModel>) {
        weatherRV.setHasFixedSize(true)
        weatherRV.layoutManager = LinearLayoutManager(this)

        val itemDecor = DividerItemDecoration(this, HORIZONTAL)
        weatherRV.addItemDecoration(itemDecor)

        val adapter = WeatherAdapter(forcastDays)
        weatherRV.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        weatherPresenter.unBound()
        disposableComposite.clear()
    }
}

