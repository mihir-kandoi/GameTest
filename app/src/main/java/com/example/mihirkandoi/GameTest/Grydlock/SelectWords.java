package com.example.mihirkandoi.GameTest.Grydlock;

import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.GameTest.Twysted.Start;
import com.example.mihirkandoi.gametest.R;

import java.util.ArrayList;

public class SelectWords extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_words);
        Parent.infoIcon(this, R.color.twysted);
        ArrayList<ToggleButton> allButtons = new ArrayList<>();
        for(int i=1;i<=16;i++) {
            ToggleButton temp = findViewById(getResources().getIdentifier("toggleButton" + Integer.toString(i), "id", getPackageName()));
            temp.setOnCheckedChangeListener(this);
            temp.setVisibility(View.GONE);
            allButtons.add(temp);
        }
        ArrayList<String> allWords = getIntent().getStringArrayListExtra("all");
        ArrayList<String> found = getIntent().getStringArrayListExtra("found");
        int i,j;
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
        for(i=8,j=0;j<8;j++) {
            if(found.contains(allWords.get(j)))
                continue;
            String word = allWords.get(j);
            word = word.toLowerCase();
            word = word.replaceFirst(Character.toString(word.charAt(0)), Character.toString(Character.toUpperCase(word.charAt(0))));
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

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                alertDialog = Parent.moduleEnd(SelectWords.this, R.color.twysted, Main.class, Start.class);
                alertDialog.show();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked)
            buttonView.setTextColor(Color.WHITE);
        else
            buttonView.setTextColor(Color.BLACK);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(alertDialog != null)
            alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}