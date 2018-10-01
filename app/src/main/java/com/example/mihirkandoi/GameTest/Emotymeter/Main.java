package com.example.mihirkandoi.GameTest.Emotymeter;

import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.GameTest.SonycSound.Start;
import com.example.mihirkandoi.gametest.R;

import java.util.Random;

public class Main extends AppCompatActivity implements View.OnClickListener{

    Intent intent;
    String roundNo;
    int result = 1;
    final Handler handler = new Handler();

    @Override
    public void onBackPressed() {
        result = 0;
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
        setContentView(R.layout.activity_emotymeter);
        intent = Parent.roundNo(this);
        roundNo = getIntent().getStringExtra("roundNo");
        final ConstraintLayout layout = findViewById(R.id.layout);
        for (int i = 13; i <= 20; i++)
            layout.removeView(findViewById(getResources().getIdentifier("imageView" + Integer.toString(i), "id", getPackageName())));
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int param = Parent.convertToPixel(this, 75);
        final int drawables[] = {R.drawable.angry_emoji, R.drawable.fear_emoji, R.drawable.happy_emoji, R.drawable.laughing_emoji, R.drawable.sad_emoji};
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Button fallingEmoji = new Button(getApplicationContext());
                fallingEmoji.setOnClickListener(Main.this);
                fallingEmoji.setBackground(getDrawable(drawables[new Random().nextInt(5)]));
                layout.addView(fallingEmoji);
                fallingEmoji.setLayoutParams(new ConstraintLayout.LayoutParams(Parent.convertToPixel(Main.this, 75), Parent.convertToPixel(Main.this, 75)));
                fallingEmoji.setX(new Random().nextInt(displayMetrics.widthPixels + 1) - (param / 2));
                fallingEmoji.setY(-fallingEmoji.getLayoutParams().height );
                fallingEmoji.animate().translationY(displayMetrics.heightPixels + fallingEmoji.getLayoutParams().height).setDuration(3000).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        layout.removeView(fallingEmoji);
                    }
                }).setInterpolator(null);
                handler.postDelayed(this, 400);
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onClick(View v) {
        if(!roundNo.equals("3/3"))
            startActivityForResult(intent, 1);
        else
            Parent.moduleEnd(this, R.color.storyLyne, Main.class, Start.class).show();
        handler.removeCallbacksAndMessages(null);
    }
}