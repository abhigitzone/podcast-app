package com.example.splash.Fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.splash.Model.ChatData;
import com.example.splash.Model.StudioData;
import com.example.splash.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class StudioFragment extends Fragment {
    private static final int PICK_VIDEO = 1;
    VideoView videoView;
    EditText videoTitle;
    EditText videoDesc;
    Button chooseVideo;
    Button publishVideo;
    ProgressBar studioProgress;
    private Uri videoUri;
    Context mContext;
    MediaController mediaController;

    //Firebase Instances..
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabaseReferences;
    StorageReference storageReference;
    StudioData studioData;
    UploadTask uploadTask;

    //FireStore references..
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String userId = firebaseUser.getUid();
    DocumentReference documentReference = db.collection("Edit User Details").document(userId);
    public ListenerRegistration listenerRegistration;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_studio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Assigning ids..
        videoView = view.findViewById(R.id.studioVideoView);
        videoTitle = view.findViewById(R.id.videoTitle);
        videoDesc = view.findViewById(R.id.videoDesc);
        chooseVideo = view.findViewById(R.id.chooseVideo);
        studioProgress = view.findViewById(R.id.studioProgress);
        publishVideo = view.findViewById(R.id.publishVideo);

        //Video player
        mediaController = new MediaController(getActivity());
        videoView.setMediaController(mediaController);
        videoView.start();

        //Firebase stuffs..
        //Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("Video");
        mDatabaseReferences = firebaseDatabase.getReference("video");
        studioData = new StudioData();

        //When click choose video button, open video chooser activity..
        chooseVideo.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_VIDEO);
        });
        //Handling Publish Button Event..
        publishVideo.setOnClickListener(v -> publishVideo());
    }

    //Video from Chooser Activity..
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_VIDEO || resultCode == RESULT_OK || data != null) {
                videoUri = data.getData();
                videoView.setVideoURI(videoUri);
            }
        } catch (Exception e) {
            Toast.makeText(mContext, "Video Pick Failed..", Toast.LENGTH_SHORT).show();
        }
    }

    //Get File Extension of video..
    private String getFileExt(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

    //Handle On Publish Video Button
    public void publishVideo() {
        String videoTtl = videoTitle.getText().toString().trim();
        String videoDes = videoDesc.getText().toString().trim();
        String search = videoTitle.getText().toString().toLowerCase();

        if (videoTtl.isEmpty() || videoDes.isEmpty()) {
            videoTitle.setError(getString(R.string.mandatory));
            videoDesc.setError(getString(R.string.mandatory));
        } else if (videoUri == null) {
            Toast.makeText(mContext, "Please select video", Toast.LENGTH_SHORT).show();
        } else {
            studioProgress.setVisibility(View.VISIBLE);
            storageReference = storageReference.child(System.currentTimeMillis() + "." + getFileExt(videoUri));
            uploadTask = storageReference.putFile(videoUri);

            Task<Uri> uriTask = uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //Calling listener register of Firestore to get current user image..
                    listenerRegistration = documentReference.addSnapshotListener((documentSnapshot, error) -> {
                        if (documentSnapshot.exists()) {
                            //Exist..
                            String imageUrl = documentSnapshot.getString("imageUrl");

                            Uri downloadUri = task.getResult();
                            studioProgress.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Video Published..!!", Toast.LENGTH_SHORT).show();

                            //Store Data in Real-time database.
                            studioData.setVideoTitle(videoTtl);
                            studioData.setVideoDesc(videoDes);
                            studioData.setVideoURL(downloadUri.toString());
                            studioData.setSearch(search);
                            studioData.setVideoProfileImg(imageUrl);

                            String key = mDatabaseReferences.push().getKey();
                            mDatabaseReferences.child(key).setValue(studioData);

                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Video Published Failed", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
