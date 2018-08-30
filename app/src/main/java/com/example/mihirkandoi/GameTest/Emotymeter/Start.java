package com.example.mihirkandoi.GameTest.Emotymeter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.gametest.R;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Parent.start(this, "Emotymeter", "INDICATOR", "Determining the emotional states that exist presently.", R.color.storyLyne, Instructions.class);
    }
}
