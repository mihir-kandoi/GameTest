package com.example.mihirkandoi.GameTest.StoryLyne;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mihirkandoi.GameTest.Parent;
import com.example.mihirkandoi.gametest.R;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        Parent.start(this, "Story Lyne", "Perception", "The ability to recognise and interpret certain stimuli.", R.color.storyLyne, Instructions.class);
    }
}
