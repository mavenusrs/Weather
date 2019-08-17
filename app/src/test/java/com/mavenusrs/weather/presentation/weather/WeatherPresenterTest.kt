package com.mavenusrs.weather.presentation.weather

import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mavenusrs.domain.errorChecker.WeatherException
import com.mavenusrs.domain.model.ResultState
import com.mavenusrs.domain.model.Weather
import com.mavenusrs.domain.model.WeatherRequest
import com.mavenusrs.domain.usecase.WeatherUseCase
import com.mavenusrs.weather.common.BehaviorSubjectTrigger
import com.mavenusrs.weather.location.MyLocation
import com.mavenusrs.weather.model.WeatherViewModel
import com.mavenusrs.weather.permission.PermissionHandler
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.junit.Before

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WeatherPresenterTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var weatherUseCase: WeatherUseCase

    @Mock
    private lateinit var permissionHandler: PermissionHandler

    @Mock
    private lateinit var location: Location

    @Mock
    private lateinit var myLocation: MyLocation

    private val weatherPresenter by lazy {
        WeatherPresenter(weatherUseCase, permissionHandler, CompositeDisposable(), myLocation,
            Schedulers.trampoline(), Schedulers.trampoline())
    }

    @Mock
    private lateinit var permissionObserver: Observer<PermissionHandler.PermissionResult>
    @Mock
    private lateinit var locationObserver: Observer<String>
    @Mock
    private lateinit var loadingObserver: Observer<ResultState.Loading>
    @Mock
    private lateinit var loadWeatherObserver: Observer<WeatherViewModel>
    @Mock
    private lateinit var errorObserver: Observer<WeatherException>

    private val validQuery = "31.23,32.2423"
    private val numberOfDays = 4
    private val validLon = 32.2423
    private val validLat = 31.23


    private val emptyQuery = ""
    private val invalidQuery = "31.2323,a"
    private val invalidNumberOfDays = 0

    @Before
    fun setUp() {
        weatherPresenter.permissionBehaviorSubjectTrigger.observer().subscribe(permissionObserver)
        weatherPresenter.showNotificationProviderDisabled.observer().subscribe(locationObserver)
        weatherPresenter.loadingSubjectTrigger.observer().subscribe(loadingObserver)
        weatherPresenter.weatherSubjectTrigger.observer().subscribe(loadWeatherObserver)
        weatherPresenter.errorSubjectTrigger.observer().subscribe(errorObserver)
    }

    @Test
    fun `test Load Weather Long Happy Path`(){
        val weatherRequest = WeatherRequest(validQuery, numberOfDays)
        val weather = Weather("Egypt", 44.0, arrayListOf())
        val weatherViewModel = WeatherViewModel("Egypt", 44.0, arrayListOf())

        `when`(location.longitude).thenReturn(validLon)
        `when`(location.latitude).thenReturn(validLat)

        `when`(permissionHandler.checkHasPermission()).thenReturn(true)
        `when`(myLocation.requestLocation()).thenReturn(Single.just(location))
        `when`(myLocation.disabledNotificationSubjectTrigger).thenReturn(BehaviorSubjectTrigger())
        `when`(weatherUseCase.execute(weatherRequest)).thenReturn(
            Observable.just(ResultState.Success<Weather>(weather)))

        weatherPresenter.onForecastRequested()

        Mockito.verify(loadingObserver).onNext(ResultState.Loading)
        Mockito.verify(loadWeatherObserver).onNext(weatherViewModel)

    }

    @Test
    fun `test Load Weather when error occured from backend`(){
        val weatherRequest = WeatherRequest(validQuery, numberOfDays)

        val weatherException = WeatherException(1, "")

        `when`(location.longitude).thenReturn(validLon)
        `when`(location.latitude).thenReturn(validLat)

        `when`(permissionHandler.checkHasPermission()).thenReturn(true)
        `when`(myLocation.requestLocation()).thenReturn(Single.just(location))
        `when`(myLocation.disabledNotificationSubjectTrigger).thenReturn(BehaviorSubjectTrigger())
        `when`(weatherUseCase.execute(weatherRequest)).thenReturn(
            Observable.just(ResultState.Failure(weatherException)))

        weatherPresenter.onForecastRequested()

        Mockito.verify(loadingObserver).onNext(ResultState.Loading)

        Mockito.verify(errorObserver).onNext(weatherException)

    }
    


    @Test
    fun `test there is no permission for location`(){
        `when`(permissionHandler.checkHasPermission()).thenReturn(false)
        `when`(permissionHandler.requestPermission()).thenReturn(Single.just(PermissionHandler.PermissionResult.GRANTED))
        `when`(myLocation.disabledNotificationSubjectTrigger).thenReturn(BehaviorSubjectTrigger())

        weatherPresenter.onForecastRequested()

        Mockito.verify(permissionHandler, times(1)).requestPermission()

    }

    @Test
    fun `test when permission is not granted`(){

        val weatherRequest = WeatherRequest(validQuery, numberOfDays)
        val weather = Weather("Egypt", 44.0, arrayListOf())

        `when`(permissionHandler.checkHasPermission()).thenReturn(false)
        `when`(permissionHandler.requestPermission()).thenReturn(Single.just(PermissionHandler.PermissionResult.DENIED_SOFT))
        `when`(myLocation.disabledNotificationSubjectTrigger).thenReturn(BehaviorSubjectTrigger())

        weatherPresenter.onForecastRequested()

        Mockito.verify(permissionHandler, times(1)).requestPermission()
        Mockito.verify(myLocation, times(0)).requestLocation()
        Mockito.verify(weatherUseCase, times(0)).execute(weatherRequest)


    }
}