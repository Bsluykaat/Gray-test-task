package com.work.webtest;

import io.realm.RealmObject;

public class ApplicationData extends RealmObject {
    public boolean answer;
    public boolean isFirstTime = true;
    public int highScore;
    public int lastClicks;
    public long summaryClicks;
    public long howMuchTime;
}
