package com.example.mihirkandoi.GameTest.Grydlock;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.mihirkandoi.GameTest.SelectWords;
import com.example.mihirkandoi.gametest.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Main extends AppCompatActivity implements View.OnTouchListener{

    TextView allTextViews[][] = new TextView[10][10];
    ArrayList<TextView> occupied = new ArrayList<>();
    ArrayList<TextView> right = new ArrayList<>();
    ArrayList<TextView> down = new ArrayList<>();
    ArrayList<TextView> finale = new ArrayList<>();
    ArrayList<TextView> found = new ArrayList<>();
    ArrayList<String> words = new ArrayList<>(Arrays.asList("CALM", "VAGUE", "IRE", "CARE", "GUTS", "GLAD", "SHAME", "GLEE"));
    ArrayList<String> wordsFound = new ArrayList<>();
    boolean isRight = true;
    int result = 1;
    int temp[]=new int[2];

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
        setContentView(R.layout.activity_grydlock);
        findViewById(R.id.submit).setEnabled(false);
        String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        for(int i = 0,c = 1; i < 10; i++)
        {
            for(int j = 0; j < 10; j++, c++)
            {
                allTextViews[i][j] = findViewById(getResources().getIdentifier("textView" + Integer.toString(c), "id", getPackageName()));
                allTextViews[i][j].setText(Character.toString(alphabets.charAt(random.nextInt(26))));
                allTextViews[i][j].setOnTouchListener(this);
            }
        }

        ArrayList<TextView> wordTextViews = new ArrayList<>();
        restart: for(int i=0 ; i<8 ; i++)
        {
            boolean isVertical = random.nextBoolean();
            int startR = random.nextInt(10 - words.get(i).length());
            int startC = random.nextInt(10);

            if(!isVertical)
            {
                int idk = startR;
                startR = startC;
                startC = idk;

                for(int j = startC; j < startC + words.get(i).length() ; j++)
                    if(occupied.contains(allTextViews[startR][j]) && !allTextViews[startR][j].getText().equals(Character.toString(words.get(i).charAt(j - startC))))
                    {
                        wordTextViews.clear();
                        i--;
                        continue restart;
                    }
                    else
                        wordTextViews.add(allTextViews[startR][j]);
            }
            else
                for(int j = startR ; j < startR + words.get(i).length() ; j++)
                    if(occupied.contains(allTextViews[j][startC]) && !allTextViews[j][startC].getText().equals(Character.toString(words.get(i).charAt(j - startR))))
                    {
                        wordTextViews.clear();
                        i--;
                        continue restart;
                    }
                    else
                        wordTextViews.add(allTextViews[j][startC]);

                occupied.addAll(wordTextViews);
                for(int k=0; k<wordTextViews.size(); k++)
                    wordTextViews.get(k).setText(Character.toString(words.get(i).charAt(k)));
                wordTextViews.clear();
        }

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Intent intent = new Intent(Main.this, SelectWords.class);
                intent.putStringArrayListExtra("all", words);
                intent.putStringArrayListExtra("found", wordsFound);
                intent.putExtra("color", R.color.storyLyne);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        int i,j=0;
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                v.setBackgroundColor(Color.parseColor("#FFFFD76A"));
                outer: for(i = 0 ; i < 10 ; i++)
                    for(j = 0; j < 10; j++)
                        if(v.getId() == allTextViews[i][j].getId())
                            break outer;
                for(int k=j;k<10;k++)
                    right.add(allTextViews[i][k]);
                for(int k=i;k<10;k++)
                    down.add(allTextViews[k][j]);
                finale.add((TextView) v);
                break;
            case MotionEvent.ACTION_MOVE:
                Rect rect;
                for(int l=1;l<right.size();l++)
                {
                    right.get(l).getLocationOnScreen(temp);
                    rect=new Rect(temp[0], temp[1], temp[0] + right.get(l).getWidth(), temp[1] + right.get(l).getHeight());
                    if(rect.contains((int) event.getRawX(), (int) event.getRawY()))
                    {
                        if(!isRight)
                        {
                            for(TextView textView : finale)
                            {
                                if (found.contains(textView))
                                    continue;
                                textView.setBackgroundColor(Color.TRANSPARENT);
                            }
                            finale.clear();
                        }
                        for(int m=finale.size();m <= l;m++)
                        {
                            right.get(m).setBackgroundColor(Color.parseColor("#FFFFD76A"));
                            finale.add(right.get(m));
                            isRight = true;
                        }
                    }
                }
                for(int l=1;l<down.size();l++)
                {
                    down.get(l).getLocationOnScreen(temp);
                    rect=new Rect(temp[0], temp[1], temp[0] + down.get(l).getWidth(), temp[1] + down.get(l).getHeight());
                    if(rect.contains((int) event.getRawX(), (int) event.getRawY()))
                    {
                        if(isRight)
                        {
                            for(TextView textView : finale)
                            {
                                if (found.contains(textView))
                                    continue;
                                textView.setBackgroundColor(Color.TRANSPARENT);
                            }
                            finale.clear();
                        }
                        for(int m=finale.size();m <= l;m++)
                        {
                            down.get(m).setBackgroundColor(Color.parseColor("#FFFFD76A"));
                            finale.add(down.get(m));
                            isRight = false;
                        }
                    }
                }
                for(int l=0;l < finale.size() - 1;l++)
                {
                    finale.get(l).getLocationOnScreen(temp);
                    rect=new Rect(temp[0], temp[1], temp[0] + finale.get(l).getWidth(), temp[1] + finale.get(l).getHeight());
                    if(rect.contains((int) event.getRawX(), (int) event.getRawY()))
                    {
                        int temp2=finale.size();
                        for(int m=l+1;m<temp2;m++)
                            finale.get(m).setBackgroundColor(Color.TRANSPARENT);
                        for(int m=l+1;m<temp2;m++)
                            finale.remove(finale.get(l+1));
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                String word = "";
                for(TextView textView : finale)
                    word += textView.getText();
                if(!words.contains(word) || word.isEmpty())
                    for(TextView textView : finale)
                    {
                        if(found.contains(textView))
                            continue;
                        textView.setBackgroundColor(Color.TRANSPARENT);
                    }
                else
                {
                    found.addAll(finale);
                    wordsFound.add(word);
                    findViewById(R.id.submit).setEnabled(true);
                }
                right.clear();
                down.clear();
                finale.clear();
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}