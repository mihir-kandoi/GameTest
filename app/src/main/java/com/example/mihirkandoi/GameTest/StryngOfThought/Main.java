package com.example.mihirkandoi.GameTest.StryngOfThought;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.GameTest.PieceOfMynd.Start;
import com.example.mihirkandoi.gametest.R;

import java.util.ArrayList;

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
        ArrayList<TextView> textViews = new ArrayList<>(5);
        for(int i = 1; i <= 5; i++)
        {
            textViews.add((TextView) findViewById(getResources().getIdentifier("option" + Integer.toString(i), "id", getPackageName())));
            textViews.get(i - 1).setOnTouchListener(this);
        }
        TextView utv = findViewById(R.id.answer);
        utv.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                TextView currentView = (TextView) v;
                UnderlineSpan us = new UnderlineSpan();
                switch (action)
                {
                    case DragEvent.ACTION_DRAG_ENTERED:
                    {
                        v.setBackgroundColor(Color.parseColor("#40FFFFFF"));
                        break;
                    }
                    case DragEvent.ACTION_DRAG_EXITED:
                    {
                        v.setBackgroundColor(Color.TRANSPARENT);
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
                        v.setBackgroundColor(Color.TRANSPARENT);
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
            return true;
        }
        else if(action == MotionEvent.ACTION_UP)
        {
            textView.setText(text);
            return true;
        }
            return false;
    }
}

class UnderlinedTextView extends android.support.v7.widget.AppCompatTextView {
    Paint paint = new Paint();

    public UnderlinedTextView(Context context) {
        super(context);
    }

    public UnderlinedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public UnderlinedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(getText().equals(""))
        {
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(20f);
            canvas.drawLine(0, getHeight(), getWidth(), getHeight(), paint);
        }
        else
        {
            paint.setColor(getResources().getColor(R.color.stryngOfThought, getContext().getTheme()));
            paint.setStrokeWidth(20f);
            canvas.drawLine(0, getHeight(), getWidth(), getHeight(), paint);
        }
    }
}
