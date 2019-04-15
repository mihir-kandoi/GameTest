package com.example.mihirkandoi.GameTest;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
