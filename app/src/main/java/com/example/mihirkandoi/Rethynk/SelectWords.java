package com.example.mihirkandoi.Rethynk;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mihirkandoi.Rethynk.Grydlock.Main;
import com.example.mihirkandoi.Rethynk.Pyctures.Start;
import com.example.mihirkandoi.rethynk.R;

import org.json.JSONException;

import java.util.ArrayList;

public class SelectWords extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    AlertDialog alertDialog;
    int result = 1;
    String roundNo;
    Intent intent;

    ArrayList<ToggleButton> FoundButtons = new ArrayList<>();
    ArrayList<ToggleButton> OtherButtons = new ArrayList<>();

    @Override
    public void onBackPressed() {
        result = 0;
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1)
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

        getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        findViewById(R.id.submit).setEnabled(false);
        final int color = getIntent().getIntExtra("color", 0);
        findViewById(R.id.submit).setBackground(getDrawable(getIntent().getIntExtra("color", 0)));
        if (color == R.color.storyLyne)
            findViewById(R.id.roundNo).setVisibility(View.GONE);
        else {
            // set current activity's roundNo
            roundNo = getIntent().getStringExtra("roundNo");
            TextView tv = findViewById(R.id.roundNo);
            tv.setText(roundNo);

            intent = new Intent(this, com.example.mihirkandoi.Rethynk.Twysted.Main.class);
            char temp = roundNo.charAt(0);
            temp++;
            intent.putExtra("roundNo", roundNo.replace(roundNo.charAt(0), temp));
        }

        for (int i = 1; i <= 4; i++) {
            ToggleButton tempFound = findViewById(getResources().getIdentifier("toggleButtonFound" + i, "id", getPackageName()));
            ToggleButton tempOther = findViewById(getResources().getIdentifier("toggleButtonOther" + i, "id", getPackageName()));
            tempFound.setOnCheckedChangeListener(this);
            tempOther.setOnCheckedChangeListener(this);
            tempFound.setVisibility(View.GONE);
            tempOther.setVisibility(View.GONE);
            FoundButtons.add(tempFound);
            OtherButtons.add(tempOther);
        }

        ArrayList<String> allWords = getIntent().getStringArrayListExtra("all");
        ArrayList<String> found = getIntent().getStringArrayListExtra("found");
        int i, j;

        ArrayList<String> allWordsPC = new ArrayList<>(); // PC -> proper casing
        for (String temp : allWords) {
            temp = temp.toLowerCase();
            temp = temp.replaceFirst(Character.toString(temp.charAt(0)), Character.toString(Character.toUpperCase(temp.charAt(0))));
            allWordsPC.add(temp);
        }

        TableRow tableRow = null;
        for (i = 0; i < found.size(); i++) {
            if (i >= 4) {
                if (i % 2 == 0) {
                    tableRow = new TableRow(this);
                    ((TableLayout) findViewById(R.id.tableLayout2)).addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                }
                ToggleButton toggleButton = new ToggleButton(this);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, getResources().getDisplayMetrics());
                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
                int temp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());
                if (i % 2 == 0)
                    layoutParams.setMargins(temp, temp, temp, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                else
                    layoutParams.setMargins(0, temp, temp, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                toggleButton.setBackground(getResources().getDrawable(R.drawable.sel_words_emotion, getTheme()));
                toggleButton.setAllCaps(false);
                toggleButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                toggleButton.setOnCheckedChangeListener(this);
                toggleButton.setGravity(Gravity.CENTER);
                toggleButton.setPadding(0, 0, 0, 0);
                tableRow.addView(toggleButton, layoutParams);
                FoundButtons.add(toggleButton);
            }
            ToggleButton temp = FoundButtons.get(i);
            String word = found.get(i);
            word = word.toLowerCase();
            word = word.replaceFirst(Character.toString(word.charAt(0)), Character.toString(Character.toUpperCase(word.charAt(0))));
            temp.setText(word);
            temp.setTextOff(word);
            temp.setTextOn(word);
            temp.setVisibility(View.VISIBLE);
        }

        if (i < 4 && i % 2 != 0)
            FoundButtons.get(i).setVisibility(View.INVISIBLE);

        for (i = 0, j = 0; j < allWords.size(); j++) {
            if (found.contains(allWords.get(j)))
                continue;
            if (i >= 4) {
                if (i % 2 == 0) {
                    tableRow = new TableRow(this);
                    ((TableLayout) findViewById(R.id.tableLayout1)).addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                }
                ToggleButton toggleButton = new ToggleButton(this);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, getResources().getDisplayMetrics());
                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
                int temp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());
                if (i % 2 == 0)
                    layoutParams.setMargins(temp, temp, temp, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                else
                    layoutParams.setMargins(0, temp, temp, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                toggleButton.setBackground(getResources().getDrawable(R.drawable.sel_words_emotion, getTheme()));
                toggleButton.setAllCaps(false);
                toggleButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                toggleButton.setOnCheckedChangeListener(this);
                toggleButton.setGravity(Gravity.CENTER);
                toggleButton.setPadding(0, 0, 0, 0);
                tableRow.addView(toggleButton, layoutParams);
                OtherButtons.add(toggleButton);
            }
            String word = allWordsPC.get(j);
            OtherButtons.get(i).setText(word);
            OtherButtons.get(i).setTextOff(word);
            OtherButtons.get(i).setTextOn(word);
            OtherButtons.get(i).setVisibility(View.VISIBLE);
            i++;
        }

        if (i == 0)
            findViewById(R.id.textView).setVisibility(View.GONE);
        else if (i < 4 && i % 2 != 0)
            OtherButtons.get(i).setVisibility(View.INVISIBLE);

        try {
            Parent.infoIcon(this, color, allWordsPC);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roundNo != null && !roundNo.equals("3/3")) {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    startActivityForResult(intent, 1);
                } else {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    if (color == R.color.sonycSound)
                        alertDialog = Parent.moduleEnd(SelectWords.this, color, Main.class, com.example.mihirkandoi.Rethynk.SeeTheSygns.Start.class);
                    else
                        alertDialog = Parent.moduleEnd(SelectWords.this, color, Main.class, Start.class);
                    alertDialog.show();
                }
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
            int i, j;
            for (i = 0; i < FoundButtons.size(); i++)
                if (FoundButtons.get(i).isChecked())
                    break;
            for (j = 0; j < OtherButtons.size(); j++)
                if (OtherButtons.get(j).isChecked())
                    break;
            if (i == FoundButtons.size() && j == OtherButtons.size())
                findViewById(R.id.submit).setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if (alertDialog != null)
            alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}