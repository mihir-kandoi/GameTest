package com.example.mihirkandoi.GameTest.PieceOfMynd;

import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.GameTest.Pyctures.Start;
import com.example.mihirkandoi.gametest.R;

import java.util.ArrayList;

public class SelectImage extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    int result = 1;
    AlertDialog alertDialog;
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
        setContentView(R.layout.activity_pom_sel_image);

        // set navigation/status bar black
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().getDecorView().setSystemUiVisibility(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Button done = findViewById(R.id.done);
        done.setEnabled(false);
        final ArrayList<Integer> images = getIntent().getIntegerArrayListExtra("drawables");
        int options[] = {R.id.option1, R.id.option2, R.id.option3, R.id.option4, R.id.option5};
        for(int i = 0; i < 5; i++)
        {
            StateListDrawable stateListDrawable = new StateListDrawable();
            LayerDrawable layerDrawable = (LayerDrawable) getDrawable(R.drawable.image_selected);
            layerDrawable.mutate();
            layerDrawable.setDrawable(0, getDrawable(images.get(i)));
            stateListDrawable.addState(new int[] {android.R.attr.state_checked}, layerDrawable);
            stateListDrawable.addState(new int[] {-android.R.attr.state_checked}, getDrawable(images.get(i)));
            findViewById(options[i]).setBackground(stateListDrawable);
        }
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                alertDialog = Parent.moduleEnd(SelectImage.this, R.color.pieceOfMynd, Main.class, Start.class);
                Main.drawables.clear();
                alertDialog.show();
                images.clear();
            }
        });
        toggleButtons = Parent.setToggleButtons(this, 5);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked)
        {
            Parent.toggleButtons(toggleButtons, buttonView);
            findViewById(R.id.done).setEnabled(true);
        }
        else
            findViewById(R.id.done).setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if(alertDialog != null)
            alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
