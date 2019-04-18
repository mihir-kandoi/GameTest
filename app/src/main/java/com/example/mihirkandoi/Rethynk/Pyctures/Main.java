package com.example.mihirkandoi.Rethynk.Pyctures;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.example.mihirkandoi.Rethynk.Emotymeter.Start;
import com.example.mihirkandoi.Rethynk.Parent;
import com.example.mihirkandoi.rethynk.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

public class Main extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    int result = 1;
    public static int count = 1;
    Intent intent;
    String roundNo;
    AlertDialog alertDialog;
    static JSONArray jsonArray = null;

    ArrayList<ToggleButton> toggleButtons = new ArrayList<>(4);

    @Override
    public void onBackPressed() {
        result = 0;
        count--;
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1)
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
        setContentView(R.layout.activity_pyctures);

        getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        ((GradientDrawable) findViewById(R.id.scrollView).getBackground()).setColor(Color.WHITE); //set ScrollView background white

        intent = Parent.setRoundNo_and_generateNextIntent(this);
        roundNo = getIntent().getStringExtra("roundNo");
        ((ImageView) findViewById(R.id.story)).setImageResource(getResources().getIdentifier("pyctures_q" + Integer.toString(count), "drawable", getPackageName()));

        if (jsonArray == null)
            try {
                jsonArray = new JSONArray(getIntent().getStringExtra("JSONarray"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        toggleButtons.addAll(Arrays.asList(new ToggleButton[]{findViewById(R.id.option1), findViewById(R.id.option2), findViewById(R.id.option3), findViewById(R.id.option4)}));
        for (int i = 0; i < 4; i++) {
            Drawable emoji = null;
            try {
                emoji = getDrawable(getResources().getIdentifier("emoji_" + jsonArray.getJSONObject(count - 1).getString("option" + Integer.toString(i + 1)), "drawable", getPackageName()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            toggleButtons.get(i).setBackground(emoji);
            toggleButtons.get(i).setOnCheckedChangeListener(this);
        }
        count++;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            buttonView.setAlpha(1f);
            for (ToggleButton toggleButton : toggleButtons)
                if(buttonView.getId() != toggleButton.getId()) {
                    toggleButton.setChecked(false);
                    toggleButton.setAlpha(0.4f);
                }

            if (!roundNo.equals("3/3")) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                startActivityForResult(intent, 1);
            }
            else {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                alertDialog = Parent.moduleEnd(this, R.color.pyctures, Main.class, Start.class);
                alertDialog.show();
            }
        }
        else
            buttonView.setAlpha(0.4f);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if (roundNo.equals("3/3") && alertDialog != null)
            alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
