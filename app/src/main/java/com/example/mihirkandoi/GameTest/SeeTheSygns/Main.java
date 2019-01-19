package com.example.mihirkandoi.GameTest.SeeTheSygns;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.example.mihirkandoi.GameTest.Grydlock.Start;
import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.gametest.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Main extends AppCompatActivity implements  CompoundButton.OnCheckedChangeListener{

    int result = 1;
    static int count = 0;
    Intent intent;
    String roundNo;
    ArrayList<ToggleButton> toggleButtons;
    AlertDialog alertDialog;
    static JSONArray jsonArray = null;

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
        setContentView(R.layout.activity_sts);

        // set navigation/status bar black
        getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // make and set custom background
        findViewById(R.id.cs).setBackground(new Drawable() {

            Paint paint = new Paint();
            @Override
            public void draw(@NonNull Canvas canvas) {
                Rect rect = getBounds();
                paint.setColor(Color.WHITE);
                canvas.drawRect(0,0, rect.width(),rect.height() / 2, paint);
                paint.setColor(getColor(R.color.colorPrimaryDark));
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

        //set info icon and get json array from intent
        try {
            if (jsonArray == null)
                jsonArray = new JSONArray(getIntent().getStringExtra("JSONarray"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ((ImageView) findViewById(R.id.symbol)).setImageResource(getResources().getIdentifier("sts_q" + Integer.toString((count + 1)), "drawable", getPackageName()));

        //set option data
        ArrayList<String> options = new ArrayList<>(5);
        for(int i=1;i<=5;i++) {
            ToggleButton toggleButton = findViewById(getResources().getIdentifier("option" + Integer.toString(i), "id", getPackageName()));
            try {
                String option = jsonArray.getJSONObject(count).getString("option" + Integer.toString(i));
                toggleButton.setText(option);
                toggleButton.setTextOn(option);
                toggleButton.setTextOff(option);
                options.add(option);
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        count++;

        try {
            Parent.infoIcon(this, R.color.colorPrimaryDark, options);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        intent = Parent.setRoundNo_and_generateNextIntent(this);
        roundNo = getIntent().getStringExtra("roundNo");
        toggleButtons = Parent.setToggleButtons(this, 5);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            buttonView.setTextColor(Color.WHITE);
            Parent.toggleButtons(toggleButtons, buttonView);
            if (!roundNo.equals("3/3"))
                startActivityForResult(intent, 1);
            else
            {
                alertDialog = Parent.moduleEnd(Main.this, R.color.colorPrimaryDark, Main.class, Start.class);
                alertDialog.show();
            }
        }
        else
            buttonView.setTextColor(Color.BLACK);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if(roundNo.equals("3/3") && alertDialog != null)
            alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
