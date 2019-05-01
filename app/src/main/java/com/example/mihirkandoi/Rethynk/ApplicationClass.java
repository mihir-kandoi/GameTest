package com.example.mihirkandoi.Rethynk;

import android.app.Application;

import com.facebook.appevents.AppEventsLogger;
import androidx.appcompat.app.AppCompatDelegate;

public class ApplicationClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        AppEventsLogger.activateApp(this);
    }
}
