package com.mavenusrs.weather.common

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.addsTo(disposableComposite: CompositeDisposable) {
    disposableComposite.add(this)
}