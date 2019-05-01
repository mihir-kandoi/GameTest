package com.example.mihirkandoi.Rethynk.Emotymeter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.mihirkandoi.Rethynk.Parent;
import com.example.mihirkandoi.Rethynk.SonycSound.Start;
import com.example.mihirkandoi.rethynk.R;

import java.util.Random;

public class Main extends AppCompatActivity implements View.OnClickListener{

    Intent intent;
    String roundNo;
    AlertDialog alertDialog;
    int result = 1;

    final Handler handler = new Handler();
    Runnable runnable;

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

        intent = Parent.setRoundNo_and_generateNextIntent(this);
        roundNo = getIntent().getStringExtra("roundNo");

        //remove all existing Buttons
        final ConstraintLayout layout = findViewById(R.id.layout);
        for (int i = 13; i <= 20; i++)
            layout.removeView(findViewById(getResources().getIdentifier("imageView" + Integer.toString(i), "id", getPackageName())));

        //"set" all the falling Buttons - thread
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int param = Parent.convertToPixel(this, 75);
        final int[] drawables = {R.drawable.emoji_agreeable, R.drawable.emoji_anger, R.drawable.emoji_anxious, R.drawable.emoji_cheerful, R.drawable.emoji_composed, R.drawable.emoji_confident, R.drawable.emoji_depressed, R.drawable.emoji_diffident, R.drawable.emoji_frustrated, R.drawable.emoji_intolerant, R.drawable.emoji_resilient};
        runnable = new Runnable() {
            @Override
            public void run() {
                final Button fallingEmoji = new Button(Main.this);
                fallingEmoji.setOnClickListener(Main.this);
                fallingEmoji.setBackground(getDrawable(drawables[new Random().nextInt(drawables.length)]));
                layout.addView(fallingEmoji);
                fallingEmoji.setLayoutParams(new ConstraintLayout.LayoutParams(Parent.convertToPixel(Main.this, 75), Parent.convertToPixel(Main.this, 75)));
                fallingEmoji.setX(new Random().nextInt(displayMetrics.widthPixels + 1) - (param / 2));
                fallingEmoji.setY(-fallingEmoji.getLayoutParams().height);
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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if(!roundNo.equals("3/3"))
            startActivityForResult(intent, 1);
        else {
            alertDialog = Parent.moduleEnd(this, R.color.storyLyne, Main.class, Start.class);
            alertDialog.show();
        }
        handler.removeCallbacks(runnable); //stop the recursive thread
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if(roundNo.equals("3/3") && alertDialog != null)
            alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if(!handler.hasMessages(0)) //if the thread is stopped...
            handler.post(runnable); //restart it!
    }
}