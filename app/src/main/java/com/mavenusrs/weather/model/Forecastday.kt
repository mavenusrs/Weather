package com.mavenusrs.weather.model

import java.io.Serializable

data class ForecastdayViewModel (var date: String, val dayAvgtempC: Double):Serializable{
    override fun toString(): String {
        return "Data: ${date} dayAvgtempC ${dayAvgtempC}"

    }
}