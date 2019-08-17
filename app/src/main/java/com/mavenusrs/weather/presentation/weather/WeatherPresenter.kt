package com.mavenusrs.weather.presentation.weather

import android.location.Location
import android.util.Log
import com.mavenusrs.domain.common.GENERAL_SERVER_ERROR
import com.mavenusrs.domain.errorChecker.WeatherException
import com.mavenusrs.domain.model.ResultState
import com.mavenusrs.domain.model.Weather
import com.mavenusrs.domain.model.WeatherRequest
import com.mavenusrs.domain.usecase.WeatherUseCase
import com.mavenusrs.weather.common.BehaviorSubjectTrigger
import com.mavenusrs.weather.common.addsTo
import com.mavenusrs.weather.location.MyLocation
import com.mavenusrs.weather.model.WeatherViewModel
import com.mavenusrs.weather.model.mapWeatherViewModel
import com.mavenusrs.weather.permission.PermissionHandler
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Named

class WeatherPresenter @Inject constructor(
    private val weatherUseCase: WeatherUseCase,
    private val permissionHandler: PermissionHandler,
    private val disposableComposite: CompositeDisposable,
    private val myLocation: MyLocation,
    @Named("subscribe") private val subscriberSchedular: Scheduler,
    @Named("observer") private val observerSchedular: Scheduler) {

    val permissionBehaviorSubjectTrigger = BehaviorSubjectTrigger<PermissionHandler.PermissionResult>()
    val showNotificationProviderDisabled = BehaviorSubjectTrigger<String>()

    val weatherSubjectTrigger = BehaviorSubjectTrigger<WeatherViewModel>()
    val loadingSubjectTrigger = BehaviorSubjectTrigger<ResultState.Loading>()
    val errorSubjectTrigger = BehaviorSubjectTrigger<WeatherException>()


    fun onForecastRequested() {
        if (!permissionHandler.checkHasPermission()) {
            permissionHandler.requestPermission().subscribe(::handlePermissionResult)
                .addsTo(disposableComposite)
        } else {
            myLocation.requestLocation()
                ?.subscribe(::getForcastWeather)
                ?.addsTo(disposableComposite)

        }

        providerDisableObserver()
    }


    fun getForcastWeather(location: Location) {

        val weatherRequest = WeatherRequest("${location.latitude},${location.longitude}", 4)
        weatherUseCase.execute(weatherRequest)
            .startWith(ResultState.Loading)
            .subscribeOn(subscriberSchedular)
            .observeOn(observerSchedular)
            .subscribe({
                handleResponse(it)
            }, {
                handleErrorResponse(WeatherException(GENERAL_SERVER_ERROR, it.message))
            }).addsTo(disposableComposite)


    }

    fun unBound() {
        disposableComposite.clear()
    }

    private fun handleResponse(resultState: ResultState<Weather>) {
        when (resultState) {
            is ResultState.Success -> handleSuccessfullyResponse(resultState.data)
            is ResultState.Failure -> handleErrorResponse(resultState.error)
            is ResultState.Loading -> handleloadingResponse(resultState)

        }
    }

    private fun handleSuccessfullyResponse(weather: Weather) {
        handleloadingResponse(null)
        weatherSubjectTrigger.trigger(mapWeatherViewModel(weather))
        Log.d("WeatherPresenter:", weather.toString())

    }

    private fun handleErrorResponse(weatherException: WeatherException) {
        handleloadingResponse(null)
        errorSubjectTrigger.trigger(weatherException)

    }

    private fun handleloadingResponse(loadingStatus: ResultState.Loading?) {
        if (loadingStatus != null) {
            loadingSubjectTrigger.trigger(loadingStatus)
        }

    }

    private fun handlePermissionResult(permissionResult: PermissionHandler.PermissionResult) {
        permissionBehaviorSubjectTrigger.trigger(permissionResult)
        permissionBehaviorSubjectTrigger.observer().subscribe {
            if (it == PermissionHandler.PermissionResult.GRANTED)
                onForecastRequested()
        }.addsTo(disposableComposite)
    }

    private fun providerDisableObserver() {
        myLocation.disabledNotificationSubjectTrigger.observer().subscribe {
            it?.let {
                showNotificationProviderDisabled.trigger(value = it)
            }
        }.addsTo(disposableComposite)
    }

}

