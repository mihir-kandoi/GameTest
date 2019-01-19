package com.example.mihirkandoi.GameTest.SonycSound;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.GameTest.SeeTheSygns.Start;
import com.example.mihirkandoi.gametest.R;

import java.util.ArrayList;

public class Main extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener
{

    int result = 1;
    int sound;
    ArrayList<ToggleButton> toggleButtons;
    Intent intent;
    String roundNo;
    AlertDialog alertDialog;
    static SoundPool soundPool = new SoundPool.Builder().setMaxStreams(1).build();
    static int count = 1;
    int streamID;

    @Override
    public void onBackPressed() {
        result = 0;
        count--;
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

        // set navigation/status bar black
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().getDecorView().setSystemUiVisibility(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        findViewById(R.id.sound).setVisibility(View.INVISIBLE);
        intent = Parent.setRoundNo_and_generateNextIntent(this);
        roundNo = getIntent().getStringExtra("roundNo");
        sound = soundPool.load(this, getResources().getIdentifier("ss_q" + Integer.toString(count++), "raw", getPackageName()), 1);
        findViewById(R.id.sound).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                streamID = soundPool.play(sound, 1,1,1,0,1);
            }
        });
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                findViewById(R.id.sound).setVisibility(View.VISIBLE);
                findViewById(R.id.progressBar).setVisibility(View.GONE);
            }
        });
        int options[] = {R.id.option4, R.id.option1, R.id.option5, R.id.option3, R.id.option2};
        for(int i : options)
        {
            StateListDrawable stateListDrawable = new StateListDrawable();
            LayerDrawable layerDrawable = (LayerDrawable) getDrawable(R.drawable.image_selected);
            layerDrawable.mutate();
            layerDrawable.setDrawable(0, findViewById(i).getBackground());
            ((GradientDrawable) layerDrawable.getDrawable(1)).setStroke(Parent.convertToPixel(this, 4), getResources().getColor(R.color.sonycSound, getTheme()));
            ((GradientDrawable) layerDrawable.getDrawable(1)).setCornerRadius(Parent.convertToPixel(this, 10));
            stateListDrawable.addState(new int[] {android.R.attr.state_checked}, layerDrawable);
            stateListDrawable.addState(new int[] {-android.R.attr.state_checked}, findViewById(i).getBackground());
            findViewById(i).setBackground(stateListDrawable);
        }
        toggleButtons = Parent.setToggleButtons(this, 5);
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
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Parent.toggleButtons(toggleButtons, buttonView);
            if (!roundNo.equals("3/3"))
                startActivityForResult(intent, 1);
            else
            {
                alertDialog = Parent.moduleEnd(this, R.color.sonycSound, Main.class, Start.class);
                alertDialog.show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(roundNo.equals("3/3") && alertDialog != null)
            alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
