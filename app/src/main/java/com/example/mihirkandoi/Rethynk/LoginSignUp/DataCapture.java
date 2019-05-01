package com.example.mihirkandoi.Rethynk.LoginSignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mihirkandoi.Rethynk.StoryLyne.Start;
import com.example.mihirkandoi.rethynk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class DataCapture extends AppCompatActivity {

    EditText date;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_capture);

        date = findViewById(R.id.birthdate);
        calendar = Calendar.getInstance();
        final ConstraintLayout constraintLayout = findViewById(R.id.root);
        constraintLayout.getForeground().setAlpha(0);
        final RadioGroup radioGroup = findViewById(R.id.radioGroup);
        final Spinner spinner = findViewById(R.id.spinner);
        date.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                date.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ViewGroup.LayoutParams radioGroupLayoutParams = radioGroup.getLayoutParams();
                radioGroupLayoutParams.height = date.getMeasuredHeight();
                radioGroup.setLayoutParams(radioGroupLayoutParams);
                //constraintLayout.updateViewLayout(radioGroup, radioGroupLayoutParams);
                ViewGroup.LayoutParams spinnerLayoutParams = spinner.getLayoutParams();
                spinnerLayoutParams.height = date.getMeasuredHeight();
                spinner.setLayoutParams(spinnerLayoutParams);
                //constraintLayout.updateViewLayout(spinner, spinnerLayoutParams);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(DataCapture.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "dd/MM/yyyy";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
                        date.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, new ArrayList<>(Collections.singletonList("Select an item"))) {

            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                textView.setTextColor(position == 0 ? Color.GRAY : Color.BLACK);
                return textView;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);

        findViewById(R.id.skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DataCapture.this, Start.class));
                finish();
            }
        });

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText profession = findViewById(R.id.profession);
                if (radioGroup.getCheckedRadioButtonId() == -1 || date.getText().toString().isEmpty() || profession.getText().toString().isEmpty()) {
                    Toasty.error(DataCapture.this, "Please fill all fields and try again", Toast.LENGTH_LONG, true).show();
                    return;
                }

                findViewById(R.id.root).getForeground().setAlpha(145);
                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                RadioButton selectRadioButton = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                FirebaseFirestore.getInstance().document("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).set(new FirestoreUI(selectRadioButton.getText().toString(), profession.getText().toString(), calendar.getTime()), SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        findViewById(R.id.root).getForeground().setAlpha(0);
                        findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        if (task.isSuccessful()) {
                            Toasty.success(DataCapture.this, "Thank you for the information", Toast.LENGTH_LONG, true).show();
                            startActivity(new Intent(DataCapture.this, Start.class));
                            finish();
                        } else {
                            Log.e("Firestore error", "DataCapture:failure", task.getException());
                            if (task.getException() instanceof FirebaseNetworkException)
                                Toasty.error(DataCapture.this, "Can't connect to the network, please check your internet connection", Toast.LENGTH_LONG, true).show();
                            else
                                Toasty.error(DataCapture.this, "Authentication failed, unhandled error", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
            }
        });
    }
}

class FirestoreUI {

    private String gender, profession;
    private Date birthdate;

    FirestoreUI() {
    }

    FirestoreUI(String gender, String profession, Date birthdate) {
        this.gender = gender;
        this.profession = profession;
        this.birthdate = birthdate;
    }

    String getGender() {
        return gender;
    }

    void setGender(String gender) {
        this.gender = gender;
    }

    String getProfession() {
        return profession;
    }

    void setProfession(String profession) {
        this.profession = profession;
    }

    Date getBirthdate() {
        return birthdate;
    }

    void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }
}