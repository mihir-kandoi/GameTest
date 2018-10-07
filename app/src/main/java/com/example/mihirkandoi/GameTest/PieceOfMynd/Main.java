package com.example.mihirkandoi.GameTest.PieceOfMynd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.gametest.R;

import java.util.ArrayList;
import java.util.Random;

public class Main extends AppCompatActivity implements View.OnTouchListener, View.OnDragListener{

    int count = 0;
    int result = 1;
    String roundNo;

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
        setContentView(R.layout.activity_pom);
        findViewById(R.id.next).setEnabled(false);
        final View view = findViewById(R.id.a1);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                View temp = findViewById(R.id.answers);
                ViewGroup.LayoutParams layoutParams = temp.getLayoutParams();
                layoutParams.width = view.getWidth() * 4;
                temp.setLayoutParams(layoutParams);
            }
        });
        final Intent intent = Parent.roundNo(this);
        final String roundNo = getIntent().getStringExtra("roundNo");
        this.roundNo = roundNo;
        final int drawables[] = {R.drawable.pom_sad, R.drawable.pom_angry, R.drawable.pom_happy};
        int drawable;
        if(roundNo.equals("1/3"))
            drawable = drawables[0];
        else if(roundNo.equals("2/3"))
            drawable = drawables[1];
        else
            drawable = drawables[2];
        Bitmap image = BitmapFactory.decodeResource(getResources(), drawable);
        ArrayList<ImageView> imageViews = new ArrayList<>(12);
        for(int i = 1; i <= 12 ; i++ )
        {
            imageViews.add((ImageView) findViewById(getResources().getIdentifier("imageView" + Integer.toString(i), "id", getPackageName())));
            findViewById(getResources().getIdentifier("a" + Integer.toString(i), "id", getPackageName())).setOnDragListener(this);
        }
        int xSize = image.getWidth() / 4;
        int ySize = image.getHeight() / 3;
        int temp = 1;
        for(int y = 0 ; y <= image.getHeight() - ySize ; y += ySize)
        {
            for(int x = 0 ; x <= image.getWidth() - xSize ; x += xSize)
            {
                Bitmap tempB = Bitmap.createBitmap(image, x, y, xSize, ySize);
                ImageView tempI = imageViews.get(new Random().nextInt(imageViews.size()));
                tempI.setTag(temp++);
                tempI.setOnTouchListener(this);
                tempI.setImageBitmap(tempB);
                imageViews.remove(tempI);
            }
        }

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if(!roundNo.equals("3/3"))
                    startActivityForResult(intent, 1);
                else
                {
                    Intent intent1 = new Intent(Main.this, SelectImage.class);
                    intent1.putExtra("drawables", drawables);
                    startActivityForResult(intent1, 1);
                }
            }
        });
        findViewById(R.id.hack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.next).setEnabled(true);
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(Build.VERSION.SDK_INT <= 23)
            v.startDrag(null , new View.DragShadowBuilder(v), v, 0);
        else
            v.startDragAndDrop(null, new View.DragShadowBuilder(v), v, 0);
        return true;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        switch (action)
        {
            case DragEvent.ACTION_DRAG_ENTERED:
            {
                v.setBackgroundColor(Color.WHITE); //highlight view
                break;
            }
            case DragEvent.ACTION_DRAG_EXITED:
            {
                v.setBackgroundColor(Color.TRANSPARENT); //set color back to original
                break;
            }
            case DragEvent.ACTION_DROP:
            {
                ImageView question = (ImageView) event.getLocalState();
                ImageView answer = (ImageView) v;
                if(question.getTag().toString().equals(answer.getTag())) //if piece is fitted correctly
                {
                    answer.setImageBitmap(((BitmapDrawable) question.getDrawable()).getBitmap());
                    question.setImageBitmap(null);
                    question.setOnTouchListener(null);
                    count++;
                    if(count == 12) //if the puzzle is complete (12 pieces)
                        findViewById(R.id.next).setEnabled(true);
                }
                else //piece fitted incorrectly
                    v.setBackgroundColor(Color.TRANSPARENT); //set color back to original
                break;
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
