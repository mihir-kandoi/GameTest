package com.example.mihirkandoi.GameTest.StoryLyne;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.GameTest.StryngOfThought.Start;
import com.example.mihirkandoi.gametest.R;

public class Main extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    int result = 1;
    Intent intent;
    String roundNo;
    ToggleButton[] toggleButtons;
    @Override
    public void onBackPressed() {
        result = 0;
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 1)
            finish();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        setResult(result);
        super.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sl);
        Parent.infoIcon(this, R.color.storyLyne); //Set info icon listener
        intent = Parent.roundNo(this);
        roundNo = getIntent().getStringExtra("roundNo");
        toggleButtons = new ToggleButton[5];
        for(int i = 1; i <= 5; i++)
        {
            toggleButtons[i - 1] = findViewById(getResources().getIdentifier("option" + Integer.toString(i), "id", getPackageName()));
            toggleButtons[i - 1].setOnCheckedChangeListener(this);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked)
        {
            Parent.toggleButtons(this, toggleButtons, 5, buttonView);
            if (!roundNo.equals("3/3"))
                startActivityForResult(intent, 1);
            else
                Parent.moduleEnd(Main.this, R.color.storyLyne, Main.class, Start.class).show();
        }
    }
}
