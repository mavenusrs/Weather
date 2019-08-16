package com.mavenusrs.data.entity.mapper

import com.mavenusrs.data.entity.ForecastdayEntity
import com.mavenusrs.data.entity.WeatherEntity
import com.mavenusrs.domain.model.Forecastday
import com.mavenusrs.domain.model.Weather

fun mapWeather(weatherEntity: WeatherEntity): Weather{
    val counteryName = weatherEntity.location.country
    val currentTempC = weatherEntity.currentEntity.tempC
    val forcaseDayList = arrayListOf<Forecastday>()

    weatherEntity.forecastEntity.forecastdayEntity.map {
        forcaseDayList.add(mapForcastDay(it))
    }

    return Weather(counteryName, currentTempC = currentTempC, forecastday = forcaseDayList)
}

fun mapForcastDay(forcastDayEntity: ForecastdayEntity): Forecastday{
    return Forecastday(forcastDayEntity.date, forcastDayEntity.dayEntity.avgtempC)
}