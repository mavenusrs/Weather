package com.mavenusrs.weather.common

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.text.SimpleDateFormat
import java.util.*

fun Disposable.addsTo(disposableComposite: CompositeDisposable) {
    disposableComposite.add(this)
}

fun Double.getDegreeWithCelsiusSympol(): String {
    return "${this} \u00B0"
}

fun String.toDayOfTheWeek(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    val date = sdf.parse(this)

    val sdf2 = SimpleDateFormat("EEEE", Locale.ENGLISH)
    return sdf2.format(date)

}