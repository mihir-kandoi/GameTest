package com.example.mihirkandoi.GameTest.SonycSound;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.gametest.R;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Parent.start(this, "Sonyc Sound", "CONNOTATION", "Emotional association called to mind by sound.", R.color.sonycSound, Instructions.class);
    }
}
