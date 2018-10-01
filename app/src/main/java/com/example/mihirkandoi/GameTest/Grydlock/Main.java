package com.example.mihirkandoi.GameTest.Grydlock;

import android.graphics.Color;
import android.graphics.Rect;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mihirkandoi.gametest.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main extends AppCompatActivity implements View.OnTouchListener{

    TextView allTextViews[][] = new TextView[10][10];
    static Word wordObjs[] = new Word[8];
    static ArrayList<TextView> occupied = new ArrayList<>();
    int temp[]=new int[2];
    ArrayList<TextView> right = new ArrayList<>();
    ArrayList<TextView> down = new ArrayList<>();
    ArrayList<TextView> finale = new ArrayList<>();
    ArrayList<String> words = new ArrayList<>(Arrays.asList("HORSE", "MONKEY", "ELEPHANT", "KEYBOARD", "MONITOR", "MOUSE", "INTERNET", "EARTH"));
    boolean isRight = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grydlock);
        findViewById(R.id.submit).setEnabled(false);
        String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int c,r;
        c=r=0;
        Random random = new Random();
        for(int i = 1; i <= 100; i++)
        {
            allTextViews[r][c] = findViewById(getResources().getIdentifier("textView" + Integer.toString(i), "id", getPackageName()));
            allTextViews[r][c].setText(Character.toString(alphabets.charAt(random.nextInt(26))));
            allTextViews[r][c].setOnTouchListener(this);
            if(c++ == 9)
            {
                r++;
                c=0;
            }
        }

        ArrayList<TextView> wordTextViews = new ArrayList<>();
        restart: for(int i=0 ; i<8 ; i++)
        {
            boolean isVertical = random.nextBoolean();
            int startR = random.nextInt(10 - words.get(i).length());
            int startC = random.nextInt(10);

            // TODO - code review
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

                wordObjs[i] = new Word(words.get(i), isVertical, startR, startC, wordTextViews);
                wordTextViews.clear();
        }
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
                                textView.setBackgroundColor(Color.TRANSPARENT);
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
                                textView.setBackgroundColor(Color.TRANSPARENT);
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
                        textView.setBackgroundColor(Color.TRANSPARENT);
                right.clear();
                down.clear();
                finale.clear();
                break;
        }
        return true;
    }
}

class Word
{
    String word;
    boolean isVertical;
    int startR, startC;
    ArrayList<TextView> textViews;

    Word(String word, boolean isVertical, int startR, int startC, ArrayList<TextView> textViews)
    {
        this.word = word;
        this.isVertical = isVertical;
        this.startR = startR;
        this.startC = startC;
        this.textViews = textViews;

        Main.occupied.addAll(textViews);

        for(int i=0; i<textViews.size(); i++)
            textViews.get(i).setText(Character.toString(word.charAt(i)));
    }
}