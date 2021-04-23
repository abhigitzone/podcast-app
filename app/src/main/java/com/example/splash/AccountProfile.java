package com.example.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.splash.Authentication.AuthHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import de.hdodenhof.circleimageview.CircleImageView;

//TODO: This class will read the respective data from Firebase FireStore and set it to textView and imageView respectively.
public class AccountProfile extends AppCompatActivity {
    Button logout;
    CircleImageView profileImage;
    ImageView profileCoverIMG;
    FirebaseAuth mFirebaseAuth;
    //User interface instances..
    Button accountEdit;
    ImageView backBtn;
    TextView accName, accBio, accEmail, accLoc, accWeb, accDOB;

    //Firebase Instances..
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String userId = firebaseUser.getUid();
    DocumentReference documentReference = db.collection("Edit User Details").document(userId);
    DocumentReference dcRef = db.collection("users").document(userId);
    private ListenerRegistration listenerRegistration, lRegister;

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

    }

    //It will automatically read the data from fireStore and display it to user.
    @Override
    protected void onStart() {
        super.onStart();
        //Read data that updated from edit profile
        listenerRegistration = documentReference.addSnapshotListener((documentSnapshot, error) -> {
            if (error != null) {
                Log.e("AccountProfile", "error" + error.getMessage());
                Toast.makeText(this, "Reason: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            assert documentSnapshot != null;
            if (documentSnapshot.exists()) {
                //Exist..
                String dob = documentSnapshot.getString("DOB");
                String bio = documentSnapshot.getString("Bio");
                String location = documentSnapshot.getString("Location");
                String website = documentSnapshot.getString("Website");
                String imageUri = documentSnapshot.getString("url");
                accDOB.setText(dob);
                accBio.setText(bio);
                accLoc.setText(location);
                Glide.with(this).load(imageUri).into(profileCoverIMG); //set cover Image
                Glide.with(this).load(imageUri).into(profileImage); //set profile Image
                if (!website.isEmpty()) {
                    accWeb.setText(website);
                } else {
                    accWeb.setText("Not available");
                }
                //TODO: Update profileCoverIMG also here..
            }
        });

        //Read data from register page..
        lRegister = dcRef.addSnapshotListener((documentSnapshot, error) -> {
            if (error != null) {
                Toast.makeText(this, "Error while loading!!", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        listenerRegistration.remove();
        lRegister.remove();
    }

    //Handling back button..
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}