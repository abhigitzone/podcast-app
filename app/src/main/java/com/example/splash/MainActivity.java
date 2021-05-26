package com.example.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.splash.Fragments.CategoryFragment;
import com.example.splash.Fragments.FragmentAbout;
import com.example.splash.Fragments.HomeFragment;
import com.example.splash.Fragments.StudioFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar; //For App Bar
    private DrawerLayout drawer; //For navigation Drawer
    RelativeLayout relativeLayout;
    Context context = this;
    Snackbar snackbar;  //Used for displaying network status messages.
    //Firebase Instances..
    FirebaseFirestore db = FirebaseFirestore.getInstance(); //entry point in FireStore database.
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String userId = firebaseUser.getUid();
    DocumentReference documentReference = db.collection("Edit User Details").document(userId);
    DocumentReference dcRef = db.collection("users").document(userId);
    DocumentReference documentCoverRef = db.collection("Edit Cover Image").document(userId);
    public ListenerRegistration listenerRegistration, lRegister, coverImgListener;
    BottomSheetDialogFragment bottomSheetDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar..
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.app_name));

        relativeLayout = findViewById(R.id.activity_main);

        //Change statusBar text color..
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //Bottom navigation bar.
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation);
        //For default fragment view..
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        //Side Navigation stuffs..
        drawer = findViewById(R.id.drawer_container);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.Navigation_drawer_open, R.string.Navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //You can also change navigation hamburger icon color from here..
        /*   toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorAccent));*/

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this); //Use to handle touch event of menu item.

    }

    //Handling Bottom navigation flow..
    private BottomNavigationView.OnNavigationItemSelectedListener navigation = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.home:
                    selectedFragment = new HomeFragment();
                    toolbar.setTitle(getString(R.string.app_name));
                    break;
                case R.id.categories:
                    //Add fragments here..
                    selectedFragment = new CategoryFragment();
                    toolbar.setTitle(getString(R.string.category));
                    break;
                case R.id.studio:
                    selectedFragment = new StudioFragment();
                    toolbar.setTitle(getString(R.string.studio));
                    break;
            }
            assert selectedFragment != null;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;

        }
    };

    /*TODO:// Create fragment for every navigation menu item..*/
    //Handling navigation flow
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.explore:
                Handler handler = new Handler(Looper.getMainLooper()); //Used multi-threading for faster response.
                handler.post(() -> {
                    startActivity(new Intent(MainActivity.this, ExploreActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                });
                break;
            case R.id.account:
                Handler handlerAcc = new Handler(Looper.getMainLooper());
                handlerAcc.post(() -> {
                    startActivity(new Intent(MainActivity.this, AccountProfile.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                });
                break;
            case R.id.favourite:
                Toast.makeText(this, "We are working upon it..", Toast.LENGTH_LONG).show();
                toolbar.setTitle(getString(R.string.favourite));
                break;
            case R.id.chat:
                Handler handlerChat = new Handler(Looper.getMainLooper());
                handlerChat.post(() -> {
                    startActivity(new Intent(MainActivity.this, CommunityChat.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                });
                break;
            case R.id.share:
                Toast.makeText(this, "We are working upon it.", Toast.LENGTH_LONG).show();
                break;
            case R.id.language:
                Toast.makeText(this, "Under Development.", Toast.LENGTH_LONG).show();
                break;
            case R.id.feedback:
                Toast.makeText(this, "under construction", Toast.LENGTH_LONG).show();
                break;
            case R.id.version:
                Toast.makeText(this, "App Version : 1.0", Toast.LENGTH_LONG).show();
                break;
            case R.id.about:
                bottomSheetDialogFragment = new FragmentAbout();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), "About us");
                bottomSheetDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme); //for transparent background
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Update navigation header..
    private void updateNavigationHeader() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        CircleImageView userImg = headerView.findViewById(R.id.NavUserImg);
        ImageView userCoverImg = headerView.findViewById(R.id.NavCoverImg);
        TextView userNameTxt = headerView.findViewById(R.id.NavUserName);
        TextView userEmail = headerView.findViewById(R.id.NavEmailId);

        //Fetching profile photo and cover image into glide..
        listenerRegistration = documentReference.addSnapshotListener((documentSnapshot, error) -> {
            if (error != null) {
                Log.e("AccountProfile", "error" + error.getMessage());
                Toast.makeText(this, "Reason: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            assert documentSnapshot != null;
            if (documentSnapshot.exists()) {
                //Exist..
                String imageUrl = documentSnapshot.getString("imageUrl");
                Glide.with(this).load(imageUrl).into(userImg); //Set Profile Image
            }
        });
        //Fetching name and email id of users collection..
        lRegister = dcRef.addSnapshotListener((documentSnapshot, error) -> {
            if (error != null) {
                Toast.makeText(this, "Error while loading!!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (documentSnapshot.exists()) {
                //Exist..
                String email = documentSnapshot.getString("email");//set Email id
                String name = documentSnapshot.getString("Fname"); //set Name
                userNameTxt.setText(name);
                userEmail.setText(email);
            }
        });
        //Fetching cover image from FireStore
        //Read cover image from FireStore
        coverImgListener = documentCoverRef.addSnapshotListener((documentSnapshot, error) -> {
            if (error != null) {
                Log.e("AccountProfile", "error" + error.getMessage());
                return;
            }
            if (documentSnapshot.exists()) {
                //Exist..
                String coverUri = documentSnapshot.getString("coverUrl");
                Glide.with(this).load(coverUri).into(userCoverImg); //set Cover Image
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                // update the ui from here
                updateNavigationHeader();
                checkInternetConnection(); //Calling internet check method when app launch.
            }
        });
    }

    //Checking internet connection while launching app.
    public void checkInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            snackbar = Snackbar.make(relativeLayout, "No Internet Connection", Snackbar.LENGTH_LONG)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkInternetConnection();
                        }
                    }).setDuration(8000);
            snackbar.show();
        }
    }

    //Toolbar icon : search bar, notification
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_home, menu);
        return true;
    }

    //On option menu click..
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.notification_home: {
                Toast.makeText(this, "Working on it", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.search_home: {
                Toast.makeText(this, "Search the API", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return true;
    }

    //On Back pressed, navigation bar closes..
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
