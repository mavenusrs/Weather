package com.mavenusrs.domain.usecase

import com.mavenusrs.domain.common.GENERAL_SERVER_ERROR
import com.mavenusrs.domain.errorChecker.WeatherException
import com.mavenusrs.domain.model.ResultState
import com.mavenusrs.domain.model.Weather
import com.mavenusrs.domain.model.WeatherRequest
import com.mavenusrs.domain.repository.WeatherRespository
import io.reactivex.Observable
import retrofit2.HttpException

class WeatherUseCase(private val weatherRespository: WeatherRespository) : ObservableUsecaseWithParams<WeatherRequest, Weather>() {

    override fun run(params: WeatherRequest): Observable<ResultState<Weather>> {

        return weatherRespository.getWeatherForcast(params.query, params.numberOfDays)
            .toObservable()
            .map {
                ResultState.Success(it) as ResultState<Weather>
            }.onErrorReturn {
                if (it is HttpException)
                    ResultState.Failure(WeatherException(it.code(), it.message))
                else
                    ResultState.Failure(WeatherException(GENERAL_SERVER_ERROR, it.message))
            }
    }

    override fun validate(params: WeatherRequest) {
        params.validate()
    }
}

