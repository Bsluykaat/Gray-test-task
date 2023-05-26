package com.work.webtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import io.realm.Realm;

public class WebActivity extends AppCompatActivity {
    Realm realm;
    ApplicationData applicationData;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        realm = Realm.getDefaultInstance();
        applicationData = realm.where(ApplicationData.class).findFirst();
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // Cookies теперь стандартная опция webView, нет нужды включать их вручную
        webView.loadUrl("https://html5test.com");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reload_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        realm.executeTransaction(realm -> {
            applicationData.isFirstTime = true;
            applicationData.answer = false;
        });
        Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
}