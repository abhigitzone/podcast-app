package com.example.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.splash.Authentication.AuthHome;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splashScreen extends AppCompatActivity {
    //To check user status..
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener; //For checking user state.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mFirebaseAuth = FirebaseAuth.getInstance();
        ImageView appIcon = findViewById(R.id.appIcon);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_up);
        appIcon.setAnimation(animation);

        //To launch splash in full screen..
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int SPLASH_SCREEN_TIME_OUT = 2000; //2000ms ~ 2 sec
        new Handler().postDelayed(() -> { //This is a thread..
            mAuthStateListener = firebaseAuth -> {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();  //If the user is already Logged In, no need to sign in again.
                if (mFirebaseUser != null && mFirebaseUser.isEmailVerified()) {
                    Intent intent = new Intent(splashScreen.this, MainActivity.class); //Start the application directly to the Dashboard.
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    startActivity(intent);
                    finish(); //finish the activity.
                } else {
                    // User is signed out or user is new to app.
                    Intent intent = new Intent(splashScreen.this, AuthHome.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
            };
            mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        }, SPLASH_SCREEN_TIME_OUT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFirebaseAuth != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener); //Deactivate user session..
        }
    }
}