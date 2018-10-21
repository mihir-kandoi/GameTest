package com.example.mihirkandoi.GameTest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.mihirkandoi.gametest.R;

import java.util.ArrayList;
import java.util.Random;

public class Parent {

    public static void start(final AppCompatActivity obj, String moduleName, String word, String wordDef, int color, final Class nextClass)
    {
        SpannableString ss = new SpannableString(moduleName);
        int index = moduleName.indexOf("y");
        ss.setSpan(new ForegroundColorSpan(Color.WHITE), index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView) obj.findViewById(R.id.word)).setText(word);
        ((TextView) obj.findViewById(R.id.wordDef)).setText(wordDef);
        ((TextView) obj.findViewById(R.id.module_name)).setText(ss);
        obj.findViewById(R.id.startGame).setBackgroundColor(obj.getResources().getColor(color, obj.getTheme()));
        obj.findViewById(R.id.startGame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Intent intent = new Intent(obj, nextClass);
                obj.startActivity(intent);
            }
        });
    }

    public static void infoIcon(final AppCompatActivity obj, final int color)
    {
        obj.findViewById(R.id.info_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = obj.getLayoutInflater().inflate(R.layout.info, null);
                view.findViewById(R.id.parent).setBackground(new Drawable() {

                    Paint paint;
                    Path path;
                    Random random;
                    Rect bounds;
                    int strokeWidth;

                    @Override
                    public void draw(@NonNull Canvas canvas) {
                        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        strokeWidth = convertToPixel(3);
                        paint.setStrokeWidth(strokeWidth);
                        paint.setPathEffect(new CornerPathEffect(convertToPixel(6)));
                        random = new Random();
                        path = new Path();
                        bounds = getBounds();
                        path.moveTo(convertToPixel(random.nextInt(21)) + strokeWidth, convertToPixel(random.nextInt(21)) + strokeWidth);
                        int Xmax = bounds.width() - strokeWidth;
                        int Xmin = Xmax - convertToPixel(21);
                        path.lineTo(random.nextInt((Xmax - Xmin) + 1) + Xmin, convertToPixel(random.nextInt(21)) + strokeWidth);
                        int Ymax = bounds.height() - strokeWidth;
                        int Ymin = Ymax - convertToPixel(21);
                        path.lineTo(random.nextInt((Xmax - Xmin) + 1) + Xmin, random.nextInt((Ymax - Ymin) + 1) + Ymin);
                        path.lineTo(convertToPixel(random.nextInt(21)) + strokeWidth, random.nextInt((Ymax - Ymin) + 1) + Ymin);
                        path.close();
                        paint.setStyle(Paint.Style.FILL);
                        paint.setColor(Color.WHITE);
                        canvas.drawPath(path, paint);
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setColor(view.getResources().getColor(color, obj.getTheme()));
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
                        return (int) (dp * view.getResources().getDisplayMetrics().density + 0.5f);
                    }
                });
                AlertDialog alertDialog = new AlertDialog.Builder(obj).setView(view).create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });
    }

    public static Intent roundNo(AppCompatActivity obj)
    {
        String roundNo = obj.getIntent().getStringExtra("roundNo");
        TextView tv = obj.findViewById(R.id.roundNo);
        tv.setText(roundNo);
        Intent intent = new Intent(obj, obj.getClass());
        if(roundNo.equals("1/3"))
            intent.putExtra("roundNo", "2/3");
        else
            intent.putExtra("roundNo", "3/3");
        return intent;
    }

    public static AlertDialog moduleEnd(final AppCompatActivity obj, int color, final Class sameModule, final Class nextModule)
    {
        View view = obj.getLayoutInflater().inflate(R.layout.complete, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(obj).setView(view).create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view.findViewById(R.id.nextGame).setBackgroundColor(obj.getResources().getColor(color, obj.getTheme()));
        view.findViewById(R.id.restartGame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                alertDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Intent in = new Intent(obj, sameModule);
                in.putExtra("roundNo", "1/3");
                obj.finish();
                obj.startActivityForResult(in, 1);
            }
        });
        view.findViewById(R.id.nextGame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                alertDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Intent intent = new Intent(obj, nextModule);
                obj.startActivity(intent);
            }
        });
        return alertDialog;
    }

    public static void instructions(final AppCompatActivity obj, int layout, int color, String instruction1, String instruction2, String buttonText, final Class start)
    {
        Point size = new Point();
        obj.getWindowManager().getDefaultDisplay().getSize(size);
        ConstraintLayout cs = new ConstraintLayout(obj.getBaseContext());
        obj.getLayoutInflater().inflate(layout, cs, true);
        if(layout == R.layout.activity_sel_words)
            cs.findViewById(R.id.submit).setBackground(obj.getDrawable(color));
        cs.measure(View.MeasureSpec.makeMeasureSpec(size.x, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(size.y, View.MeasureSpec.EXACTLY));
        cs.layout(0, 0, cs.getMeasuredWidth(), cs.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(cs.getMeasuredWidth(), cs.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        cs.draw(canvas);
        ImageView imageView = obj.findViewById(R.id.example);
        imageView.setImageBitmap(bitmap);
        ((TextView) obj.findViewById(R.id.instruction1)).setText(instruction1);
        ((TextView) obj.findViewById(R.id.instruction2)).setText(instruction2);
        if(instruction2.isEmpty())
            obj.findViewById(R.id.instruction2).setVisibility(View.GONE);
        Button startButton = obj.findViewById(R.id.start);
        startButton.setText(buttonText);
        startButton.setBackgroundColor(obj.getResources().getColor(color, obj.getTheme()));
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Intent intent = new Intent(obj, start);
                intent.putExtra("roundNo","1/3");
                obj.startActivityForResult(intent, 1);
            }
        });
    }

    public static ArrayList<ToggleButton> setToggleButtons(AppCompatActivity obj, int size)
    {
        ArrayList<ToggleButton> toggleButtons = new ArrayList<>(size);
        for(int i = 1; i <= size; i++)
        {
            toggleButtons.add((ToggleButton) obj.findViewById(obj.getResources().getIdentifier("option" + Integer.toString(i), "id", obj.getPackageName())));
            toggleButtons.get(i - 1).setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) obj);
        }
        return toggleButtons;
    }

    public static void toggleButtons(ArrayList<ToggleButton> toggleButtons, View view)
    {
        for(ToggleButton toggleButton : toggleButtons)
            if(view.getId() != toggleButton.getId())
                toggleButton.setChecked(false);
    }

    public static int convertToPixel(AppCompatActivity obj, int dp)
    {
        return (int) (dp * obj.getResources().getDisplayMetrics().density + 0.5f);
    }
}
