package com.example.mihirkandoi.GameTest.Pyctures;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.gametest.R;

public class Main extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    int result = 1;
    ToggleButton[] toggleButtons;
    Intent intent;
    String roundNo;

    @Override
    public void onBackPressed() {
        result = 0;
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
        intent = Parent.roundNo(this);
        roundNo = getIntent().getStringExtra("roundNo");
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
        toggleButtons = new ToggleButton[5];
        for(int i = 1; i <= 5; i++)
        {
            toggleButtons[i - 1] = findViewById(getResources().getIdentifier("option" + Integer.toString(i), "id", getPackageName()));
            toggleButtons[i - 1].setOnCheckedChangeListener(this);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked)
        {
            Parent.toggleButtons(this, toggleButtons, 5, buttonView);
            if (!roundNo.equals("3/3"))
                startActivityForResult(intent, 1);
            else
                Parent.moduleEnd(this, R.color.pyctures, Main.class, Start.class).show();
        }
    }
}
