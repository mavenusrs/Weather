package com.mavenusrs.weather

import android.app.Application
import com.mavenusrs.weather.di.ApplicationComponent
import com.mavenusrs.weather.di.ApplicationModule
import com.mavenusrs.weather.di.DaggerApplicationComponent

class MyApplication : Application() {
    private lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        inject()
    }

    private fun inject() {
        component = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
        component.inject(this)
    }

    fun getComponent(): ApplicationComponent {
        return component
    }
}