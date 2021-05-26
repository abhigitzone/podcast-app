package com.example.splash;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.splash.Authentication.AuthHome;
import com.example.splash.Model.ChatData;
import com.example.splash.Model.ProfileVisibleData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

//This class will read the respective data from Firebase FireStore and set it to textView and imageView respectively.
public class AccountProfile extends AppCompatActivity {
    Button logout;
    CircleImageView profileImage;
    ImageView profileCoverIMG;
    SwitchMaterial profileVisible;
    //User interface instances..
    Button accountEdit;
    ImageView backBtn;
    TextView accName, accBio, accEmail, accLoc, accWeb, accDOB;

    //Firebase Instances..
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String userId = firebaseUser.getUid();
    DocumentReference documentReference = db.collection("Edit User Details").document(userId);
    DocumentReference dcRef = db.collection("users").document(userId);
    DocumentReference documentCoverRef = db.collection("Edit Cover Image").document(userId);
    //For Profile visibility..TODO://Make activity for view Profile of other users..
    DocumentReference documentUserID = db.collection("USER ID").document(userId);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Initializing Id's..
        accName = findViewById(R.id.accountName);
        accBio = findViewById(R.id.accountBio);
        accEmail = findViewById(R.id.accountEmail);
        accLoc = findViewById(R.id.accountLocation);
        accWeb = findViewById(R.id.accountWebsite);
        accDOB = findViewById(R.id.accountDOB);
        profileImage = findViewById(R.id.profileImg);
        profileVisible = findViewById(R.id.profileVisibility);
        profileCoverIMG = findViewById(R.id.profileCoverImg);

        //Handling back pressed..
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        //Handling Edit button..
        accountEdit = findViewById(R.id.accountEdit);
        accountEdit.setOnClickListener(v -> {
            startActivity(new Intent(AccountProfile.this, EditProfile.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        //Sign out the user..
        mFirebaseAuth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            mFirebaseAuth.signOut();
            this.finishAffinity(); //Close all previous activity.
            startActivity(new Intent(AccountProfile.this, AuthHome.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show();
        });

        //Handling profile visible switch button
        profileVisibleSwitch();
    }

    //It will automatically read the data from fireStore and display it to user.
    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();
        //Read data that updated from edit profile
        documentReference.addSnapshotListener(this, (documentSnapshot, error) -> {
            if (error != null) {
                Log.e("AccountProfile", "error" + error.getMessage());
                return;
            }
            assert documentSnapshot != null;
            if (documentSnapshot.exists()) {
                //Exist..
                String dob = documentSnapshot.getString("DOB");
                String bio = documentSnapshot.getString("Bio");
                String location = documentSnapshot.getString("Location");
                String website = documentSnapshot.getString("Website");
                String imageUri = documentSnapshot.getString("imageUrl");
                accDOB.setText(dob);
                accBio.setText(bio);
                accLoc.setText(location);
                Glide.with(this).load(imageUri).into(profileImage); //set profile Image
                if (!website.isEmpty()) {
                    accWeb.setText(website);
                } else {
                    accWeb.setText("Not available");
                }
            }
        });

        //Read data from registration page..Name and Email..
        dcRef.addSnapshotListener(this, (documentSnapshot, error) -> {
            if (error != null) {
                Log.e("AccountProfile", "error" + error.getMessage());
                return;
            }
            if (documentSnapshot.exists()) {
                //Exist..
                String email = documentSnapshot.getString("email");
                String name = documentSnapshot.getString("Fname");
                accEmail.setText(email);
                accName.setText(name);
            }
        });

        //Read cover image from FireStore
        documentCoverRef.addSnapshotListener(this, (documentSnapshot, error) -> {
            if (error != null) {
                Log.e("AccountProfile", "error" + error.getMessage());
                return;
            }
            if (documentSnapshot.exists()) {
                //Exist..
                String coverUri = documentSnapshot.getString("coverUrl");
                Glide.with(this).load(coverUri).into(profileCoverIMG); //Set cover image
            }
        });
    }

    //Switch button methods..
    public void profileVisibleSwitch() {
        profileVisible.setTextColor(getResources().getColor(R.color.light_yellow));
        profileVisible.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Map<String, Object> user = new HashMap<>();
            user.put("userId", userId);
            documentUserID.set(user).addOnSuccessListener(unused -> //Write into Firestore
                    Toast.makeText(AccountProfile.this, "Profile is visible", Toast.LENGTH_SHORT).show());
        });
    }

    //Handling back button..
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}