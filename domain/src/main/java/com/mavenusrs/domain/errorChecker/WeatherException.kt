package com.mavenusrs.domain.errorChecker

class WeatherException(val code: Int?, override val message: String?): Throwable(message)