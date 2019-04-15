package com.example.mihirkandoi.GameTest.SplashAndIntro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.mihirkandoi.GameTest.StoryLyne.Start;
import com.example.mihirkandoi.gametest.R;
import com.github.paolorotolo.appintro.AppIntro;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AppIntroSlider extends AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        startActivity(new Intent(this, Start.class));
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(this, Start.class));
        finish();
    }
}

