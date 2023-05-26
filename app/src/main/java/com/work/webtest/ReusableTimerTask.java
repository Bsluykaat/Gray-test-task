package com.work.webtest;

import android.content.res.Resources;
import android.media.MediaPlayer;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import java.util.TimerTask;

public class ReusableTimerTask extends TimerTask {
    MediaPlayer player;
    ConstraintLayout mainBackground;
    Resources resources;

    public ReusableTimerTask(MediaPlayer player, ConstraintLayout mainBackground, Resources resources) {
        this.player = player;
        this.mainBackground = mainBackground;
        this.resources = resources;
    }

    @Override
    public void run() {
        if (player.isPlaying()) {
            player.pause();
        }
        mainBackground.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.grey, null));
    }
}
