package com.example.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.splash.Exoplayer.ViewHolder;
import com.example.splash.Model.StudioData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import de.hdodenhof.circleimageview.CircleImageView;

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

        progressBar = findViewById(R.id.exploreProgress);

        //Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReferences = firebaseDatabase.getReference("video");
    }

    //Retrieving video from the realtime database... and populating in recycler view..
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<StudioData> options = new FirebaseRecyclerOptions.Builder<StudioData>()
                .setQuery(mDatabaseReferences, StudioData.class).build();

        FirebaseRecyclerAdapter<StudioData, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<StudioData, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull StudioData model) {
                holder.setVideo(getApplication(), model.getVideoTitle(), model.getVideoURL(), model.getVideoDesc(), model.getVideoProfileImg());
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                progressBar.setVisibility(View.GONE);
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.explore_video_sample, parent, false);
                return new ViewHolder(view);
            }
        };

        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    //Used for search bar
    private void firebaseSearch(String searchText) {
        String query = searchText.toLowerCase();
        Query firebaseQuery = mDatabaseReferences.orderByChild("search").startAt(query).endAt(query + "\uf8ff");

        FirebaseRecyclerOptions<StudioData> options = new FirebaseRecyclerOptions.Builder<StudioData>()
                .setQuery(firebaseQuery, StudioData.class).build();

        FirebaseRecyclerAdapter<StudioData, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<StudioData, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull StudioData model) {
                holder.setVideo(getApplication(), model.getVideoTitle(), model.getVideoURL(), model.getVideoDesc(), model.getVideoProfileImg());
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                progressBar.setVisibility(View.GONE);
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
        getMenuInflater().inflate(R.menu.option_menu_explore, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.notification) {
            Toast.makeText(this, "under development", Toast.LENGTH_SHORT).show();
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
