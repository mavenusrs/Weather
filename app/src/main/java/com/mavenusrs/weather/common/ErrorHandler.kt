package com.mavenusrs.weather.common

import android.content.Context
import com.mavenusrs.domain.errorChecker.WeatherException
import com.mavenusrs.weather.R

class ErrorHandler (val weatherException: WeatherException){
    fun getErrorMessage(context: Context): String{
        return weatherException.message?: context.getString(R.string.general_error_message)
    }
}