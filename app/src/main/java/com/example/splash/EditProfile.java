package com.example.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Objects;

public class EditProfile extends AppCompatActivity {
    Toolbar editToolbar;
    Button saveEdit;
    ProgressBar progressBar;

    //TODO: This class is made for saving data to fireStore for respective users.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Toolbar stuffs..
        editToolbar = findViewById(R.id.EditToolbar);
        setSupportActionBar(editToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Change statusBar text color..
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //Instances..
        saveEdit = findViewById(R.id.saveEditBtn);
        progressBar = findViewById(R.id.saveProgress);

        //Handling save button event..
        saveEdit.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(EditProfile.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        return true;
    }
}