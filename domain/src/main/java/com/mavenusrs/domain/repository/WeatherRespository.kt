package com.mavenusrs.domain.repository

import com.mavenusrs.domain.model.Weather
import io.reactivex.Single

interface WeatherRespository{
    fun getWeatherForcast( query: String, numberOfDays: Int):Single<Weather>
}