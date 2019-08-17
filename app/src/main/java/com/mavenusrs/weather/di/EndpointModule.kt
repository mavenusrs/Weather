package com.mavenusrs.weather.di

import com.mavenusrs.data.remote.WeaterApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class EndpointModule {

    @PerScreen
    @Provides
    fun provideWeatherApi(retrofit: Retrofit): WeaterApi {
        return retrofit.create(WeaterApi::class.java)
    }
}