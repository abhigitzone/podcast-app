package com.example.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.splash.Authentication.AuthHome;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class AccountProfile extends AppCompatActivity {
    Button logout;
    FirebaseAuth mFirebaseAuth;

    //Instances..
    Button accountEdit;
    ImageView backBtn;

    //TODO: This class will read the respective data from Firebase FireStore and set it to textView and imageView respectively.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        accountEdit = findViewById(R.id.accountEdit);
        accountEdit.setOnClickListener(v -> {
            startActivity(new Intent(AccountProfile.this, EditProfile.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        mFirebaseAuth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            mFirebaseAuth.signOut();
            this.finishAffinity(); //Close all previous activity.
            startActivity(new Intent(AccountProfile.this, AuthHome.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}