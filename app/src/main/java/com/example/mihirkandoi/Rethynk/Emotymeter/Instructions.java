package com.example.mihirkandoi.Rethynk.Emotymeter;

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
        Parent.instructions(this, R.layout.activity_emotymeter, R.color.storyLyne, "As the Emojis fall...", "...select the emoticons that best describe how you feel currently.", "Play Game", Main.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
