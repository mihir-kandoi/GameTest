package com.example.mihirkandoi.GameTest.PieceOfMynd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.gametest.R;

public class Instructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        Parent.instructions(this, R.layout.activity_pom, R.color.pieceOfMynd, "Drag the pieces above to complete the picture.", "3 puzzles will be shown one after the other.", "Next", Instructions2.class);
    }
}