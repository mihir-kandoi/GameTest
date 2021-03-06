package com.example.mihirkandoi.Rethynk.StryngOfThought;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.mihirkandoi.Rethynk.Parent;
import com.example.mihirkandoi.rethynk.R;

public class Instructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        Parent.instructions(this, R.layout.activity_sot, R.color.stryngOfThought, "Complete the sentences with the emotion you think you’d experience the most.", "Drag your response to fill in the blanks. There are no right or wrong answers.", "Play Game", Main.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}