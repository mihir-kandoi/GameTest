package com.example.mihirkandoi.GameTest.Twysted;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mihirkandoi.GameTest.Parent;
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
    final int[] x = new int[1];
    final int[] y = new int[1];
    ArrayList<String> words = new ArrayList<>(Arrays.asList("Fear", "Mad", "Gay", "Sad", "Dreams", "Frayed", "Rage"));

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twysted);
        findViewById(R.id.submit).setEnabled(false);
        tableLayout = findViewById(R.id.tableLayout);

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
                if(tableRow.getChildCount() == 0) {
                    tableLayout.removeView(tableRow);
                    tableRow = (TableRow) tableLayout.getChildAt(tableLayout.getChildCount() -1);
                }
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
    }

    void setTableRow(TableRow tableRow)
    {
        tableRow.getLayoutParams().width = TableRow.LayoutParams.MATCH_PARENT;
        tableRow.getLayoutParams().height = TableRow.LayoutParams.WRAP_CONTENT;
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        tableRow.requestLayout();
    }
}
