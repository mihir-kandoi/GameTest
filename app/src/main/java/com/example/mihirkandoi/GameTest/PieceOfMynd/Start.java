package com.example.mihirkandoi.GameTest.PieceOfMynd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.gametest.R;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        Parent.start(this, "Piece Of Mynd", "Pointer", "Indicating states of reaction that are evoked in the presence of certain stimuli.", R.color.pieceOfMynd, Instructions.class);
    }
}
