package com.example.mihirkandoi.GameTest.PieceOfMynd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.gametest.R;

public class Instructions2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        Parent.instructions(this, R.layout.activity_pom_sel_image, R.color.pieceOfMynd, "All the completed puzzles will be shown one after the other.", "Select the picture that best resonates with how you feel.", "Play Game", Main.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
