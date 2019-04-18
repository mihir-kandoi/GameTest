package com.example.mihirkandoi.Rethynk.SplashAndIntro;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.example.mihirkandoi.Rethynk.StoryLyne.Start;
import com.example.mihirkandoi.rethynk.R;
import com.firebase.ui.auth.AuthUI;

public class SplashEntry extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_entry);

        //check if app data was cleared
        if (getPreferences(Context.MODE_PRIVATE).getBoolean("IS_DATA_CLEARED", true)) {
            getPreferences(Context.MODE_PRIVATE).edit().putBoolean("IS_DATA_CLEARED", false).apply();
            AuthUI.getInstance().signOut(this);
            intent = new Intent(this, AppIntroSlider.class);
        }
        else
            intent = new Intent(this, Start.class);

        // set "y" letter in app accent color
        TextView appName = findViewById(R.id.appName);
        SpannableString ss = new SpannableString(appName.getText());
        int index = appName.getText().toString().indexOf("y");
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#ad19d1")), index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        appName.setText(ss);

        new Thread()
        {
            @Override
            public void run() {
                try {
                    sleep(900);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
