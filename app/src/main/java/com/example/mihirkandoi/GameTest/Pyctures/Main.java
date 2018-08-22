package com.example.mihirkandoi.GameTest.Pyctures;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.gametest.R;

public class Main extends AppCompatActivity {

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
        setContentView(R.layout.activity_pyctures);
        final Intent intent = Parent.roundNo(this);
        final String roundNo = getIntent().getStringExtra("roundNo");
        int drawables[] = {R.drawable.story1, R.drawable.story2, R.drawable.story3};
        int drawable;
        if(roundNo.equals("1/3"))
            drawable = drawables[0];
        else if(roundNo.equals("2/3"))
            drawable = drawables[1];
        else
            drawable = drawables[2];
        ((ImageView) findViewById(R.id.story)).setImageResource(drawable);
        int options[] = {R.id.option4, R.id.option1, R.id.option5, R.id.option3, R.id.option2};
        for(int i : options)
        {
            StateListDrawable stateListDrawable = new StateListDrawable();
            LayerDrawable layerDrawable = (LayerDrawable) getDrawable(R.drawable.image_selected);
            layerDrawable.mutate();
            layerDrawable.setDrawable(0, findViewById(i).getBackground());
            final float scale = getResources().getDisplayMetrics().density;
            ((GradientDrawable) layerDrawable.getDrawable(1)).setStroke((int) (4 * scale + 0.5f), getResources().getColor(R.color.pyctures, getTheme()));
            ((GradientDrawable) layerDrawable.getDrawable(1)).setCornerRadius((int) (10 * scale + 0.5f));
            stateListDrawable.addState(new int[] {android.R.attr.state_checked}, layerDrawable);
            stateListDrawable.addState(new int[] {-android.R.attr.state_checked}, findViewById(i).getBackground());
            findViewById(i).setBackground(stateListDrawable);
        }
        ToggleButton happy = findViewById(R.id.option1);
        ToggleButton sad = findViewById(R.id.option4);
        ToggleButton angry = findViewById(R.id.option5);
        ToggleButton fear = findViewById(R.id.option3);
        ToggleButton laughing = findViewById(R.id.option2);
        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
