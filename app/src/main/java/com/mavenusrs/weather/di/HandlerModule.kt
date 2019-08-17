package com.mavenusrs.weather.di

import android.Manifest
import android.app.Activity
import android.content.Context
import com.mavenusrs.weather.common.LOCATION_REQUEST_CODE
import com.mavenusrs.weather.location.MyLocation
import com.mavenusrs.weather.permission.PermissionDispatcher
import com.mavenusrs.weather.permission.PermissionHandler
import com.mavenusrs.weather.permission.PermissionHandlerImpl
import dagger.Module
import dagger.Provides
import java.lang.ref.WeakReference
import javax.inject.Named

@Module
class HandlerModule(private val activity: Activity) {


    @PerScreen
    @Named("Activity")
    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @PerScreen
    @Provides
    fun providePermissionHandler(): PermissionHandler {
        return PermissionHandlerImpl(
            WeakReference(activity), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), LOCATION_REQUEST_CODE
        )
    }

    @PerScreen
    @Provides
    fun providePermissionDispatcher(permissionHandler: PermissionHandler): PermissionDispatcher {
        return PermissionDispatcher(permissionHandler)
    }


    @PerScreen
    @Provides
    fun provideMyLocation(@Named("Activity") activity: Activity): MyLocation {
        return MyLocation(
            WeakReference(activity))
    }


}