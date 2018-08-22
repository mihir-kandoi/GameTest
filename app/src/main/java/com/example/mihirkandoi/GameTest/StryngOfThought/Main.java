package com.example.mihirkandoi.GameTest.StryngOfThought;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.GameTest.PieceOfMynd.Start;
import com.example.mihirkandoi.gametest.R;

public class Main extends AppCompatActivity implements View.OnTouchListener{

    String text;
    TextView old;

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
        setContentView(R.layout.activity_sot);
        Parent.infoIcon(this, R.color.stryngOfThought); //Set info icon listener
        final Intent intent = Parent.roundNo(this);
        final String roundNo = getIntent().getStringExtra("roundNo");
        findViewById(R.id.option1).setOnTouchListener(this);
        findViewById(R.id.option2).setOnTouchListener(this);
        findViewById(R.id.option3).setOnTouchListener(this);
        findViewById(R.id.option4).setOnTouchListener(this);
        findViewById(R.id.option5).setOnTouchListener(this);
        TextView utv = findViewById(R.id.answer);
        utv.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int temp = 0;
                int action = event.getAction();
                TextView currentView = (TextView) v;
                UnderlineSpan us = new UnderlineSpan();
                switch (action)
                {
                    case DragEvent.ACTION_DRAG_ENTERED:
                    {
                        temp = v.getDrawingCacheBackgroundColor();
                        v.setBackgroundColor(Color.parseColor("#40FFFFFF"));
                        break;
                    }
                    case DragEvent.ACTION_DRAG_EXITED:
                    {
                        v.setBackgroundColor(temp);
                        break;
                    }
                    case DragEvent.ACTION_DROP:
                    {
                        if(!currentView.getText().equals(""))
                        {
                            old.setText(currentView.getText().toString());
                            old.setOnTouchListener(Main.this);
                        }
                        SpannableString spannableString = new SpannableString(event.getClipData().getItemAt(0).getText());
                        spannableString.setSpan(us, 0, spannableString.length(), 0);
                        currentView.setText(spannableString);
                        currentView.setTextColor(Color.WHITE);
                        currentView.setTextSize(((TextView) findViewById(R.id.iFeel)).getTextSize() / getResources().getDisplayMetrics().scaledDensity);
                        v.setBackgroundColor(temp);
                        old = (TextView) event.getLocalState();
                        old.setOnTouchListener(null);
                        if(!roundNo.equals("3/3"))
                            startActivityForResult(intent, 1);
                        else
                            Parent.moduleEnd(Main.this, R.color.stryngOfThought, Main.class, Start.class).show();
                        break;
                    }
                    case DragEvent.ACTION_DRAG_ENDED:
                    {
                        if(!event.getResult())
                        {
                            TextView textView = (TextView) event.getLocalState();
                            textView.setText(text);
                        }
                        break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        TextView textView = (TextView) v;
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN)
        {
            Drawable temp = v.getBackground();
            textView.setBackground(null);
            text = textView.getText().toString();
            if(Build.VERSION.SDK_INT <= 23)
                v.startDrag(ClipData.newPlainText("option", textView.getText()), new View.DragShadowBuilder(v), v, 0);
            else
                v.startDragAndDrop(ClipData.newPlainText("option", textView.getText()), new View.DragShadowBuilder(v), v, 0);
            textView.setText("");
            textView.setBackground(temp);
        }
        return false;
    }
}
