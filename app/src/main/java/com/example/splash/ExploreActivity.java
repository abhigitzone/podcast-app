package com.example.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.splash.Exoplayer.ViewHolder;
import com.example.splash.Model.ExploreData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ExploreActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabaseReferences;
    RecyclerView recyclerView;
    Toolbar toolbar;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        recyclerView = findViewById(R.id.recyclerExplore);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Toolbar
        toolbar = findViewById(R.id.exploreToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Change statusBar text color..
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReferences = firebaseDatabase.getReference("Video");

    }

    //Retrieving video from the realtime database... and populating in recycler view..
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<ExploreData> options = new FirebaseRecyclerOptions.Builder<ExploreData>()
                .setQuery(mDatabaseReferences, ExploreData.class).build();

        FirebaseRecyclerAdapter<ExploreData, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ExploreData, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ExploreData model) {
                holder.setVideo(getApplication(), model.getTitle(), model.getUrl(), model.getAuthor());
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //progressBar.setVisibility(View.GONE);
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.explore_video_sample, parent, false);
                return new ViewHolder(view);
            }
        };

        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    //Handling option menu..
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.notification) {
            Toast.makeText(this, "under development", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Working on it", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    //TODO:// Have to implement onBackPressed to stop playing video from exoplayer

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
