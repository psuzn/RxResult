package me.sujanpoudel.rxResult.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import kotlin.jvm.functions.Function1;
import me.sujanpoudel.rxresult.RxResult;

public class JavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java);

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        RxResult.startForResult(this, intent, intent1 -> intent1.getData())
                .subscribe();
    }
}