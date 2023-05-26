package com.work.webtest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;

public class PauseActivity extends AppCompatActivity {
    int todayCount;
    TextView highScoreTextView, todayClicksTextView, howMuchTimeTextView, lastClicksTextView;
    Button button;
    Realm realm;
    ApplicationData applicationData;
    Animation animation, animation1, animation2, animation3;
    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    View.OnClickListener listener = view -> {
        AlertDialog dialog = new AlertDialog.Builder(PauseActivity.this)
                .setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    realm.executeTransaction(realm -> {
                        applicationData.summaryClicks += todayCount;
                        applicationData.lastClicks = todayCount;
                        applicationData.highScore = Math.max(todayCount, applicationData.highScore);
                        applicationData.howMuchTime += Calendar.getInstance().getTimeInMillis() - applicationData.howMuchTime;
                    });
                    realm.close();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finishAndRemoveTask();

                })
                .setTitle(R.string.requestion)
                .create();
        dialog.show();
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause);
        realm = Realm.getDefaultInstance();
        applicationData = realm.where(ApplicationData.class).findFirst();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(applicationData.howMuchTime);
        todayCount = getIntent().getIntExtra("count", 0);
        button = findViewById(R.id.exitButton);
        highScoreTextView = findViewById(R.id.highScoreTextView);
        todayClicksTextView = findViewById(R.id.todayClicksTextView);
        howMuchTimeTextView = findViewById(R.id.howMuchTimeTextView);
        lastClicksTextView = findViewById(R.id.lastClicksTextView);

        animation = AnimationUtils.loadAnimation(PauseActivity.this, R.anim.moving_anim);
        animation1 = AnimationUtils.loadAnimation(PauseActivity.this, R.anim.moving_anim);
        animation2 = AnimationUtils.loadAnimation(PauseActivity.this, R.anim.moving_anim);
        animation3 = AnimationUtils.loadAnimation(PauseActivity.this, R.anim.moving_anim);

        animation.setStartOffset(0);
        animation1.setStartOffset(70);
        animation2.setStartOffset(140);
        animation3.setStartOffset(210);

        highScoreTextView.setText(getString(R.string.highScore, applicationData.highScore));
        todayClicksTextView.setText(getString(R.string.today, todayCount));
        howMuchTimeTextView.setText(getString(R.string.howMuchTime, format.format(calendar.getTime())));
        lastClicksTextView.setText(getString(R.string.yesterday, applicationData.lastClicks));

        lastClicksTextView.startAnimation(animation);
        todayClicksTextView.startAnimation(animation1);
        howMuchTimeTextView.startAnimation(animation2);
        highScoreTextView.startAnimation(animation3);

        button.setOnClickListener(listener);
    }
}