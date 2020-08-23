
# RxResult
This library allows the usage of RxJava with the new Activity Result APIs are made available on the [Activity 1.2.0-alpha02](https://developer.android.com/jetpack/androidx/releases/activity#1.2.0-alpha02), [Fragment 1.3.0-alpha02](https://developer.android.com/jetpack/androidx/releases/fragment#1.3.0-alpha02)


[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)



## Setup

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'me.sujanpoudel.rxresult:latest_version'
}

```

## Usage

>  These example are using Activity you can do also use it in Fragment.

##### Kotlin

```kotlin

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val intent = Intent().apply { //create intent
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
        }

       startForResult(subject) // Observable<Intent>
        .subscribe { intent ->
            Log.d("result is", "${intent}")
        }

```

##### Java

```Java

public class JavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java);

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        RxResult.startForResult(this, intent)
                .subscribe(intent->  Log.d("result is", intent.toString()) );
    }
}

```

You can also provide a data extractor function to transform intent to result

```kotlin
    val intent = Intent().apply { //create intent
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
    }

    startForResult(intent) {
        it.data!!
    }.subscribe { uri ->
        Glide.with(this).load(uri)
            .centerCrop()
            .into(imageView)
    }

```

Also you can provide a ```Observable<Intent>``` as intent source. It is best with [RxBinding](https://github.com/JakeWharton/RxBinding/tree/47ae0cec70480287b07d288722a71d7cbf245eb6)  by [@JakeWharton](https://github.com/JakeWharton)

```kotlin
    val imageIntentSource = PublishSubject.create<Intent>()

    val imageView = findViewById<ImageView>(R.id.iv)
    val button = findViewById<Button>(R.id.btn_image)

    startForResult(imageIntentSource) {
        it.data!!
    }.subscribe {
        Glide.with(this).load(it)
            .centerCrop()
            .into(imageView)
    }

   button.clicks().map { imagePickerIntent() }.subscribe(imageIntentSource)


```

You can have multiple intent source for different type of results

```kotlin
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

```


## Useful Libraries
 -  [RxPermissions](https://github.com/tbruyelle/RxPermissions) by [@tbruyelle ](https://github.com/tbruyelle)
 -  [RxBinding](https://github.com/JakeWharton/RxBinding) by [@JakeWharton ](https://github.com/JakeWharton)
 -  [RxJava](https://github.com/ReactiveX/RxJava) & [RxAndroid](https://github.com/ReactiveX/RxAndroid) from [@ReactiveX ](https://github.com/ReactiveX/RxJava)



## Bugs and Feedback

For bugs, questions and discussions please use the [Github Issues](https://github.com/psuzn/RxResult).

## License
```
Copyright 2020 @psuzn

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```