package com.mavenusrs.domain.usecase

import com.mavenusrs.domain.common.GENERAL_SERVER_ERROR
import com.mavenusrs.domain.errorChecker.WeatherException
import com.mavenusrs.domain.model.ResultState
import com.mavenusrs.domain.model.Weather
import com.mavenusrs.domain.model.WeatherRequest
import com.mavenusrs.domain.repository.WeatherRespository
import io.reactivex.Single
import javax.xml.ws.http.HTTPException

class WeatherUseCase(val weatherRespository: WeatherRespository) : SingleUsecaseWithParams<WeatherRequest, Weather>() {
    override fun run(params: WeatherRequest): Single<ResultState<Weather>> {

        return weatherRespository.getWeatherForcast(params.query, params.numberOfDays)
            .map {
                ResultState.Success(it) as ResultState<Weather>
            }.onErrorReturn {
                if (it is HTTPException)
                    ResultState.Failure(WeatherException(it.statusCode, it.message))
                else
                    ResultState.Failure(WeatherException(GENERAL_SERVER_ERROR, it.message))
            }
    }

    override fun validate(params: WeatherRequest) {
        params.validate()
    }
}

