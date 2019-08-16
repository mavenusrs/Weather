package com.mavenusrs.domain.model

import com.mavenusrs.domain.common.LAT_LON_CODE_ERROR
import com.mavenusrs.domain.common.NUMBER_OF_DAYS_CODE_ERROR
import com.mavenusrs.domain.errorChecker.WeatherException

data class WeatherRequest(val query: String, val numberOfDays: Int){

    fun validate() {
        if (numberOfDays <= 0){
            throw WeatherException(NUMBER_OF_DAYS_CODE_ERROR, "")
        }
        if (isLatLanInvalid()){
            throw WeatherException(LAT_LON_CODE_ERROR, "")
        }

    }

    private fun isLatLanInvalid(): Boolean {
        val latLan = query.split(",")

        if (latLan[0].isEmpty() || latLan[0].isEmpty())
            return true

        try {
            val lat = latLan[0].toDouble()
            val lon = latLan[1].toDouble()


            if (lat < -90 && lat > 90)
                return true


            if (lon < -180 && lat > 180)
                return true

        } catch (numberFormatException: NumberFormatException) {
            return true
        }

        return false
    }
}