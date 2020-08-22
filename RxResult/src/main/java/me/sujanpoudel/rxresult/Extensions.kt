package me.sujanpoudel.rxresult

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import io.reactivex.Observable
import io.reactivex.Single

fun <T> AppCompatActivity.startForResult(intent: Intent, resultExtractor: (Intent) -> T): Single<T> {
    return ActivityResultSingle(this, intent, resultExtractor)
}

fun AppCompatActivity.startForResult(intent: Intent) = startForResult(intent) { it }


fun <T> Fragment.startForResult(intent: Intent, resultExtractor: (Intent) -> T): Single<T> {
    return ActivityResultSingle(this, intent, resultExtractor)
}

fun Fragment.startForResult(intent: Intent) = startForResult(intent) { it }


fun <T> AppCompatActivity.startForResult(intent: Observable<Intent>, resultExtractor: (Intent) -> T): Observable<T> {
    return intent
        .flatMap {
            ActivityResultSingle(this, it, resultExtractor).toObservable()
        }
}

fun AppCompatActivity.startForResult(intent: Observable<Intent>) = startForResult(intent) { it }


fun <T> Fragment.startForResult(intent: Observable<Intent>, resultExtractor: (Intent) -> T): Observable<T> {
    return intent.flatMap {
        ActivityResultSingle(this, it, resultExtractor).toObservable()
    }
}

fun Fragment.startForResult(intent: Observable<Intent>) = startForResult(intent) { it }
