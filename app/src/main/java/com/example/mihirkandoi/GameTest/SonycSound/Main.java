package com.example.mihirkandoi.GameTest.SonycSound;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.gametest.R;

public class Main extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    int result = 1;
    ToggleButton[] toggleButtons = new ToggleButton[5];
    Intent intent;
    String roundNo;
    SoundPool soundPool = new SoundPool.Builder().setMaxStreams(1).build();
    int streamID;

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
        setContentView(R.layout.activity_ss);
        intent = Parent.roundNo(this);
        roundNo = getIntent().getStringExtra("roundNo");
        int sounds[] = {R.raw.bell, R.raw.explosion, R.raw.alarm};
        final int sound;
        if(roundNo.equals("1/3"))
            sound = soundPool.load(getApplicationContext(),sounds[0], 1);
        else if(roundNo.equals("2/3"))
            sound = soundPool.load(getApplicationContext(),sounds[1], 1);
        else
            sound = soundPool.load(getApplicationContext(),sounds[2], 1);
        findViewById(R.id.sound).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                streamID = soundPool.play(sound, 1,1,1,0,1);
            }
        });
        int options[] = {R.id.option4, R.id.option1, R.id.option5, R.id.option3, R.id.option2};
        for(int i : options)
        {
            StateListDrawable stateListDrawable = new StateListDrawable();
            LayerDrawable layerDrawable = (LayerDrawable) getDrawable(R.drawable.image_selected);
            layerDrawable.mutate();
            layerDrawable.setDrawable(0, findViewById(i).getBackground());
            ((GradientDrawable) layerDrawable.getDrawable(1)).setStroke(Parent.convertToPixel(this, 4), getResources().getColor(R.color.pyctures, getTheme()));
            ((GradientDrawable) layerDrawable.getDrawable(1)).setCornerRadius(Parent.convertToPixel(this, 10));
            stateListDrawable.addState(new int[] {android.R.attr.state_checked}, layerDrawable);
            stateListDrawable.addState(new int[] {-android.R.attr.state_checked}, findViewById(i).getBackground());
            findViewById(i).setBackground(stateListDrawable);
        }
        for(int i = 1; i <= 5; i++)
        {
            toggleButtons[i - 1] = findViewById(getResources().getIdentifier("option" + Integer.toString(i), "id", getPackageName()));
            toggleButtons[i - 1].setOnCheckedChangeListener(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        soundPool.stop(streamID);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked)
        {
            Parent.toggleButtons(this, toggleButtons, 5, buttonView);
            if (!roundNo.equals("3/3"))
                startActivityForResult(intent, 1);
            else
                Parent.moduleEnd(this, R.color.sonycSound, Main.class, Start.class).show();
        }
    }
}
