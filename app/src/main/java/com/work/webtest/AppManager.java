package com.work.webtest;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AppManager extends Application {
    @Override
    public void onCreate() {
        Realm.init(this);
        //Realm.deleteRealm(Realm.getDefaultConfiguration());
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder().allowWritesOnUiThread(true).build());
        if (Realm.getDefaultInstance().where(ApplicationData.class).findFirst() == null) {
            Realm.getDefaultInstance().executeTransaction(realm -> Realm.getDefaultInstance().createObject(ApplicationData.class));
        }
        super.onCreate();
    }
}
