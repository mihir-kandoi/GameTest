package com.example.mihirkandoi.GameTest.SeeTheSygns;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.gametest.R;

public class Instructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        Parent.instructions(this, R.layout.activity_sts, R.color.seeTheSygns, "Observe the symbol...", "…and choose the closest emotion you experience after viewing it.", "Play Game", Main.class);
    }
}