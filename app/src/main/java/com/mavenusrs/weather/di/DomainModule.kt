package com.mavenusrs.weather.di

import com.mavenusrs.data.remote.WeaterApi
import com.mavenusrs.data.repository.WeatherRespositoryImpl
import com.mavenusrs.domain.repository.WeatherRespository
import com.mavenusrs.domain.usecase.WeatherUseCase
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class DomainModule {

    @PerScreen
    @Provides
    fun provideWeatherRespository(weatherApi: WeaterApi): WeatherRespository {
        return WeatherRespositoryImpl(weatherApi)
    }

    @PerScreen
    @Provides
    fun provideWeatherUseCase(weatherRespository: WeatherRespository): WeatherUseCase {
        return WeatherUseCase(weatherRespository)
    }


    @PerScreen
    @Provides
    fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }
}