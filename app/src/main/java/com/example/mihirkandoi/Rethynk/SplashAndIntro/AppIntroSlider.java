package com.example.mihirkandoi.Rethynk.SplashAndIntro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.mihirkandoi.Rethynk.LoginSignUp.Login;
import com.example.mihirkandoi.rethynk.R;
import com.github.paolorotolo.appintro.AppIntro;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AppIntroSlider extends AppIntro {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == 1)
            finish();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDoneText("Login");
        setSeparatorColor(Color.BLACK);
        setColorSkipButton(Color.BLACK);
        setIndicatorColor(Color.GRAY, Color.BLACK);
        setColorDoneText(Color.BLACK);
        setNextArrowColor(Color.BLACK);
        addSlide(SliderFragment.newInstance(R.layout.fragment_1st_slide));
        addSlide(SliderFragment.newInstance(R.layout.fragment_2nd_slide));
        addSlide(SliderFragment.newInstance(R.layout.fragment_3rd_slide));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivityForResult(new Intent(this, Login.class), 1);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivityForResult(new Intent(this, Login.class), 1);
    }
}

