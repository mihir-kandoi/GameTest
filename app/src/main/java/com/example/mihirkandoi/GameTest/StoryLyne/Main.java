package com.example.mihirkandoi.GameTest.StoryLyne;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.GameTest.StryngOfThought.Start;
import com.example.mihirkandoi.gametest.R;

public class Main extends AppCompatActivity {

    int result = 1;

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
        final Intent intent = Parent.roundNo(this);
        final String roundNo = getIntent().getStringExtra("roundNo");
        final ToggleButton tb1 = findViewById(R.id.option1);
        final ToggleButton tb2 = findViewById(R.id.option2);
        final ToggleButton tb3 = findViewById(R.id.option3);
        final ToggleButton tb4 = findViewById(R.id.option4);
        final ToggleButton tb5 = findViewById(R.id.option5);
        tb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    tb2.setChecked(false);
                    tb3.setChecked(false);
                    tb4.setChecked(false);
                    tb5.setChecked(false);
                    if(!roundNo.equals("3/3"))
                        startActivityForResult(intent, 1);
                    else
                        Parent.moduleEnd(Main.this, R.color.storyLyne, Main.class, Start.class).show();
                }
            }
        });
        tb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    tb1.setChecked(false);
                    tb3.setChecked(false);
                    tb4.setChecked(false);
                    tb5.setChecked(false);
                    if(!roundNo.equals("3/3"))
                        startActivityForResult(intent, 1);
                    else
                        Parent.moduleEnd(Main.this, R.color.storyLyne, Main.class, Start.class).show();
                }
            }
        });
        tb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    tb1.setChecked(false);
                    tb2.setChecked(false);
                    tb4.setChecked(false);
                    tb5.setChecked(false);
                    if(!roundNo.equals("3/3"))
                        startActivityForResult(intent, 1);
                    else
                        Parent.moduleEnd(Main.this, R.color.storyLyne, Main.class, Start.class).show();
                }
            }
        });
        tb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    tb1.setChecked(false);
                    tb2.setChecked(false);
                    tb3.setChecked(false);
                    tb5.setChecked(false);
                    if(!roundNo.equals("3/3"))
                        startActivityForResult(intent, 1);
                    else
                        Parent.moduleEnd(Main.this, R.color.storyLyne, Main.class, Start.class).show();
                }
            }
        });
        tb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    tb1.setChecked(false);
                    tb2.setChecked(false);
                    tb3.setChecked(false);
                    tb4.setChecked(false);
                    if(!roundNo.equals("3/3"))
                        startActivityForResult(intent, 1);
                    else
                        Parent.moduleEnd(Main.this, R.color.storyLyne, Main.class, Start.class).show();
                }
            }
        });
    }
}
