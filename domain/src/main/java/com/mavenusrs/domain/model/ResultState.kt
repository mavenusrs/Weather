package com.mavenusrs.domain.model

import com.mavenusrs.domain.errorChecker.WeatherException

sealed class ResultState<T>{
    data class Success<T: Any>(val data: T):ResultState<T>()
    data class Failure<T>(val error: WeatherException): ResultState<T>()
    object Loading: ResultState<Nothing>()
}