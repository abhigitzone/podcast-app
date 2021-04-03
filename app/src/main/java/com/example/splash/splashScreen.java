package com.example.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.splash.Authentication.AuthHome;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splashScreen extends AppCompatActivity {
    TextView appTitle;
    //To check user status..
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mFirebaseAuth = FirebaseAuth.getInstance();
        appTitle = findViewById(R.id.app_title);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_up);
        appTitle.startAnimation(animation);

        //To launch splash in full screen..
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int SPLASH_SCREEN_TIME_OUT = 2000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAuthStateListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();  //If the user is already Logged In, no need to sign in again.
                        if (mFirebaseUser != null && mFirebaseUser.isEmailVerified()) {
                            //Toast.makeText(getApplicationContext(), "Welcome back", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(splashScreen.this, MainActivity.class); //Start the application directly to the Dashboard.
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            startActivity(intent);
                            finish();
                        } else {
                            // User is signed out
                            Intent intent = new Intent(splashScreen.this, AuthHome.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                        }
                    }
                };
                mFirebaseAuth.addAuthStateListener(mAuthStateListener);
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFirebaseAuth != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
}