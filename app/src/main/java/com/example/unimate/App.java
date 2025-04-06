package com.example.unimate;

import android.app.Application;

import com.example.unimate.database.AppDatabase;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase.getInstance(this);
    }
}