package com.example.mihirkandoi.GameTest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.mihirkandoi.GameTest.Grydlock.Main;
import com.example.mihirkandoi.GameTest.Twysted.Start;
import com.example.mihirkandoi.gametest.R;

import org.json.JSONException;

import java.util.ArrayList;

public class SelectWords extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    AlertDialog alertDialog;
    ArrayList<ToggleButton> allButtons = new ArrayList<>();
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
        setContentView(R.layout.activity_sel_words);

        // set status bar black
        getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        findViewById(R.id.submit).setEnabled(false);
        final int color = getIntent().getIntExtra("color", 0);
        findViewById(R.id.submit).setBackground(getDrawable(getIntent().getIntExtra("color", 0)));

        for(int i=1;i<=16;i++) {
            ToggleButton temp = findViewById(getResources().getIdentifier("toggleButton" + Integer.toString(i), "id", getPackageName()));
            temp.setOnCheckedChangeListener(this);
            temp.setVisibility(View.GONE);
            allButtons.add(temp);
        }

        ArrayList<String> allWords = getIntent().getStringArrayListExtra("all");
        ArrayList<String> found = getIntent().getStringArrayListExtra("found");
        int i,j;

        ArrayList<String> allWordsPC = new ArrayList<>();

        for(String temp : allWords)
        {
            temp = temp.toLowerCase();
            temp = temp.replaceFirst(Character.toString(temp.charAt(0)), Character.toString(Character.toUpperCase(temp.charAt(0))));
            allWordsPC.add(temp);
        }

        for(i=0;i<found.size();i++) {
            ToggleButton temp = allButtons.get(i);
            String word = found.get(i);
            word = word.toLowerCase();
            word = word.replaceFirst(Character.toString(word.charAt(0)), Character.toString(Character.toUpperCase(word.charAt(0))));
            temp.setText(word);
            temp.setTextOff(word);
            temp.setTextOn(word);
            temp.setVisibility(View.VISIBLE);
        }

        if(i%2!=0)
            allButtons.get(i).setVisibility(View.INVISIBLE);

        for(i=8,j=0;j<allWords.size();j++) {
            if(found.contains(allWords.get(j)))
                continue;
            String word = allWordsPC.get(j);
            allButtons.get(i).setText(word);
            allButtons.get(i).setTextOff(word);
            allButtons.get(i).setTextOn(word);
            allButtons.get(i).setVisibility(View.VISIBLE);
            i++;
        }

        if(i==8)
            findViewById(R.id.textView).setVisibility(View.GONE);

        else if(i%2!=0)
            allButtons.get(i).setVisibility(View.INVISIBLE);

        try {
            Parent.infoIcon(this, color, allWordsPC);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                alertDialog = Parent.moduleEnd(SelectWords.this, color, Main.class, Start.class);
                alertDialog.show();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            buttonView.setTextColor(Color.WHITE);
            findViewById(R.id.submit).setEnabled(true);
        } else {
            buttonView.setTextColor(Color.BLACK);
            int i;
            for(i=0;i<allButtons.size();i++)
                if(allButtons.get(i).isChecked())
                    break;
            if(i==allButtons.size())
                findViewById(R.id.submit).setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if(alertDialog != null)
            alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}