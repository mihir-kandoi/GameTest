package com.example.mihirkandoi.GameTest.StryngOfThought;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import com.example.mihirkandoi.gametest.R;

public class UnderlinedTextView extends android.support.v7.widget.AppCompatTextView {
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
        Paint paint = new Paint();
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
