package com.example.mihirkandoi.GameTest.Pyctures;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.example.mihirkandoi.GameTest.Emotymeter.Start;
import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.gametest.R;

import java.util.ArrayList;

public class Main extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    int result = 1;
    public static int count = 1;
    ArrayList<ToggleButton> toggleButtons;
    Intent intent;
    String roundNo;
    AlertDialog alertDialog;

    @Override
    public void onBackPressed() {
        result = 0;
        count--;
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
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
        setContentView(R.layout.activity_pyctures);

        // set status bar black
        getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        ((GradientDrawable) findViewById(R.id.scrollView).getBackground()).setColor(Color.WHITE); //set scrollview bg white

        intent = Parent.setRoundNo_and_generateNextIntent(this);
        roundNo = getIntent().getStringExtra("roundNo");
        ((ImageView) findViewById(R.id.story)).setImageResource(getResources().getIdentifier("pyctures_q" + Integer.toString(count++), "drawable", getPackageName()));
        int options[] = {R.id.option4, R.id.option1, R.id.option5, R.id.option3, R.id.option2};
        for(int i : options)
        {
            StateListDrawable stateListDrawable = new StateListDrawable();
            LayerDrawable layerDrawable = (LayerDrawable) getDrawable(R.drawable.image_selected);
            layerDrawable.mutate();
            layerDrawable.setDrawable(0, findViewById(i).getBackground());
            ((GradientDrawable) layerDrawable.getDrawable(1)).setStroke(Parent.convertToPixel(this, 4), getResources().getColor(R.color.pyctures, getTheme()));
            ((GradientDrawable) layerDrawable.getDrawable(1)).setCornerRadius(Parent.convertToPixel(this, 10));
            stateListDrawable.addState(new int[] {android.R.attr.state_checked}, layerDrawable);
            stateListDrawable.addState(new int[] {-android.R.attr.state_checked}, findViewById(i).getBackground());
            findViewById(i).setBackground(stateListDrawable);
        }
        toggleButtons = Parent.setToggleButtons(this, 5);
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
                alertDialog = Parent.moduleEnd(this, R.color.pyctures, Main.class, Start.class);
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
}
