package com.mavenusrs.domain.usecase

import com.mavenusrs.domain.errorChecker.WeatherException
import com.mavenusrs.domain.model.ResultState
import io.reactivex.Single

abstract class SingleUsecaseWithParams<P, R : Any> {

    fun execute(params: P): Single<ResultState<R>> {
        return try {
            validate(params)
            run(params)
        } catch (weatherException: WeatherException) {
            Single.error(weatherException)
        }
    }

    abstract fun validate(params: P)

    abstract fun run(params: P): Single<ResultState<R>>
}