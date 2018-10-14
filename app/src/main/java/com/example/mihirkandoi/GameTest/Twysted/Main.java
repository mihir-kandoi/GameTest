package com.example.mihirkandoi.GameTest.Twysted;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.GameTest.SelectWords;
import com.example.mihirkandoi.gametest.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class Main extends AppCompatActivity implements View.OnClickListener{

    TableLayout tableLayout;
    TableRow tableRow;
    ArrayList<TextView> arrayList = new ArrayList<>();
    ArrayList<String> words = new ArrayList<>(Arrays.asList("Fear", "Mad", "Gay", "Sad", "Dreams", "Frayed", "Rage"));
    ArrayList<String> wordsFound = new ArrayList<>();
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < word.length(); i++) {
                tableRow.removeView(arrayList.get(arrayList.size() - 1));
                arrayList.remove(arrayList.size() - 1);
                if (tableRow.getChildCount() == 0) {
                    tableLayout.removeViewAt(tableLayout.getChildCount() - 1);
                    tableRow = (TableRow) tableLayout.getChildAt(tableLayout.getChildCount() - 1);
                }
            }
            word="";
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    };
    Intent intent;
    final int[] x = new int[1];
    final int[] y = new int[1];
    int count=0;
    int ogCount = words.size();
    String word="";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twysted);
        findViewById(R.id.submit).setEnabled(false);
        tableLayout = findViewById(R.id.tableLayout);
        intent = new Intent(Main.this, SelectWords.class);
        intent.putStringArrayListExtra("all", words);
        intent.putExtra("color", R.color.twysted);

        StringBuilder sb = new StringBuilder();
        for(String string : words)
            sb.append(string);

        char[] chars = sb.toString().toLowerCase().toCharArray();
        Set<Character> present = new HashSet<>();
        int len = 0;
        for (char c : chars)
            if (present.add(c))
                chars[len++] = c;
        sb = new StringBuilder(new String(chars,0, len));

        Random random = new Random();
        for(int i=1;i<=9;i++) {
            Button button = findViewById(getResources().getIdentifier("button" + Integer.toString(i), "id", getPackageName()));
            button.setOnClickListener(this);
            int index = random.nextInt(sb.length());
            button.setText(Character.toString(sb.charAt(index)));
            sb.deleteCharAt(index);
        }

        tableLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tableLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                x[0] = tableLayout.getWidth() / (findViewById(R.id.button1).getWidth() + Parent.convertToPixel(Main.this, 8));
                y[0] = tableLayout.getHeight() / (findViewById(R.id.button1).getHeight() + Parent.convertToPixel(Main.this, 8));
            }
        });

        findViewById(R.id.backspace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrayList.isEmpty())
                    return;
                tableRow.removeView(arrayList.get(arrayList.size() - 1));
                arrayList.remove(arrayList.size() - 1);
                word = new StringBuilder(word).deleteCharAt(word.length() - 1).toString();
                if(tableRow.getChildCount() == 0) {
                    tableLayout.removeView(tableRow);
                    tableRow = (TableRow) tableLayout.getChildAt(tableLayout.getChildCount() -1);
                }
            }
        });

        findViewById(R.id.backspace).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(arrayList.isEmpty())
                    return true;
                int size = arrayList.size();
                for(int i=0;i<size;i++) {
                    tableRow.removeView(arrayList.get(arrayList.size() - 1));
                    arrayList.remove(arrayList.size() - 1);
                    if (tableRow.getChildCount() == 0) {
                        tableLayout.removeView(tableRow);
                        tableRow = (TableRow) tableLayout.getChildAt(tableLayout.getChildCount() - 1);
                    }
                }
                word = "";
                return true;
            }
        });

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                intent.putStringArrayListExtra("found", wordsFound);
                intent.putExtra("color", R.color.twysted);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(tableRow == null) {
            tableLayout.addView((tableRow = new TableRow(getApplicationContext())));
            setTableRow(tableRow);
        }
        else if(tableLayout.getChildCount() == y[0] && tableRow.getChildCount() == x[0]) {
            Toast.makeText(getApplicationContext(), "Out of space!", Toast.LENGTH_LONG).show();
            return;
        }
        else if(tableRow.getChildCount() == x[0]) {
            tableLayout.addView((tableRow = new TableRow(getApplicationContext())));
            setTableRow(tableRow);
        }
        TextView textView = new TextView(getApplicationContext());
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(v.getWidth(), v.getHeight());
        if(tableRow.getChildCount() != (x[0] - 1))
            layoutParams.rightMargin = Parent.convertToPixel(this, 8);
        if(tableLayout.getChildCount() != y[0])
            layoutParams.bottomMargin = Parent.convertToPixel(this, 8);
        textView.setLayoutParams(layoutParams);
        TextView og = (TextView) v;
        textView.setText(og.getText());
        textView.setTextSize(og.getTextSize() / getResources().getDisplayMetrics().scaledDensity);
        textView.setBackground(og.getBackground());
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setAllCaps(true);
        tableRow.addView(textView);
        arrayList.add(textView);
        word += textView.getText();
        String string="";
        for(String str : words)
        {
            if (str.equalsIgnoreCase(word))
            {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                handler.postDelayed(runnable, 750);
                wordsFound.add(str);
                findViewById(R.id.submit).setEnabled(true);
                string = str;
                ((TextView) findViewById(R.id.wordCounter)).setText(Integer.toString(++count)+"\nwords");
                if(count == ogCount) {
                    Toast.makeText(getApplicationContext(), "All words found!", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.submit).performClick();
                }
                else
                    Toast.makeText(getApplicationContext(), "Word found!", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        if(!string.isEmpty())
            words.remove(string);
    }

    void setTableRow(TableRow tableRow)
    {
        tableRow.getLayoutParams().width = TableRow.LayoutParams.MATCH_PARENT;
        tableRow.getLayoutParams().height = TableRow.LayoutParams.WRAP_CONTENT;
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        tableRow.requestLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
