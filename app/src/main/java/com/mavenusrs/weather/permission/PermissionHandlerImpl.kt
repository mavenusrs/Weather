package com.mavenusrs.weather.permission

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import io.reactivex.Single
import io.reactivex.subjects.AsyncSubject
import java.lang.ref.WeakReference

class PermissionHandlerImpl(
    val activityRef: WeakReference<Activity>,
    val permissions: Array<String>, val requestCode: Int) : PermissionHandler {

    lateinit var subject: AsyncSubject<PermissionHandler.PermissionResult>


    override fun checkHasPermission(): Boolean {
        activityRef.get()?.apply {
            permissions.map {
                if (ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED)
                    return false
            }

            return true
        }

        return false
    }

    override fun requestPermission(): Single<PermissionHandler.PermissionResult> {
        subject = AsyncSubject.create<PermissionHandler.PermissionResult>()

        activityRef.get()?.apply {
            ActivityCompat.requestPermissions(this, permissions, requestCode)
        }

        return subject.firstOrError()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onPermissionResult(granted: Boolean) {

        if (granted) {
            subject.run {
                onNext(PermissionHandler.PermissionResult.GRANTED)
            }
        } else {
            with(subject) {
                permissions.map {
                    onNext(
                        if (activityRef.get()?.shouldShowRequestPermissionRationale(it) == true)
                            PermissionHandler.PermissionResult.DENIED_HARD
                        else
                            PermissionHandler.PermissionResult.DENIED_SOFT
                    )
                }
            }
        }
        subject.onComplete()
    }
}