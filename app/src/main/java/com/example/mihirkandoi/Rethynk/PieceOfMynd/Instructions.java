package com.example.mihirkandoi.Rethynk.PieceOfMynd;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.mihirkandoi.Rethynk.Parent;
import com.example.mihirkandoi.rethynk.R;

public class Instructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        Parent.instructions(this, R.layout.activity_pom, R.color.pieceOfMynd, "Drag the pieces above to alert_dialog_complete the picture.", "3 puzzles will be shown one after the other.", "Next", Instructions2.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}