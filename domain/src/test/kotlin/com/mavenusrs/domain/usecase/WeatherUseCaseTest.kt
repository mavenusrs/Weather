package com.mavenusrs.domain.usecase

import com.mavenusrs.domain.common.GENERAL_SERVER_ERROR
import com.mavenusrs.domain.common.LAT_LON_CODE_ERROR
import com.mavenusrs.domain.common.NUMBER_OF_DAYS_CODE_ERROR
import com.mavenusrs.domain.errorChecker.WeatherException
import com.mavenusrs.domain.model.ResultState
import com.mavenusrs.domain.model.Weather
import com.mavenusrs.domain.model.WeatherRequest
import com.mavenusrs.domain.repository.WeatherRespository
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyList
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WeatherUseCaseTest {

    @Mock
    lateinit var weatherRepository: WeatherRespository

    lateinit var weatherUseCase: WeatherUseCase

    val validQuery = "31.23,32.2423"
    val numberOfDays = 4

    val emptyQuery = ""
    val invalidQuery = "31.2323,a"
    val invalidNumberOfDays = 0


    @Before
    fun setup(){
        weatherUseCase = WeatherUseCase(weatherRepository)
    }


    @Test
    fun `check validation of number of days is invalid`(){
        val codeError = NUMBER_OF_DAYS_CODE_ERROR

        weatherUseCase.execute(WeatherRequest(validQuery, invalidNumberOfDays))
            .test().assertError {
                (it as WeatherException).code == codeError
            }

    }

    @Test
    fun `check validation of latlon is invalid`(){
        val codeError = LAT_LON_CODE_ERROR

        weatherUseCase.execute(WeatherRequest(invalidQuery, numberOfDays))
            .test().assertError {
                (it as WeatherException).code == codeError
            }

    }

    @Test
    fun `check validation of latlon is empty`(){
        val codeError = LAT_LON_CODE_ERROR

        weatherUseCase.execute(WeatherRequest(emptyQuery, numberOfDays))
            .test().assertError {
                (it as WeatherException).code == codeError
            }

    }

    @Test
    fun `check other error occure from server side`(){
        val codeError = GENERAL_SERVER_ERROR
        `when`(weatherRepository.getWeatherForcast(validQuery, numberOfDays))
            .thenReturn(Single.error(WeatherException(codeError, "")))

        weatherUseCase.execute(WeatherRequest(validQuery, numberOfDays))
            .test().assertValueAt(0) {
                (it as ResultState.Failure).error.code == codeError
            }
    }

    @Test
    fun `check get Forcast successfully returned`(){
        val weather = Weather("Egypt", 45.0, arrayListOf())
        `when`(weatherRepository.getWeatherForcast(validQuery, numberOfDays))
            .thenReturn(Single.just(weather))

        weatherUseCase.execute(WeatherRequest(validQuery, numberOfDays))
            .test()
            .assertNoErrors()
            .assertValueAt(0) {
                (it as ResultState.Success).data == weather
            }
    }

}