package com.example.mihirkandoi.GameTest.Emotymeter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.gametest.R;

public class Instructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        Parent.instructions(this, R.layout.activity_emotymeter, R.color.storyLyne, "As the Emojis fall...", "...select the emoticons that best describe how you feel currently.", "Play Game", Main.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
