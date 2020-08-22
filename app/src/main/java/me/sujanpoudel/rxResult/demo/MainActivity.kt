package me.sujanpoudel.rxResult.demo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import me.sujanpoudel.rxresult2.startForResult
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private val subject = PublishSubject.create<Intent>()
    private val disposeBag = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        disposeBag.add(startForResult(subject)
            .subscribe { it ->
                Log.d("result is", "${it}")
            })

        disposeBag.add(startForResult(Intent.createChooser(intent, "Select Picture"))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it ->
                Log.d("result first", "${it}")
                disposeBag.clear()
                second()
            })
    }

    private fun second() {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        disposeBag.add(startForResult(Intent.createChooser(intent, "Select Video"))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it ->
                Log.d("result second", "${it}")
                subject.onNext(intent)
            })
    }
}