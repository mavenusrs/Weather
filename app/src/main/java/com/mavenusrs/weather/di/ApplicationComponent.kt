package com.mavenusrs.weather.di

import com.mavenusrs.weather.MyApplication
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, RxModule::class])
interface ApplicationComponent{

    fun inject(con: MyApplication)

    fun plus(domainModule: DomainModule, endpointModule: EndpointModule, handlerModule: HandlerModule): MainComponent
}