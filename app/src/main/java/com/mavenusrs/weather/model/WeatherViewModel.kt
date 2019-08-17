package com.mavenusrs.weather.model

import java.io.Serializable

data class WeatherViewModel (val locationName: String,
                             val currentTempC: Double, var forecastday: List<ForecastdayViewModel>): Serializable