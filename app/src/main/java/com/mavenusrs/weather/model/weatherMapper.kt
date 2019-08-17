package com.mavenusrs.weather.model

import com.mavenusrs.domain.model.Forecastday
import com.mavenusrs.domain.model.Weather

fun mapWeatherViewModel(weather: Weather): WeatherViewModel {
    return WeatherViewModel(weather.locationName, weather.currentTempC, weather.forecastday.map {
        mapForcastDay(it)
    })
}

fun mapForcastDay(forecastday: Forecastday): ForecastdayViewModel {
    return ForecastdayViewModel(forecastday.date, forecastday.dayAvgtempC)
}