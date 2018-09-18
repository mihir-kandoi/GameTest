package com.example.mihirkandoi.GameTest.Grydlock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mihirkandoi.gametest.R;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grydlock);
        findViewById(R.id.submit).setEnabled(false);
    }
}
