package com.example.mihirkandoi.GameTest.Twysted;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
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

    TableLayout answers, questions;
    TableRow tableRowA, tableRowQ;
    ArrayList<TextView> arrayList = new ArrayList<>();
    ArrayList<String> words = new ArrayList<>(Arrays.asList("Fear", "Mad", "Gay", "Sad", "Dreams", "Frayed", "Rage"));
    ArrayList<String> wordsClone = new ArrayList<>();
    ArrayList<String> wordsFound = new ArrayList<>();
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < word.length(); i++) {
                tableRowA.removeView(arrayList.get(arrayList.size() - 1));
                arrayList.remove(arrayList.size() - 1);
                if (tableRowA.getChildCount() == 0) {
                    answers.removeViewAt(answers.getChildCount() - 1);
                    tableRowA = (TableRow) answers.getChildAt(answers.getChildCount() - 1);
                }
            }
            word="";
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    };
    Intent intent;
    int x1, y1;
    int count=0;
    int ogCount = words.size();
    int result = 1;
    String word="";

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
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twysted_actual);

        wordsClone.addAll(words);

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        ConstraintLayout cs = new ConstraintLayout(getBaseContext());
        getLayoutInflater().inflate(R.layout.activity_twysted, cs, true);
        cs.measure(View.MeasureSpec. makeMeasureSpec(size.x, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(size.y, View.MeasureSpec.EXACTLY));
        cs.layout(0, 0, cs.getMeasuredWidth(), cs.getMeasuredHeight());

        findViewById(R.id.submit).setEnabled(false);
        final Button refButton = cs.findViewById(R.id.button1);
        ImageButton backspace = findViewById(R.id.backspace);
        answers = findViewById(R.id.answers);
        questions = findViewById(R.id.questions);
        intent = new Intent(Main.this, SelectWords.class);
        intent.putStringArrayListExtra("all", words);
        intent.putExtra("color", R.color.sonycSound);

        answers.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                answers.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                y1 = answers.getHeight() / (refButton.getHeight() + Parent.convertToPixel(Main.this, 8));
            }
        });
        x1 = cs.findViewById(R.id.tableLayout).getWidth() / (refButton.getWidth() + Parent.convertToPixel(Main.this, 8));

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
        int temp = sb.length();
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(refButton.getWidth(), refButton.getHeight());
        layoutParams.rightMargin = Parent.convertToPixel(this, 8);
        layoutParams.bottomMargin = Parent.convertToPixel(this, 8);
        for(int i=0;i<temp;i++) {
            if(tableRowQ == null || tableRowQ.getChildCount() == x1) {
                questions.addView((tableRowQ = new TableRow(getApplicationContext())));
                setTableRow(tableRowQ);
            }
            TextView textView = new TextView(getApplicationContext());
            textView.setLayoutParams(layoutParams);
            int index = random.nextInt(sb.length());
            textView.setText(Character.toString(sb.charAt(index)));
            textView.setTextSize(refButton.getTextSize() / getResources().getDisplayMetrics().scaledDensity);
            textView.setBackground(refButton.getBackground());
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.BLACK);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setAllCaps(true);
            tableRowQ.addView(textView);
            textView.setOnClickListener(this);
            sb.deleteCharAt(index);
        }
        ((ConstraintLayout) findViewById(R.id.mainCS)).removeView(backspace);
        backspace.setLayoutParams(layoutParams);
        if(tableRowQ.getChildCount() <= x1)
        {
            if(tableRowQ.getChildCount() == x1)
            {
                questions.addView((tableRowQ = new TableRow(getApplicationContext())));
                setTableRow(tableRowQ);
            }
            layoutParams.rightMargin = Parent.convertToPixel(this, 8);
            temp = (x1 - tableRowQ.getChildCount()) - 1;
            for(int i=0;i<temp;i++) {
                TextView textView = new TextView(getApplicationContext());
                textView.setLayoutParams(layoutParams);
                textView.setVisibility(View.INVISIBLE);
                textView.setEnabled(false);
                textView.setClickable(false);
                tableRowQ.addView(textView);
            }
        }
        tableRowQ.addView(backspace);
        answers.getLayoutParams().height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
        answers.requestLayout();

        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrayList.isEmpty())
                    return;
                tableRowA.removeView(arrayList.get(arrayList.size() - 1));
                arrayList.remove(arrayList.size() - 1);
                word = new StringBuilder(word).deleteCharAt(word.length() - 1).toString();
                if(tableRowA.getChildCount() == 0) {
                    answers.removeView(tableRowA);
                    tableRowA = (TableRow) answers.getChildAt(answers.getChildCount() -1);
                }
            }
        });

        backspace.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(arrayList.isEmpty())
                    return true;
                int size = arrayList.size();
                for(int i=0;i<size;i++) {
                    tableRowA.removeView(arrayList.get(arrayList.size() - 1));
                    arrayList.remove(arrayList.size() - 1);
                    if (tableRowA.getChildCount() == 0) {
                        answers.removeView(tableRowA);
                        tableRowA = (TableRow) answers.getChildAt(answers.getChildCount() - 1);
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
                intent.putExtra("color", R.color.sonycSound);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(tableRowA == null) {
            answers.addView((tableRowA = new TableRow(getApplicationContext())));
            setTableRow(tableRowA);
        }
        else if(answers.getChildCount() == y1 && tableRowA.getChildCount() == x1) {
            Toast.makeText(getApplicationContext(), "Out of space!", Toast.LENGTH_LONG).show();
            return;
        }
        else if(tableRowA.getChildCount() == x1) {
            answers.addView((tableRowA = new TableRow(getApplicationContext())));
            setTableRow(tableRowA);
        }
        TextView textView = new TextView(getApplicationContext());
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(v.getWidth(), v.getHeight());
        if(tableRowA.getChildCount() != (x1 - 1))
            layoutParams.rightMargin = Parent.convertToPixel(this, 8);
        if(answers.getChildCount() != y1)
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
        tableRowA.addView(textView);
        arrayList.add(textView);
        word += textView.getText();
        String string="";
        for(String str : wordsClone)
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
            wordsClone.remove(string);
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
