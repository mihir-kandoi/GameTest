package com.example.mihirkandoi.Rethynk.SonycSound;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.mihirkandoi.Rethynk.Parent;
import com.example.mihirkandoi.rethynk.R;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Parent.start(this, "Sonyc Sound", "CONNOTATION", "Emotional association called to mind by sound.", R.color.sonycSound, Instructions.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
