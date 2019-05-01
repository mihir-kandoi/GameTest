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

import com.example.mihirkandoi.Rethynk.StoryLyne.Start;
import com.example.mihirkandoi.rethynk.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseUiException;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class Login extends AppCompatActivity {

    private static final int RC_SIGN_IN = 0;

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
        setContentView(R.layout.activity_login);
        findViewById(R.id.root).getForeground().setAlpha(0);

        //Make "Sign Up" link to SignUp activity
        TextView textView = findViewById(R.id.sign_up);
        SpannableString spannableString = new SpannableString(textView.getText());
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View textView) {
                startActivityForResult(new Intent(Login.this, SignUp.class), 1);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        }, 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
            span = new URLSpanNoUnderline(span.getURL());
            spannable.setSpan(span, start, end, 0);
        }
        textView.setText(spannable);

        findViewById(R.id.fb_login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(Collections.singletonList(new AuthUI.IdpConfig.FacebookBuilder().build())).build(), RC_SIGN_IN);
            }
        });

        findViewById(R.id.google_login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build())).build(), RC_SIGN_IN);
            }
        });

        //E-Mail login button
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = ((EditText) findViewById(R.id.email)).getText().toString();
                String password = ((EditText) findViewById(R.id.password)).getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toasty.error(Login.this, "Please fill both the fields and try again", Toast.LENGTH_LONG, true).show();
                    return;
                }

                findViewById(R.id.root).getForeground().setAlpha(145);
                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                findViewById(R.id.root).getForeground().setAlpha(0);
                                findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in FirestoreUI's information
                                    getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE).edit().putBoolean("IS_DATA_CLEARED", false).apply();
                                    Toasty.success(Login.this, "Login successful!", Toast.LENGTH_LONG, true).show();
                                    startActivity(new Intent(Login.this, DataCapture.class));
                                    setResult(1);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the FirestoreUI.
                                    Log.e("Email login failed", "signInWithEmail:failure", task.getException());
                                    if (task.getException() instanceof FirebaseAuthInvalidUserException)
                                        Toasty.error(Login.this, "User record not found, please sign up first", Toast.LENGTH_SHORT, true).show();
                                    else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                                        Toasty.error(Login.this, "Invalid E-Mail or password entered", Toast.LENGTH_SHORT, true).show();
                                    else if (task.getException() instanceof FirebaseNetworkException)
                                        Toasty.error(Login.this, "Can't connect to the network, please check your internet connection", Toast.LENGTH_LONG, true).show();
                                    else if (task.getException() instanceof FirebaseUiException)
                                        Toasty.error(Login.this, "Unknown error, please check if your E-Mail is already registered with us using another provider", Toast.LENGTH_LONG, true).show();
                                    else
                                        Toasty.error(Login.this, "Authentication failed, unhandled error", Toast.LENGTH_SHORT, true).show();
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


        findViewById(R.id.forgot_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((EditText) findViewById(R.id.email)).getText().toString().isEmpty()) {
                    Toasty.error(Login.this, "Please enter the E-Mail ID", Toast.LENGTH_LONG, true).show();
                    findViewById(R.id.email).requestFocus();
                    return;
                }

                findViewById(R.id.root).getForeground().setAlpha(145);
                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                FirebaseAuth.getInstance().sendPasswordResetEmail(((EditText) findViewById(R.id.email)).getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        findViewById(R.id.root).getForeground().setAlpha(0);
                        findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        if (task.isSuccessful())
                            Toasty.success(Login.this, "Please check your E-Mail for password reset instructions", Toast.LENGTH_LONG, true).show();
                        else {
                            Log.e("Forgot password failed", "forgotPassword:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidUserException)
                                Toasty.error(Login.this, "E-Mail not found, please sign up first", Toast.LENGTH_SHORT, true).show();
                            else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                                Toasty.error(Login.this, "Invalid E-Mail entered, please check and try again", Toast.LENGTH_SHORT, true).show();
                            else if (task.getException() instanceof FirebaseNetworkException)
                                Toasty.error(Login.this, "Can't connect to the network, please check your internet connection", Toast.LENGTH_LONG, true).show();
                            else
                                Toasty.error(Login.this, "Authentication failed, unhandled error", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE).edit().putBoolean("IS_DATA_CLEARED", false).apply();
                Toasty.success(Login.this, "Login successful!", Toast.LENGTH_LONG, true).show();
                startActivity(new Intent(this, DataCapture.class));
                setResult(1);
                finish();
            } else {
                // Sign in failed
                if (response != null) {
                    Log.e("Provider login failed", response.getError().getMessage(), response.getError());
                    if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK)
                        Toasty.error(this, "Can't connect to the network, please check your internet connection", Toast.LENGTH_LONG, true).show();
                    else if (response.getError().getErrorCode() == ErrorCodes.DEVELOPER_ERROR)
                        Toasty.error(this, "Unknown error, please check if your E-Mail is already registered with us using another provider", Toast.LENGTH_LONG, true).show();
                    else
                        Toasty.error(this, "Sign in failed, please try again", Toast.LENGTH_LONG, true).show();
                }
            }
        } else if (requestCode == 1 && resultCode == 1) {
            setResult(1);
            finish();
        }
    }
}
