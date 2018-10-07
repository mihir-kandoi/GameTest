package com.example.mihirkandoi.GameTest.SeeTheSygns;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.GameTest.Grydlock.Start;
import com.example.mihirkandoi.gametest.R;

import java.util.ArrayList;

public class Main extends AppCompatActivity implements  CompoundButton.OnCheckedChangeListener{

    int result = 1;
    Intent intent;
    String roundNo;
    ArrayList<ToggleButton> toggleButtons;
    AlertDialog alertDialog;

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
        setContentView(R.layout.activity_sts);
        Parent.infoIcon(this, R.color.seeTheSygns);
        intent = Parent.roundNo(this);
        roundNo = getIntent().getStringExtra("roundNo");
        toggleButtons = Parent.setToggleButtons(this, 5);
        int drawables[] = {R.drawable.justice, R.drawable.flag, R.drawable.wheelchair};
        int drawable;
        switch (roundNo) {
            case "1/3":
                drawable = drawables[0];
                break;
            case "2/3":
                drawable = drawables[1];
                break;
            default:
                drawable = drawables[2];
                break;
        }
        ((ImageView) findViewById(R.id.symbol)).setImageResource(drawable);
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
                alertDialog = Parent.moduleEnd(Main.this, R.color.seeTheSygns, Main.class, Start.class);
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
