package com.mavenusrs.domain.model

data class Weather (val locationName: String,
                    val currentTempC: Double, var forecastday: List<Forecastday>){
    override fun toString(): String {
        return "Country ${locationName} currentTempC: ${currentTempC} forecastdays: ${forecastday.map {
            "$it,"
        }}"
    }

    override fun equals(other: Any?): Boolean {
        other?.apply {
            when(this){
                is Weather -> return (other as Weather).toString() == toString()
            }
        }
        return false
    }
}