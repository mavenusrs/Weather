package com.mavenusrs.weather.di

import com.mavenusrs.weather.presentation.weather.MainActivity
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [DomainModule::class, EndpointModule::class, HandlerModule::class])
interface MainComponent{
    fun inject(mainActivity: MainActivity)
}