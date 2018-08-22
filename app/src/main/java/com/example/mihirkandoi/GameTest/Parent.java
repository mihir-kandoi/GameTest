package com.example.mihirkandoi.GameTest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.mihirkandoi.gametest.R;

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
                View view = obj.getLayoutInflater().inflate(R.layout.info, null);
                GradientDrawable gd = (GradientDrawable) view.getBackground();
                final float scale = obj.getResources().getDisplayMetrics().density;
                gd.setStroke((int) (4 * scale + 0.5f), obj.getResources().getColor(color, obj. getTheme()));
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
        AlertDialog alertDialog = new AlertDialog.Builder(obj).setView(view).create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view.findViewById(R.id.nextGame).setBackgroundColor(obj.getResources().getColor(color, obj.getTheme()));
        view.findViewById(R.id.restartGame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(obj, sameModule);
                in.putExtra("roundNo", "1/3");
                obj.finish();
                obj.startActivityForResult(in, 1);
            }
        });
        view.findViewById(R.id.nextGame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        cs.measure(View.MeasureSpec.makeMeasureSpec(size.x, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(size.y, View.MeasureSpec.EXACTLY));
        cs.layout(0, 0, cs.getMeasuredWidth(), cs.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(cs.getMeasuredWidth(), cs.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        cs.draw(canvas);
        ImageView imageView = obj.findViewById(R.id.example);
        imageView.setImageBitmap(bitmap);
        ((TextView) obj.findViewById(R.id.instruction1)).setText(instruction1);
        ((TextView) obj.findViewById(R.id.instruction2)).setText(instruction2);
        ((Button) obj.findViewById(R.id.start)).setText(buttonText);
        Button startButton = obj.findViewById(R.id.start);
        startButton.setBackgroundColor(obj.getResources().getColor(color, obj.getTheme()));
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(obj, start);
                intent.putExtra("roundNo","1/3");
                obj.startActivityForResult(intent, 1);
            }
        });
    }

    public static void compoundButtons(AppCompatActivity obj, int no)
    {
        for(int i = 1; i <= no; i++)
        {
            ToggleButton tb = obj.findViewById(obj.getResources().getIdentifier("option" + Integer.toString(i),"id", obj.getPackageName()));
            tb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
