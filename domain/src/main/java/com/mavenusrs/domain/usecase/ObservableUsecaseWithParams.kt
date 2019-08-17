package com.mavenusrs.domain.usecase

import com.mavenusrs.domain.errorChecker.WeatherException
import com.mavenusrs.domain.model.ResultState
import io.reactivex.Observable

abstract class ObservableUsecaseWithParams<P, R : Any> {

    fun execute(params: P): Observable<ResultState<R>> {
        return try {
            validate(params)
            run(params)
        } catch (weatherException: WeatherException) {
            Observable.error(weatherException)
        }
    }

    abstract fun validate(params: P)

    abstract fun run(params: P): Observable<ResultState<R>>
}