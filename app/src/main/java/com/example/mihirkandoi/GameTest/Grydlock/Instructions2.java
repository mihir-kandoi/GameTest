package com.example.mihirkandoi.GameTest.Grydlock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.gametest.R;

public class Instructions2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        Parent.instructions(this, R.layout.activity_start, R.color.grydlock, "All the feelings words possible in the grid are shown.", "Tap as many as you identify with. Try andn select atleast 2 words.", "Play Game", Main.class);
    }
}
