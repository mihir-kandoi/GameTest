package com.example.mihirkandoi.GameTest.SonycSound;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.gametest.R;

public class Instructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        Parent.instructions(this, R.layout.activity_ss, R.color.sonycSound, "Tap the icon to listen to the sound. Choose the emoticon which best resonates with how you feel when you hear the sound.", "There are no wrong answers.", "Play game", Main.class);
    }
}