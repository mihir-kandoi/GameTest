package com.example.mihirkandoi.Rethynk.LoginSignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.mihirkandoi.rethynk.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class SignUp extends AppCompatActivity {

    private static final int RC_SIGN_UP = 0;

    class URLSpanNoUnderline extends URLSpan {
        URLSpanNoUnderline(String url) {
            super(url);
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findViewById(R.id.root).getForeground().setAlpha(0);

        //Make "Login" link to Login activity
        TextView textView = findViewById(R.id.login);
        SpannableString spannableString = new SpannableString(textView.getText());
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View textView) {
                finish();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        }, 25, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString);

        textView = findViewById(R.id.agreements);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        Linkify.addLinks(textView, Pattern.compile("(Terms & Conditions|Privacy Policy)"), null, null, new Linkify.TransformFilter() {
            public final String transformUrl(final Matcher match, String url) {
                return "about:blank";
            }
        });
        Spannable spannable = new SpannableString(textView.getText());
        URLSpan[] spans = spannable.getSpans(0, spannable.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = spannable.getSpanStart(span);
            int end = spannable.getSpanEnd(span);
            spannable.removeSpan(span);
            span = new SignUp.URLSpanNoUnderline(span.getURL());
            spannable.setSpan(span, start, end, 0);
        }
        textView.setText(spannable);

        findViewById(R.id.fb_sign_up_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(Collections.singletonList(new AuthUI.IdpConfig.FacebookBuilder().build())).build(), RC_SIGN_UP);
            }
        });

        findViewById(R.id.google_sign_up_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build())).build(), RC_SIGN_UP);
            }
        });

        findViewById(R.id.sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String first_name = ((EditText) findViewById(R.id.first_name)).getText().toString();
                final String last_name = ((EditText) findViewById(R.id.last_name)).getText().toString();
                final String phone_number = ((EditText) findViewById(R.id.phone_number)).getText().toString();
                String email = ((EditText) findViewById(R.id.email)).getText().toString();
                String password = ((EditText) findViewById(R.id.password)).getText().toString();

                if (first_name.isEmpty() || last_name.isEmpty() || phone_number.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toasty.error(SignUp.this, "Please fill all fields and try again", Toast.LENGTH_LONG, true).show();
                    return;
                }

                findViewById(R.id.root).getForeground().setAlpha(145);
                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign up success, update UI with the signed-up FirestoreUI's information
                                    FirebaseFirestore.getInstance().document("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).set(new User(first_name, last_name, phone_number)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            findViewById(R.id.root).getForeground().setAlpha(0);
                                            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                            if (task.isSuccessful()) {
                                                Toasty.success(SignUp.this, "Sign up successful! Please log in now", Toast.LENGTH_LONG, true).show();
                                                finish();
                                            } else {
                                                Log.e("Firestore error", "signInWithEmail:failure", task.getException());
                                                if (task.getException() instanceof FirebaseNetworkException)
                                                    Toasty.error(SignUp.this, "Can't connect to the network, please check your internet connection", Toast.LENGTH_LONG, true).show();
                                                else
                                                    Toasty.error(SignUp.this, "Authentication failed, unhandled error", Toast.LENGTH_SHORT, true).show();
                                            }
                                        }
                                    });
                                } else {
                                    // If sign up fails, display a message to the FirestoreUI.

                                    findViewById(R.id.root).getForeground().setAlpha(0);
                                    findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                    Log.e("Email sign up failed", "signInWithEmail:failure", task.getException());
                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                                        Toasty.error(SignUp.this, "Invalid E-Mail format", Toast.LENGTH_SHORT, true).show();
                                    else if (task.getException() instanceof FirebaseNetworkException)
                                        Toasty.error(SignUp.this, "Can't connect to the network, please check your internet connection", Toast.LENGTH_LONG, true).show();
                                    else if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                        Toasty.error(SignUp.this, "The email address is already in use by another account", Toast.LENGTH_LONG, true).show();
                                    else
                                        Toasty.error(SignUp.this, "Authentication failed, unhandled error", Toast.LENGTH_SHORT, true).show();
                                }
                            }
                        });
            }
        });

        //Show or hide password
        final EditText password = findViewById(R.id.password);
        ((ToggleButton) findViewById(R.id.pass_visible)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                else
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_UP is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_UP) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed up
            if (resultCode == RESULT_OK) {
                getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE).edit().putBoolean("IS_DATA_CLEARED", false).apply();
                Toasty.success(SignUp.this, "Login successful!", Toast.LENGTH_LONG, true).show();
                startActivity(new Intent(this, DataCapture.class));
                setResult(1);
                finish();
            } else {
                // Sign up failed
                if (response != null) {
                    Log.e("Provider sign up failed", response.getError().getMessage(), response.getError());
                    if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK)
                        Toasty.error(this, "Can't connect to the network, please check your internet connection", Toast.LENGTH_LONG, true).show();
                    else if (response.getError().getErrorCode() == ErrorCodes.DEVELOPER_ERROR)
                        Toasty.error(this, "Unknown error, please check if your E-Mail is already registered with us using another provider", Toast.LENGTH_LONG, true).show();
                    else {
                        Toasty.error(this, "Sign in failed, please try again", Toast.LENGTH_LONG, true).show();
                    }
                }
            }
        }
    }
}

class User {

    User() {
    }

    private String fName, lName, phoneNumber;

    String getfName() {
        return fName;
    }

    void setfName(String fName) {
        this.fName = fName;
    }

    String getlName() {
        return lName;
    }

    void setlName(String lName) {
        this.lName = lName;
    }

    String getPhoneNumber() {
        return phoneNumber;
    }

    void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    User(String fName, String lName, String phoneNumber) {
        this.fName = fName;
        this.lName = lName;
        this.phoneNumber = phoneNumber;
    }
}