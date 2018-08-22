package com.example.mihirkandoi.GameTest.Pyctures;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.gametest.R;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        Parent.start(this, "Pyctures", "REVELATION", "Evoking emotion through dramatic means.", R.color.pyctures, Instructions.class);
    }
}
