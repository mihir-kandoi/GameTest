package com.example.mihirkandoi.Rethynk.SplashAndIntro;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mihirkandoi.Rethynk.StoryLyne.Start;
import com.example.mihirkandoi.rethynk.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.concurrent.CountDownLatch;

import es.dmoral.toasty.Toasty;

public class SplashEntry extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_entry);
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        //check if app data was cleared or if the user still exists
        if (getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE).getBoolean("IS_DATA_CLEARED", true)) {
            intent = new Intent(this, AppIntroSlider.class);
            countDownLatch.countDown();
        }
        else {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseAuth.getInstance().getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            intent = new Intent(SplashEntry.this, Start.class);
                        else {
                            if(task.getException() instanceof FirebaseNetworkException) {
                                Toasty.error(SplashEntry.this, "Can't connect to the network, please check your internet connection", Toast.LENGTH_LONG, true).show();
                                finish();
                            }
                            else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                FirebaseAuth.getInstance().signOut();
                                AuthUI.getInstance().signOut(SplashEntry.this);
                                intent = new Intent(SplashEntry.this, AppIntroSlider.class);
                            }
                            else {
                                Log.e("Current user reload", "Unhandled error", task.getException());
                                Toasty.error(SplashEntry.this, "Unhandled Firebase error", Toast.LENGTH_LONG, true).show();
                                finish();
                            }
                        }
                        countDownLatch.countDown();
                    }
                });
            } else {
                intent = new Intent(this, AppIntroSlider.class);
                countDownLatch.countDown();
            }
        }

        // set "y" letter in app accent color
        TextView appName = findViewById(R.id.appName);
        SpannableString ss = new SpannableString(appName.getText());
        int index = appName.getText().toString().indexOf("y");
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#ad19d1")), index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        appName.setText(ss);

        new Thread() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                    sleep(750);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
