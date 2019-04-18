package com.example.mihirkandoi.Rethynk.StryngOfThought;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.mihirkandoi.Rethynk.Parent;
import com.example.mihirkandoi.Rethynk.PieceOfMynd.Start;
import com.example.mihirkandoi.rethynk.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Main extends AppCompatActivity implements View.OnTouchListener{

    String roundNo;
    AlertDialog alertDialog;
    int result = 1;
    static int count = 0;
    static JSONArray jsonArray = null;

    String text;
    TextView old;

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
        setContentView(R.layout.activity_sot);

        final String roundNo = getIntent().getStringExtra("roundNo");
        this.roundNo = roundNo;
        final Intent intent = Parent.setRoundNo_and_generateNextIntent(this);

        if(jsonArray == null)
            try {
                jsonArray = new JSONArray(getIntent().getStringExtra("JSONarray"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        try {
            ((TextView) findViewById(R.id.questions)).setText(jsonArray.getJSONObject(count).getString("question"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<TextView> textViews = new ArrayList<>(5);
        ArrayList<String> options = new ArrayList<>(5);
        for(int i = 1; i <= 5; i++)
        {
            textViews.add((TextView) findViewById(getResources().getIdentifier("option" + Integer.toString(i), "id", getPackageName())));
            textViews.get(i - 1).setOnTouchListener(this);
            try {
                String option = jsonArray.getJSONObject(count).getString("option" + Integer.toString(i));
                textViews.get(i - 1).setText(option);
                options.add(option);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        count++;

        try {
            Parent.infoIcon(this, R.color.stryngOfThought, options);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView utv = findViewById(R.id.answer);
        final UnderlineSpan us = new UnderlineSpan();
        utv.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                TextView currentView = (TextView) v;
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
                            String text = currentView.getText().toString();
                            text = text.replaceFirst(Character.toString(text.charAt(0)), Character.toString(Character.toUpperCase(text.charAt(0))));
                            old.setText(text);
                            old.setOnTouchListener(Main.this);
                        }
                        String oldText = event.getClipData().getItemAt(0).getText().toString();
                        oldText = oldText.replaceFirst(Character.toString(oldText.charAt(0)), Character.toString(Character.toLowerCase(oldText.charAt(0))));
                        SpannableString spannableString = new SpannableString(oldText);
                        spannableString.setSpan(us, 0, spannableString.length(), 0);
                        currentView.setText(spannableString);
                        currentView.setTextColor(Color.WHITE);
                        currentView.setTextSize(((TextView) findViewById(R.id.iFeel)).getTextSize() / getResources().getDisplayMetrics().scaledDensity);
                        v.setBackgroundColor(Color.TRANSPARENT);
                        old = (TextView) event.getLocalState();
                        old.setOnTouchListener(null);

                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        if(!roundNo.equals("3/3"))
                            startActivityForResult(intent, 1);
                        else {
                            alertDialog = Parent.moduleEnd(Main.this, R.color.stryngOfThought, Main.class, Start.class);
                            alertDialog.show();
                        }
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

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if(roundNo.equals("3/3") && alertDialog != null)
            alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}

class UnderlinedTextView extends androidx.appcompat.widget.AppCompatTextView {
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
    }
}
