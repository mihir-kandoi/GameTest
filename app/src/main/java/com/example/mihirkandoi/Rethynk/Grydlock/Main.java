package com.example.mihirkandoi.Rethynk.Grydlock;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.core.graphics.ColorUtils;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.mihirkandoi.Rethynk.SelectWords;
import com.example.mihirkandoi.rethynk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Main extends AppCompatActivity implements View.OnTouchListener {

    int result = 1;
    static int count = 0;

    TextView[][] allTextViews = new TextView[10][10];
    ArrayList<TextView> occupied = new ArrayList<>();
    ArrayList<TextView> right = new ArrayList<>();
    ArrayList<TextView> down = new ArrayList<>();
    ArrayList<TextView> finale = new ArrayList<>();
    ArrayList<TextView> found = new ArrayList<>();
    static JSONArray jsonArray = null;
    ArrayList<String> words = new ArrayList<>();
    ArrayList<String> wordsFound = new ArrayList<>();
    ArrayList<TextView> overlappingViews = new ArrayList<>();
    ArrayList<Integer> overlappingColors = new ArrayList<>();
    Random random = new Random();
    boolean isRight = true;
    int color = 0;
    int[] temp = new int[2];
    Intent intent;

    @Override
    public void onBackPressed() {
        result = 0;
        count--;
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
        setContentView(R.layout.activity_grydlock);

        findViewById(R.id.submit).setEnabled(false); //disable forward navigation until a word is found

        intent = new Intent(Main.this, SelectWords.class);
        intent.putStringArrayListExtra("all", words);
        intent.putExtra("color", R.color.storyLyne);

        try {
            if (jsonArray == null)
                jsonArray = new JSONArray(getIntent().getStringExtra("JSONarray"));
            JSONObject jsonObject = jsonArray.getJSONObject(count++);
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext())
                words.add(jsonObject.getString(keys.next()).toUpperCase());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //fill grid with random letters
        String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0, c = 1; i < 10; i++) {
            for (int j = 0; j < 10; j++, c++) {
                allTextViews[i][j] = findViewById(getResources().getIdentifier("textView" + c, "id", getPackageName()));
                allTextViews[i][j].setText(Character.toString(alphabets.charAt(random.nextInt(26))));
                allTextViews[i][j].setOnTouchListener(this);
            }
        }

        // set all the words in place
        ArrayList<TextView> wordTextViews = new ArrayList<>(); // stores the TextViews which will hold the word

        restart:
        for (int i = 0; i < words.size(); i++) {
            boolean isVertical = random.nextBoolean();

            // assuming isVertical is true
            int startRow = random.nextInt(10 - words.get(i).length());
            int startCol = random.nextInt(10);

            if (!isVertical) //if the word is horizontal -
            {
                // reverse the rows and columns
                int idk = startRow;
                startRow = startCol;
                startCol = idk;

                for (int j = startCol; j < startCol + words.get(i).length(); j++)
                    if (occupied.contains(allTextViews[startRow][j]) && !allTextViews[startRow][j].getText().equals(Character.toString(words.get(i).charAt(j - startCol)))) { // if TextView is occupied and does NOT have the same letter as the word to be put in...
                        wordTextViews.clear();

                        //restart the iteration with new indexes
                        i--;
                        continue restart;
                    } else
                        wordTextViews.add(allTextViews[startRow][j]);
            } else // same logic as if(!isVertical) block modified for horizontal word
                for (int j = startRow; j < startRow + words.get(i).length(); j++)
                    if (occupied.contains(allTextViews[j][startCol]) && !allTextViews[j][startCol].getText().equals(Character.toString(words.get(i).charAt(j - startRow)))) {
                        wordTextViews.clear();
                        i--;
                        continue restart;
                    } else
                        wordTextViews.add(allTextViews[j][startCol]);

            // set the TextViews with the characters
            for (int k = 0; k < wordTextViews.size(); k++)
                wordTextViews.get(k).setText(Character.toString(words.get(i).charAt(k)));

            occupied.addAll(wordTextViews);
            wordTextViews.clear();
        }

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                intent.putStringArrayListExtra("found", wordsFound);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        int i, j = 0;
        switch (action) {

            case MotionEvent.ACTION_DOWN:
                color = Color.argb(175, random.nextInt(255), random.nextInt(255), random.nextInt(255)); //get random color
                if (!found.contains(v)) //if TextView is already selected/found
                    v.setBackgroundColor(color);
                else {
                    overlappingViews.add((TextView) v);
                    int ogColor = ((ColorDrawable) v.getBackground()).getColor();
                    overlappingColors.add(ogColor);
                    v.setBackgroundColor(ColorUtils.blendARGB(ogColor, color, 0.5F)); //mix both the colors
                }

                // get row and column of DOWNed TextView
                outer:
                for (i = 0; i < 10; i++)
                    for (j = 0; j < 10; j++)
                        if (v.getId() == allTextViews[i][j].getId())
                            break outer;

                for (int k = j; k < 10; k++) // save all TextViews to the right of the DOWNed TextView
                    right.add(allTextViews[i][k]);
                for (int k = i; k < 10; k++) // save all TextViews below the DOWNed TextView
                    down.add(allTextViews[k][j]);
                finale.add((TextView) v);
                break;

            case MotionEvent.ACTION_MOVE:
                Rect rect;
                for (int l = 1; l < right.size(); l++) { // iterate through all TextViews to the right of the DOWNed TextView
                    right.get(l).getLocationOnScreen(temp);
                    rect = new Rect(temp[0], temp[1], temp[0] + right.get(l).getWidth(), temp[1] + right.get(l).getHeight());
                    if (rect.contains((int) event.getRawX(), (int) event.getRawY())) { // if finger is inside the TextView...
                        if (!isRight) { // orientation changed to down
                            for (TextView textView : finale) {
                                if (found.contains(textView)) //if selected TextView is already in use set back to original color
                                    textView.setBackgroundColor(overlappingColors.get(overlappingViews.indexOf(textView)));
                                else
                                    textView.setBackgroundColor(Color.TRANSPARENT);
                            }
                            finale.clear();
                        }
                        for (int m = finale.size(); m <= l; m++) {
                            if (found.contains(right.get(m))) { // if TextView is already found
                                overlappingViews.add(right.get(m));
                                int ogColor = ((ColorDrawable) right.get(m).getBackground()).getColor();
                                overlappingColors.add(ogColor);
                                right.get(m).setBackgroundColor(ColorUtils.blendARGB(ogColor, color, 0.5F));
                            } else //set to new color
                                right.get(m).setBackgroundColor(color);
                            finale.add(right.get(m));
                            isRight = true;
                        }
                    }
                }
                for (int l = 1; l < down.size(); l++) { // iterate through all TextViews below the DOWNed TextView (same shit as right but for down)
                    down.get(l).getLocationOnScreen(temp);
                    rect = new Rect(temp[0], temp[1], temp[0] + down.get(l).getWidth(), temp[1] + down.get(l).getHeight());
                    if (rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                        if (isRight) {
                            for (TextView textView : finale) {
                                if (found.contains(textView))
                                    textView.setBackgroundColor(overlappingColors.get(overlappingViews.indexOf(textView)));
                                else
                                    textView.setBackgroundColor(Color.TRANSPARENT);
                            }
                            finale.clear();
                        }
                        for (int m = finale.size(); m <= l; m++) {
                            if (found.contains(down.get(m))) {
                                overlappingViews.add(down.get(m));
                                int ogColor = ((ColorDrawable) down.get(m).getBackground()).getColor();
                                overlappingColors.add(ogColor);
                                down.get(m).setBackgroundColor(ColorUtils.blendARGB(ogColor, color, 0.5F));
                            } else
                                down.get(m).setBackgroundColor(color);
                            finale.add(down.get(m));
                            isRight = false;
                        }
                    }
                }

                for (int l = 0; l < finale.size() - 1; l++) { // for deselecting TextViews
                    finale.get(l).getLocationOnScreen(temp);
                    rect = new Rect(temp[0], temp[1], temp[0] + finale.get(l).getWidth(), temp[1] + finale.get(l).getHeight());
                    if (rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                        int temp2 = finale.size();
                        for (int m = l + 1; m < temp2; m++)
                            if (found.contains(finale.get(m)))
                                finale.get(m).setBackgroundColor(overlappingColors.get(overlappingViews.indexOf(finale.get(m))));
                            else
                                finale.get(m).setBackgroundColor(Color.TRANSPARENT);
                        for (int m = l + 1; m < temp2; m++)
                            finale.remove(finale.get(l + 1));
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                String word = "";
                for (TextView textView : finale) //make word
                    word += textView.getText();
                if (!words.contains(word) || word.isEmpty()) //if word not found
                    for (TextView textView : finale) {
                        if (found.contains(textView))
                            textView.setBackgroundColor(overlappingColors.get(overlappingViews.indexOf(textView)));
                        else
                            textView.setBackgroundColor(Color.TRANSPARENT);
                    }
                else {
                    found.addAll(finale);
                    wordsFound.add(word);
                    findViewById(R.id.submit).setEnabled(true);
                }
                overlappingViews.clear();
                overlappingColors.clear();
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