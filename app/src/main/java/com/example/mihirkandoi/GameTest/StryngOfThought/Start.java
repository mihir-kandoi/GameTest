package com.example.mihirkandoi.GameTest.StryngOfThought;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.gametest.R;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        Parent.start(this, "Stryng Of Thought", "KNOWLEDGE", "Appreciation of the range of thoughts that silently exist.", R.color.stryngOfThought, Instructions.class);
    }
}
