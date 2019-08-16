package com.mavenusrs.domain.model

data class Forecastday (var date: String, val dayAvgtempC: Double){
    override fun toString(): String {
        return "Data: ${date} dayAvgtempC ${dayAvgtempC}"

    }
}