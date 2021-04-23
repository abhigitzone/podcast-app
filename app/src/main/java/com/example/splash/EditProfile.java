package com.example.splash;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

//TODO: This class is made for saving data to fireStore for respective users.
public class EditProfile extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private static final int PICK_COVER_IMAGE = 2;
    Toolbar editToolbar;
    Button saveEdit;
    ProgressBar progressBar;
    CircleImageView EditProfileImage;
    ImageView EditCoverImg;
    Uri profileImageUri, coverImageUri;
    UploadTask uploadTask;

    //EditText form instances..
    EditText etDOB, etBio, etLoc, etWeb;

    //FireStore instances..
    FirebaseDatabase database;
    StorageReference storageProfileReference, storageCoverReference;
    DocumentReference documentReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
        EditProfileImage = findViewById(R.id.EditProfileImg);
        EditCoverImg = findViewById(R.id.chooseCoverImg);
        etDOB = findViewById(R.id.EditDOB);
        etBio = findViewById(R.id.EditBio);
        etLoc = findViewById(R.id.EditLocation);
        etWeb = findViewById(R.id.EditWebsite);

        //Firebase Instances..
        database = FirebaseDatabase.getInstance();
        storageProfileReference = FirebaseStorage.getInstance().getReference("Profile Image");
        storageCoverReference = FirebaseStorage.getInstance().getReference("Cover Image");

        //Handling save button event..
        saveEdit.setOnClickListener(v -> {
            updateProfile();
        });

        //On click profile Image, user can choose image.
        EditProfileImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE);
        });
        //TODO: Enable it and implement it for cover image.
        //On click cover Image, user can choose image.
        EditCoverImg.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_COVER_IMAGE);
        });
    }

    //TODO: Modify it to handle for two different images..
    //Choose profile image to upload to database..
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_IMAGE || resultCode == RESULT_OK || data != null) {
                profileImageUri = data.getData();
                Glide.with(this).load(profileImageUri).into(EditProfileImage);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Image Pick Failed..", Toast.LENGTH_SHORT).show();
        } //Getting cover images from gallery..
        try {
            if (requestCode == PICK_COVER_IMAGE || resultCode == RESULT_OK) {
                coverImageUri = data.getData();
                Glide.with(this).load(coverImageUri).into(EditCoverImg);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Image Pick Failed", Toast.LENGTH_SHORT).show();
        }
    }

    //Get File Extension of images..
    private String getFileExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

    //Call this method to update user profile instantly..
    private void updateProfile() {
        String dob = etDOB.getText().toString();
        String bio = etBio.getText().toString();
        String loc = etLoc.getText().toString();
        String web = etWeb.getText().toString();
        //TODO://Update it with cover images too..
        if (dob.isEmpty()) {
            etDOB.setError(getString(R.string.mandatory));
        } else if (bio.isEmpty()) {
            etBio.setError(getString(R.string.mandatory));
        } else if (loc.isEmpty()) {
            etLoc.setError(getString(R.string.mandatory));
        } else if (profileImageUri == null) {
            Toast.makeText(this, "Please Select Image", Toast.LENGTH_SHORT).show();
        } else {
            //Uploading profile Image to Firebase Storage..
            storageProfileReference = storageProfileReference.child(System.currentTimeMillis() + "." + getFileExt(profileImageUri));
            uploadTask = storageProfileReference.putFile(profileImageUri);
            //uploading cover image to firebase storage..
            storageCoverReference = storageCoverReference.child(System.currentTimeMillis() + "." + getFileExt(coverImageUri));
            uploadTask = storageCoverReference.putFile(coverImageUri);
            progressBar.setVisibility(View.VISIBLE);
            Task<Uri> uriTask = uploadTask.continueWithTask((Continuation<UploadTask.TaskSnapshot, Task<Uri>>) task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageProfileReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadURI = task.getResult();
                    String currentUserId = user.getUid();
                    documentReference = db.collection("Edit User Details").document(currentUserId);
                    Map<String, Object> user = new HashMap<>();
                    user.put("url", downloadURI.toString());
                    user.put("DOB", dob);
                    user.put("Bio", bio);
                    user.put("Location", loc);
                    user.put("Website", web);
                    documentReference.set(user).addOnSuccessListener(aVoid ->
                            Log.d("TAG", "onSuccess: user profile is created for : " + currentUserId));
                    Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);

                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}