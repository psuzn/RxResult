package me.sujanpoudel.rxresult

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.*
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.MainThreadDisposable

internal class ActivityResultSingle<T>(
    private val activity: ActivityResultCaller,
    private val intent: Intent,
    private val resultExtractor: (Intent) -> T
) : Single<T>() {
    override fun subscribeActual(observer: SingleObserver<in T>) {
        val resultLauncher = activity.registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_CANCELED) {
                result.data?.let {
                    observer.onSuccess(resultExtractor(it))
                } ?: kotlin.run {
                    observer.onError(ActivityNullResultException)
                }
            }
        }
        observer.onSubscribe(Listener(resultLauncher))
        resultLauncher.launch(intent)
    }

    private class Listener(
        private val activityResultLauncher: ActivityResultLauncher<*>
    ) : MainThreadDisposable() {
        override fun onDispose() {
            activityResultLauncher.unregister()
        }
    }

}
