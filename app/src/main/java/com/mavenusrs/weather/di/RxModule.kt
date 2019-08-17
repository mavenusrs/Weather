package com.mavenusrs.weather.di

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton

@Module
class RxModule {

    @Singleton
    @Provides
    @Named("subscribe")
    fun providesSubscribeSchedular() : Scheduler {
        return Schedulers.io()
    }

    @Singleton
    @Provides
    @Named("observer")
    fun providesObserverSchedular() : Scheduler {
        return AndroidSchedulers.mainThread()
    }

}