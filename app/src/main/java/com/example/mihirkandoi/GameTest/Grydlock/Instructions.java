package com.example.mihirkandoi.GameTest.Grydlock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.gametest.R;

public class Instructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        Parent.instructions(this, R.layout.activity_grydlock, R.color.grydlock, "Find words that best describe feelings by swiping horizontally or vertically in the grid.", "", "Next", Instructions2.class);
    }
}
