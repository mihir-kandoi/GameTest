package com.example.mihirkandoi.GameTest.PieceOfMynd;

import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.GameTest.Pyctures.Start;
import com.example.mihirkandoi.gametest.R;

public class SelectImage extends AppCompatActivity {

    int result = 1;

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
        setContentView(R.layout.activity_pom_select_image);
        Button done = findViewById(R.id.done);
        done.setEnabled(false);
        int images[] = getIntent().getIntArrayExtra("drawables");
        int options[] = {R.id.option1, R.id.option2, R.id.option3};
        for(int i = 0; i < 3; i++)
        {
            StateListDrawable stateListDrawable = new StateListDrawable();
            LayerDrawable layerDrawable = (LayerDrawable) getDrawable(R.drawable.image_selected);
            layerDrawable.mutate();
            layerDrawable.setDrawable(0, getDrawable(images[i]));
            stateListDrawable.addState(new int[] {android.R.attr.state_checked}, layerDrawable);
            stateListDrawable.addState(new int[] {-android.R.attr.state_checked}, getDrawable(images[i]));
            findViewById(options[i]).setBackground(stateListDrawable);
        }
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Parent.moduleEnd(SelectImage.this, R.color.pieceOfMynd, Main.class, Start.class).show();
            }
        });
        final ToggleButton image1 = findViewById(R.id.option1);
        final ToggleButton image2 = findViewById(R.id.option2);
        final ToggleButton image3 = findViewById(R.id.option3);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image1.setChecked(true);
                image2.setChecked(false);
                image3.setChecked(false);
                findViewById(R.id.done).setEnabled(true);
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image1.setChecked(false);
                image2.setChecked(true);
                image3.setChecked(false);
                findViewById(R.id.done).setEnabled(true);
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image1.setChecked(false);
                image2.setChecked(false);
                image3.setChecked(true);
                findViewById(R.id.done).setEnabled(true);
            }
        });
    }
}
