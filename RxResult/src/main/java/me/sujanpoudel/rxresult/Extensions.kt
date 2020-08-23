@file:JvmName("RxResult")
@file:JvmMultifileClass

package me.sujanpoudel.rxresult

import android.content.Intent
import androidx.annotation.CheckResult
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import io.reactivex.Observable
import io.reactivex.Single


@CheckResult
fun <T> AppCompatActivity.startForResult(intent: Intent, resultExtractor: (Intent) -> T): Single<T> {
    return ActivityResultSingle(this, intent, resultExtractor)
}

@CheckResult
fun AppCompatActivity.startForResult(intent: Intent) = startForResult(intent) { it }

@CheckResult
fun <T> Fragment.startForResult(intent: Intent, resultExtractor: (Intent) -> T): Single<T> {
    return ActivityResultSingle(this, intent, resultExtractor)
}

@CheckResult
fun Fragment.startForResult(intent: Intent) = startForResult(intent) { it }

@CheckResult
fun <T> AppCompatActivity.startForResult(intent: Observable<Intent>, resultExtractor: (Intent) -> T): Observable<T> {
    return intent
        .flatMap {
            ActivityResultSingle(this, it, resultExtractor).toObservable()
        }
}

@CheckResult
fun AppCompatActivity.startForResult(intent: Observable<Intent>) = startForResult(intent) { it }

@CheckResult
fun <T> Fragment.startForResult(intent: Observable<Intent>, resultExtractor: (Intent) -> T): Observable<T> {
    return intent.flatMap {
        ActivityResultSingle(this, it, resultExtractor).toObservable()
    }
}

@CheckResult
fun Fragment.startForResult(intent: Observable<Intent>) = startForResult(intent) { it }
