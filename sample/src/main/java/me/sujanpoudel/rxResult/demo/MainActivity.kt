package me.sujanpoudel.rxResult.demo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import me.sujanpoudel.rxresult.startForResult


class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var videoView: VideoView

    private lateinit var button: Button
    private lateinit var videoButton: Button


    private val disposeBag = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.iv)
        button = findViewById(R.id.btn_image)
        videoButton = findViewById(R.id.btn_video)
        videoView = findViewById(R.id.video_view)

        multiIntentSource()
    }

    fun simpleExample() {
        disposeBag.add(button.clicks()
            .flatMapSingle {
                startForResult(imagePickerIntent())
            }.subscribe {
                Glide.with(this).load(it.data)
                    .centerCrop()
                    .into(imageView)
            });
    }

    fun exampleWithResultExtractor() {
        disposeBag.add(button.clicks()
            .flatMapSingle {
                startForResult(imagePickerIntent()) {
                    it.data!!
                }
            }.subscribe {
                Glide.with(this).load(it)
                    .centerCrop()
                    .into(imageView)
            });
    }

    private fun intentSourceExample() {
        val intentSource = PublishSubject.create<Intent>()
        disposeBag.add(startForResult(intentSource) {
            it.data!!
        }.subscribe {
            Glide.with(this).load(it)
                .centerCrop()
                .into(imageView)
        })

        button.clicks().map { imagePickerIntent() }.subscribe(intentSource)

    }

    private fun multiIntentSource() {
        val imageIntentSource = PublishSubject.create<Intent>()
        val videoIntentSource = PublishSubject.create<Intent>()

        disposeBag.add(startForResult(imageIntentSource) {
            it.data!!
        }.subscribe {
            Glide.with(this).load(it)
                .centerCrop()
                .into(imageView)
        })

        disposeBag.add(startForResult(videoIntentSource) {
            it.data!!
        }.subscribe {
            videoView.setVideoURI(it)
            videoView.start()
        })

        button.clicks().map { imagePickerIntent() }.subscribe(imageIntentSource)
        videoButton.clicks().map { videoIntent() }.subscribe(videoIntentSource)
    }

    private fun imagePickerIntent() = Intent().apply {
        type = "image/*"
        action = Intent.ACTION_GET_CONTENT
    }

    private fun videoIntent() = Intent().apply {
        type = "video/*"
        action = Intent.ACTION_GET_CONTENT
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.dispose()
    }
}