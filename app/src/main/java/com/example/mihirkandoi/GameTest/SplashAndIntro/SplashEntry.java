package com.example.mihirkandoi.GameTest.SplashAndIntro;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.example.mihirkandoi.gametest.R;

public class SplashEntry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_entry);

        // set "y" letter in app accent color
        TextView appName = findViewById(R.id.appName);
        SpannableString ss = new SpannableString(appName.getText());
        int index = appName.getText().toString().indexOf("y");
        ss.setSpan(new ForegroundColorSpan(getColor(R.color.colorAccent)), index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        new Thread()
        {
            @Override
            public void run() {
                try {
                    sleep(900);
                    Intent intent = new Intent(getApplicationContext(), AppIntroSlider.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
