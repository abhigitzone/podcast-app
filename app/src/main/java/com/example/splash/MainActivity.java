package com.example.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.splash.Fragments.AccountFragment;
import com.example.splash.Fragments.ExploreFragment;
import com.example.splash.Fragments.HomeFragment;
import com.example.splash.Fragments.LibraryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    View bottomMusic;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar..
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.app_name));

        //Assigning Id for bottom music controller and opening intent..
        bottomMusic = findViewById(R.id.bottom_music);
        bottomMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MusicController.class));
            }
        });

        //Bottom navigation bar.
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        //Navigation stuffs..
        drawer = findViewById(R.id.drawer_container);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.Navigation_drawer_open, R.string.Navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //You can also change navigation hamburger icon color from here..
        /*   toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorAccent));*/

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
                case R.id.studio:
                    selectedFragment = new LibraryFragment();
                    toolbar.setTitle(getString(R.string.studio));
                    break;
                case R.id.account:
                    selectedFragment = new AccountFragment();
                    toolbar.setTitle(getString(R.string.account));
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ExploreFragment()).commit();
                toolbar.setTitle(getString(R.string.explore));
                break;
            case R.id.categories:
                //Add fragments here..
                Toast.makeText(this, "Have to build fragment for it..", Toast.LENGTH_SHORT).show();
                toolbar.setTitle(getString(R.string.category));
                break;
            case R.id.favourite:
                Toast.makeText(this, "We are working upon it..", Toast.LENGTH_LONG).show();
                toolbar.setTitle(getString(R.string.favourite));
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
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }
    //On option menu click..


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.notification: {
                Toast.makeText(this, "Working on it", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.search: {
                Toast.makeText(this, "search will open new fragment", Toast.LENGTH_SHORT).show();
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
