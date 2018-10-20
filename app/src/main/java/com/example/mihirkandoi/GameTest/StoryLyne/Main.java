package com.example.mihirkandoi.GameTest.StoryLyne;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.GameTest.StryngOfThought.Start;
import com.example.mihirkandoi.gametest.R;

import java.util.ArrayList;
import java.util.Random;

public class Main extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    int result = 1;
    Intent intent;
    AlertDialog alertDialog;
    String roundNo;
    ArrayList<ToggleButton> toggleButtons;

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
        setContentView(R.layout.activity_sl);
        Parent.infoIcon(this, R.color.storyLyne);
        intent = Parent.roundNo(this);
        roundNo = getIntent().getStringExtra("roundNo");
        toggleButtons = Parent.setToggleButtons(this, 5);
        findViewById(R.id.cs).setBackground(new Drawable() {

            Paint paint = new Paint();
            @Override
            public void draw(@NonNull Canvas canvas) {
                Rect rect = getBounds();
                paint.setColor(Color.parseColor("#a8e124"));
                canvas.drawRect(0,0, rect.width(),rect.height() / 2, paint);
                paint.setColor(Color.WHITE);
                canvas.drawRect(0,rect.height() / 2, rect.width(), rect.height(), paint);
            }

            @Override
            public void setAlpha(int alpha) {
                paint.setAlpha(alpha);
            }

            @Override
            public void setColorFilter(@Nullable ColorFilter colorFilter) {
                paint.setColorFilter(colorFilter);
            }

            @Override
            public int getOpacity() {
                return PixelFormat.OPAQUE;
            }
        });

        for(int i=1;i<=5;i++) {
            StateListDrawable stateListDrawable = new StateListDrawable();
            SLEmotion slEmotionObj = new SLEmotion();
            stateListDrawable.addState(new int[]{-android.R.attr.state_checked}, slEmotionObj);
            stateListDrawable.addState(new int[]{android.R.attr.state_checked}, new SLEmotion(slEmotionObj.path));
            findViewById(getResources().getIdentifier("option" + Integer.toString(i), "id", getPackageName())).setBackground(stateListDrawable);
        }

        final int[] y = new int[2];
        final Drawable outerDrawable = new Drawable() {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            Path path = new Path();
            Rect bounds;
            Random random = new Random();

            @Override
            public void draw(@NonNull Canvas canvas) {
                if(path.isEmpty()) {
                    bounds = getBounds();
                    paint.setColor(Color.parseColor("#a8e124"));
                    path.moveTo(0, 0);
                    path.lineTo(bounds.width(), 0);
                    int Ymax = bounds.height();
                    int Ymin = Ymax - convertToPixel(26);
                    y[0] = random.nextInt((Ymax - Ymin) + 1) + Ymin;
                    path.lineTo(bounds.width(), y[0]);
                    y[1] = random.nextInt((Ymax - Ymin) + 1) + Ymin;
                    path.lineTo(0, y[1]);
                    path.close();
                }
                canvas.drawPath(path, paint);
            }

            @Override
            public void setAlpha(int alpha) {
                paint.setAlpha(alpha);
            }

            @Override
            public void setColorFilter(@Nullable ColorFilter colorFilter) {
                paint.setColorFilter(colorFilter);
            }

            @Override
            public int getOpacity() {
                return PixelFormat.OPAQUE;
            }

            int convertToPixel(int dp) {
                return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
            }

        };
        findViewById(R.id.outer).setBackground(outerDrawable);

        final Drawable innerDrawable = new Drawable() {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            Path path = new Path();
            Rect bounds;
            Random random = new Random();

            @Override
            public void draw(@NonNull Canvas canvas) {
                if(path.isEmpty()) {
                    paint.setColor(getResources().getColor(R.color.storyLyne, getTheme()));
                    paint.setPathEffect(new CornerPathEffect(convertToPixel(6)));
                    bounds = getBounds();
                    path.moveTo(convertToPixel(random.nextInt(21)), convertToPixel(random.nextInt(21)));
                    int Xmax = bounds.width();
                    int Xmin = Xmax - convertToPixel(21);
                    path.lineTo(random.nextInt((Xmax - Xmin) + 1) + Xmin, convertToPixel(random.nextInt(21)));
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) findViewById(R.id.inner).getLayoutParams();
                    int offset = layoutParams.topMargin + layoutParams.bottomMargin;
                    path.lineTo(random.nextInt((Xmax - Xmin) + 1) + Xmin, y[0] - offset);
                    path.lineTo(convertToPixel(random.nextInt(11)), y[1] - offset);
                    path.close();
                }
                canvas.drawPath(path, paint);
            }

            @Override
            public void setAlpha(int alpha) {
                paint.setAlpha(alpha);
            }

            @Override
            public void setColorFilter(@Nullable ColorFilter colorFilter) {
                paint.setColorFilter(colorFilter);
            }

            @Override
            public int getOpacity() {
                return PixelFormat.OPAQUE;
            }

            int convertToPixel(int dp) {
                return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
            }
        };
        findViewById(R.id.inner).setBackground(innerDrawable);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Parent.toggleButtons(toggleButtons, buttonView);
            if (!roundNo.equals("3/3"))
                startActivityForResult(intent, 1);
            else
            {
                alertDialog = Parent.moduleEnd(Main.this, R.color.storyLyne, Main.class, Start.class);
                alertDialog.show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if(roundNo.equals("3/3") && alertDialog != null)
            alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    class SLEmotion extends Drawable
    {
        Paint paint;
        Path path;
        Random random;
        Rect bounds;
        int strokeWidth;

        void init()
        {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(getResources().getColor(R.color.storyLyne, getTheme()));
            strokeWidth = convertToPixel(3);
            paint.setStrokeWidth(strokeWidth);
            paint.setPathEffect(new CornerPathEffect(convertToPixel(4)));
            random = new Random();
        }

        SLEmotion()
        {
            init();
            path = new Path();
            paint.setStyle(Paint.Style.STROKE);
        }

        SLEmotion(Path path)
        {
            init();
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            this.path = path;
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            if(path.isEmpty()) {
                bounds = getBounds();
                path.moveTo(convertToPixel(random.nextInt(11)) + strokeWidth, convertToPixel(random.nextInt(11)) + strokeWidth);
                int Xmax = bounds.width() - strokeWidth;
                int Xmin = Xmax - convertToPixel(11);
                path.lineTo(random.nextInt((Xmax - Xmin) + 1) + Xmin, convertToPixel(random.nextInt(11)) + strokeWidth);
                int Ymax = bounds.height() - strokeWidth;
                int Ymin = Ymax - convertToPixel(11);
                path.lineTo(random.nextInt((Xmax - Xmin) + 1) + Xmin, random.nextInt((Ymax - Ymin) + 1) + Ymin);
                path.lineTo(convertToPixel(random.nextInt(11)) + strokeWidth, random.nextInt((Ymax - Ymin) + 1) + Ymin);
                path.close();
            }
            canvas.drawPath(path, paint);
        }

        @Override
        public void setAlpha(int alpha) {
            paint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {
            paint.setColorFilter(colorFilter);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.OPAQUE;
        }

        int convertToPixel(int dp) {
            return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
        }
    }
}