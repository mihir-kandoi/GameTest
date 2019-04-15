package com.example.mihirkandoi.GameTest.SonycSound;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.SoundPool;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.GameTest.SeeTheSygns.Start;
import com.example.mihirkandoi.gametest.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

public class Main extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener
{

    static int count = 1;
    int result = 1;
    static JSONArray jsonArray = null;
    Intent intent;
    String roundNo;
    AlertDialog alertDialog;

    ArrayList<ToggleButton> toggleButtons = new ArrayList<>(4);
    static SoundPool soundPool = new SoundPool.Builder().setMaxStreams(1).build();
    int streamID, sound;

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

        intent = Parent.setRoundNo_and_generateNextIntent(this);
        roundNo = getIntent().getStringExtra("roundNo");

        getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        findViewById(R.id.sound).setVisibility(View.INVISIBLE);
        sound = soundPool.load(this, getResources().getIdentifier("ss_q" + Integer.toString(count), "raw", getPackageName()), 1);

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                findViewById(R.id.sound).setVisibility(View.VISIBLE);
                findViewById(R.id.progressBar).setVisibility(View.GONE);
            }
        });

        findViewById(R.id.sound).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                streamID = soundPool.play(sound, 1,1,1,0,1);
            }
        });

        toggleButtons.addAll(Arrays.asList(new ToggleButton[]{findViewById(R.id.option1), findViewById(R.id.option2), findViewById(R.id.option3), findViewById(R.id.option4)}));

        if (jsonArray == null)
            try {
                jsonArray = new JSONArray(getIntent().getStringExtra("JSONarray"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        for(int i = 0; i < 4; i++)
        {
            Drawable emoji = null;
            try {
                emoji = getDrawable(getResources().getIdentifier("emoji_" + jsonArray.getJSONObject(count - 1).getString("option" + Integer.toString(i + 1)), "drawable", getPackageName()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            toggleButtons.get(i).setBackground(emoji);
            toggleButtons.get(i).setOnCheckedChangeListener(this);
        }
        count++;
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
            buttonView.setAlpha(1f);
            for (ToggleButton toggleButton : toggleButtons)
                if(buttonView.getId() != toggleButton.getId()) {
                    toggleButton.setChecked(false);
                    toggleButton.setAlpha(0.4f);
                }

            if (!roundNo.equals("3/3")) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                startActivityForResult(intent, 1);
            }
            else {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                alertDialog = Parent.moduleEnd(this, R.color.sonycSound, Main.class, Start.class);
                alertDialog.show();
            }
        }
        else
            buttonView.setAlpha(0.4f);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(roundNo.equals("3/3") && alertDialog != null)
            alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
