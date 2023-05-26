package com.work.webtest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Inet4Address;
import java.net.Socket;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    FirebaseRemoteConfig remoteConfig;
    ApplicationData data;
    boolean answer, isFirstTime;
    Thread question = new Thread(new Runnable() {
        @Override
        public void run() {
            remoteConfig = FirebaseRemoteConfig.getInstance();
            Task<Void> fetch = remoteConfig.fetch(0);
            fetch.addOnSuccessListener(unused -> {
                remoteConfig.activate();
            });
            boolean control = remoteConfig.getBoolean("control");
            if (!control) {
                finish();
                return;
            }
            if (isFirstTime) {
                answer = remoteConfig.getBoolean("server_answer");
                runOnUiThread(() -> Realm.getDefaultInstance().executeTransaction(realm -> {
                    data.answer = answer;
                    data.isFirstTime = false;
                }));
            }
            Intent intent;
            if (answer) {
                intent = new Intent(MainActivity.this, WebActivity.class);
            } else {
                intent = new Intent(MainActivity.this, GameActivity.class);
            }
            startActivity(intent);
            finish();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = Realm.getDefaultInstance().where(ApplicationData.class).findFirst();
        answer = data.answer;
        isFirstTime = data.isFirstTime;
        question.start();
    }
}