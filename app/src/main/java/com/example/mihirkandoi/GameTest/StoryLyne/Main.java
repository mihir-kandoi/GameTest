package com.example.mihirkandoi.GameTest.StoryLyne;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.GameTest.StryngOfThought.Start;
import com.example.mihirkandoi.gametest.R;

import java.util.ArrayList;

public class Main extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    int result = 1;
    Intent intent;
    String roundNo;
    ArrayList<ToggleButton> toggleButtons;

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
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(Color.parseColor("#a8e124"));
        intent = Parent.roundNo(this);
        roundNo = getIntent().getStringExtra("roundNo");
        toggleButtons = Parent.setToggleButtons(this, 5);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked)
        {
            Parent.toggleButtons(toggleButtons, buttonView);
            if (!roundNo.equals("3/3"))
                startActivityForResult(intent, 1);
            else
                Parent.moduleEnd(Main.this, R.color.storyLyne, Main.class, Start.class).show();
        }
    }
}
