package com.example.splash;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

//TODO: This class is made for saving data to fireStore for respective users.
public class EditProfile extends AppCompatActivity {
    private static final String TAG = "EditProfile";
    private static final int PICK_IMAGE = 1;
    private static final int PICK_COVER_IMAGE = 2;
    Toolbar editToolbar;
    Button saveEdit;
    ProgressBar progressBar;
    CircleImageView EditProfileImage;
    ImageView EditCoverImg;
    Uri profileImageUri, coverImageUri;
    UploadTask uploadTaskImg, uploadTaskCover;

    //EditText form instances..
    EditText etDOB, etBio, etLoc, etWeb;

    //FireStore instances..
    FirebaseDatabase database;
    StorageReference storageProfileReference, storageCoverReference; //Firebase Storage
    DocumentReference documentReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //Firestore instances for reading current data..
    String userId = user.getUid();
    DocumentReference dcRefEditText = db.collection("Edit User Details").document(userId);
    DocumentReference dcRefCoverImg = db.collection("Edit Cover Image").document(userId);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Toolbar stuffs..
        editToolbar = findViewById(R.id.EditToolbar);
        setSupportActionBar(editToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Change statusBar icon color..
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

        setPreviousDetails(); //Calling this method to automatically set user details, if set previously.

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
        //On click cover Image, user can choose image.
        EditCoverImg.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_COVER_IMAGE);
        });
    }

    //Choose profile image and cover image to upload to database..
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) { //Getting profile image
                profileImageUri = data.getData(); //It contains raw image..
                Glide.with(this).load(profileImageUri).into(EditProfileImage);
            } else if (requestCode == PICK_COVER_IMAGE) { //Getting cover images from gallery..
                coverImageUri = data.getData();
                Glide.with(this).load(coverImageUri).into(EditCoverImg);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Image Pick Failed..", Toast.LENGTH_SHORT).show();
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
        if (dob.isEmpty()) {
            etDOB.setError(getString(R.string.mandatory));
        } else if (bio.isEmpty()) {
            etBio.setError(getString(R.string.mandatory));
        } else if (loc.isEmpty()) {
            etLoc.setError(getString(R.string.mandatory));
        } else if (profileImageUri == null || coverImageUri == null) {
            Toast.makeText(this, "Please Select Image", Toast.LENGTH_SHORT).show();
        } else {
            //Uploading profile Image to Firebase Storage..
            storageProfileReference = storageProfileReference.child(System.currentTimeMillis() + "." + getFileExt(profileImageUri));
            uploadTaskImg = storageProfileReference.putFile(profileImageUri);
            progressBar.setVisibility(View.VISIBLE);
            updateCoverImg(); //Calling this method to upload cover image to server.
            Task<Uri> uriTask = uploadTaskImg.continueWithTask(task -> {
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
                    user.put("imageUrl", downloadURI.toString());
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

    //This method will use to update cover image from edit profile.
    private void updateCoverImg() {
        storageCoverReference = storageCoverReference.child(System.currentTimeMillis() + "." + getFileExt(coverImageUri));
        uploadTaskCover = storageCoverReference.putFile(coverImageUri);
        Task<Uri> uriTask = uploadTaskCover.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return storageCoverReference.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadURI = task.getResult();
                String currentUserId = user.getUid();
                documentReference = db.collection("Edit Cover Image").document(currentUserId);
                Map<String, Object> user = new HashMap<>();
                user.put("coverUrl", downloadURI.toString());
                documentReference.set(user).addOnSuccessListener(aVoid ->
                        Log.d("TAG", "onSuccess: user profile is created for : " + currentUserId));
            }
        });
    }
    //This method is used to set the current details of user, if set previously and exists..
    public void setPreviousDetails(){
        dcRefEditText.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        String dob = documentSnapshot.getString("DOB");
                        String bio = documentSnapshot.getString("Bio");
                        String location = documentSnapshot.getString("Location");
                        String website = documentSnapshot.getString("Website");
                        String imageUri = documentSnapshot.getString("imageUrl");
                        etDOB.setText(dob);
                        etBio.setText(bio);
                        etLoc.setText(location);
                        etWeb.setText(website);
                        Glide.with(EditProfile.this).load(imageUri).into(EditProfileImage); //set profile Image
                        /*TODO:// Set image to Glide so that the image should be replaced after choosing new.*/
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }

        });
        //Getting current cover image of the user if exist.
        dcRefCoverImg.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        String coverUri = documentSnapshot.getString("coverUrl");
                        Glide.with(EditProfile.this).load(coverUri).into(EditCoverImg); //Set Cover Image
                    }
                }
            }
        });
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