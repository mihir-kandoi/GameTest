package com.example.mihirkandoi.Rethynk.PieceOfMynd;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.mihirkandoi.Rethynk.Parent;
import com.example.mihirkandoi.Rethynk.SonycSound.Start;
import com.example.mihirkandoi.rethynk.R;

import java.util.ArrayList;
import java.util.Arrays;

public class SelectImage extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    int result = 1;
    AlertDialog alertDialog;

    ArrayList<ToggleButton> toggleButtons = new ArrayList<>(5);

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

        getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Button done = findViewById(R.id.done);
        done.setEnabled(false);

        final ArrayList<Integer> images = getIntent().getIntegerArrayListExtra("drawables");
        toggleButtons.addAll(Arrays.asList(new ToggleButton[]{findViewById(R.id.option1), findViewById(R.id.option2), findViewById(R.id.option3), findViewById(R.id.option4), findViewById(R.id.option5)}));
        for(int i = 0; i < 5; i++)
        {
            StateListDrawable stateListDrawable = new StateListDrawable();
            LayerDrawable layerDrawable = (LayerDrawable) getDrawable(R.drawable.image_selected);
            layerDrawable.mutate();
            layerDrawable.setDrawable(0, getDrawable(images.get(i)));
            ((GradientDrawable) layerDrawable.getDrawable(1)).setStroke(Parent.convertToPixel(this, 4), getResources().getColor(R.color.pieceOfMynd, getTheme()));
            stateListDrawable.addState(new int[] {android.R.attr.state_checked}, layerDrawable);
            stateListDrawable.addState(new int[] {-android.R.attr.state_checked}, getDrawable(images.get(i)));
            toggleButtons.get(i).setBackground(stateListDrawable);
            toggleButtons.get(i).setOnCheckedChangeListener(this);
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