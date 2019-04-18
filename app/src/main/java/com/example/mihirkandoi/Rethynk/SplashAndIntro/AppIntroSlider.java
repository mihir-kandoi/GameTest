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
        startActivity(new Intent(this, Login.class));
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(this, Login.class));
    }
}

