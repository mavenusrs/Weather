package com.mavenusrs.data.repository

import com.mavenusrs.data.common.WEATHER_KEY
import com.mavenusrs.data.entity.mapper.mapWeather
import com.mavenusrs.data.remote.WeaterApi
import com.mavenusrs.domain.model.Weather
import com.mavenusrs.domain.repository.WeatherRespository
import io.reactivex.Single

class WeatherRespositoryImpl(private val weatherApi: WeaterApi) :WeatherRespository{

    override fun getWeatherForcast(query: String, numberOfDays: Int): Single<Weather> {
        return weatherApi.forcast(WEATHER_KEY, query, days = numberOfDays)
           .map {
            mapWeather(it)
        }
    }

}