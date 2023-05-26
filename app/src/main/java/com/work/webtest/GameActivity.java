package com.work.webtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;

public class GameActivity extends AppCompatActivity {
    long count;
    int todayCount = 0;
    Realm realm;
    ApplicationData applicationData;
    ImageButton button;
    ConstraintLayout mainBackground;
    TextView textView;
    MediaPlayer player;
    Random random = new Random(Calendar.getInstance().getTimeInMillis());
    int colors[] = new int[10];
    Timer timer;
    Animation first, second;
    AnimationSet set = new AnimationSet(true);

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            count++;
            todayCount++;
            textView.setText(String.valueOf(count));
            if (!player.isPlaying()) {
                player.start();
            }
            mainBackground.setBackgroundColor(colors[random.nextInt(10)]);
            textView.setTextColor(colors[random.nextInt(10)]);
            if (timer != null) {
                timer.cancel();
                timer.purge();
                timer = null;
            }
            timer = new Timer();
            timer.schedule(new ReusableTimerTask(player, mainBackground, getResources()), 500);
            button.startAnimation(set);
            textView.startAnimation(set);
        }
    };

    MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            switch (random.nextInt(3)) {
                case 0:
                    player = MediaPlayer.create(GameActivity.this, R.raw.bonfire_anime);
                    break;
                case 1:
                    player = MediaPlayer.create(GameActivity.this, R.raw.music_from_anime);
                    break;
                case 2:
                    player = MediaPlayer.create(GameActivity.this, R.raw.nyan_cat);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        realm = Realm.getDefaultInstance();
        applicationData = realm.where(ApplicationData.class).findFirst();

        first = AnimationUtils.loadAnimation(GameActivity.this, R.anim.increase_anim);
        second = AnimationUtils.loadAnimation(GameActivity.this, R.anim.decrease_anim);
        second.setStartOffset(50);
        set.addAnimation(first);
        set.addAnimation(second);

        button = findViewById(R.id.mainButton);
        mainBackground = findViewById(R.id.mainBackground);
        textView = findViewById(R.id.mainCounter);

        textView.setText(String.valueOf(applicationData.summaryClicks));

        colors[0] = ResourcesCompat.getColor(getResources(), R.color.red, null);
        colors[1] = ResourcesCompat.getColor(getResources(), R.color.green, null);
        colors[2] = ResourcesCompat.getColor(getResources(), R.color.blue, null);
        colors[3] = ResourcesCompat.getColor(getResources(), R.color.yellow, null);
        colors[4] = ResourcesCompat.getColor(getResources(), R.color.orange, null);
        colors[5] = ResourcesCompat.getColor(getResources(), R.color.purple, null);
        colors[6] = ResourcesCompat.getColor(getResources(), R.color.MediumSpringGreen, null);
        colors[7] = ResourcesCompat.getColor(getResources(), R.color.Aqua, null);
        colors[8] = ResourcesCompat.getColor(getResources(), R.color.pink, null);
        colors[9] = ResourcesCompat.getColor(getResources(), R.color.AntiqueWhite, null);

        switch (random.nextInt(3)) {
            case 0:
                player = MediaPlayer.create(GameActivity.this, R.raw.bonfire_anime);
                break;
            case 1:
                player = MediaPlayer.create(GameActivity.this, R.raw.music_from_anime);
                break;
            case 2:
                player = MediaPlayer.create(GameActivity.this, R.raw.nyan_cat);
                break;
        }

        button.setOnClickListener(listener);
        mainBackground.setOnClickListener(listener);
        player.setOnCompletionListener(onCompletionListener);
        count = applicationData.summaryClicks;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reload_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reload:
                realm.executeTransaction(realm -> {
                    applicationData.isFirstTime = true;
                    applicationData.answer = false;
                });
                Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show();
                break;
            case R.id.pause:
                Intent intent = new Intent(GameActivity.this, PauseActivity.class);
                intent.putExtra("count", todayCount);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        realm.executeTransaction(realm -> {
            applicationData.summaryClicks = count;
            applicationData.lastClicks = todayCount;
            applicationData.highScore = Math.max(todayCount, applicationData.highScore);
            applicationData.howMuchTime += Calendar.getInstance().getTimeInMillis() - applicationData.howMuchTime;
        });
        realm.close();
        super.onDestroy();
    }
}