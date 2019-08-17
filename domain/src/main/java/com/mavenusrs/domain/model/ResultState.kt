package com.mavenusrs.domain.model

import com.mavenusrs.domain.errorChecker.WeatherException

sealed class ResultState<out T: Any>{

    data class Success<T: Any>(val data: T):ResultState<T>()
    data class Failure(val error: WeatherException): ResultState<Nothing>()
    object Loading: ResultState<Nothing>()

}