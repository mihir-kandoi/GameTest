package com.example.mihirkandoi.GameTest.SeeTheSygns;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.gametest.R;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Parent.start(this, "See The Sygns", "ANALYSIS", "An examination of the interrelationship between representations and their significance to an individual.", R.color.seeTheSygns, Instructions.class);
    }
}