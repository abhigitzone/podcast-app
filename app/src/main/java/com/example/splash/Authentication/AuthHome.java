package com.example.splash.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.splash.MainActivity;
import com.example.splash.R;
import com.example.splash.splashScreen;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class AuthHome extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    //Image slider ..
    ImageSlider imageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_home);

        tabLayout = findViewById(R.id.tabLayout_id);
        viewPager = findViewById(R.id.viewpager_id);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        //Adding Fragment here....
        adapter.AddFragment(new FragmentLogin(), getString(R.string.login));
        adapter.AddFragment(new FragmentRegister(), getString(R.string.register));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        //ImageSlider Stuff's..
        imageSlider = findViewById(R.id.imageSlider);
        List<SlideModel> slideModels = new ArrayList<>();
        //Add n number of tiles you want in slideshow..
        slideModels.add(new SlideModel("https://marketplace.canva.com/EADao2u6anU/1/0/800w/canva-navy-blue-motivation-desktop-wallpaper-59hOevk9cqY.jpg"));
        slideModels.add(new SlideModel("https://images.pexels.com/photos/1761362/pexels-photo-1761362.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"));
        slideModels.add(new SlideModel("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80"));
        slideModels.add(new SlideModel("https://images.unsplash.com/photo-1487537023671-8dce1a785863?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80"));
        slideModels.add(new SlideModel("https://images.pexels.com/photos/761963/pexels-photo-761963.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"));
        slideModels.add(new SlideModel("https://images.pexels.com/photos/1626481/pexels-photo-1626481.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"));
        slideModels.add(new SlideModel("https://current.org/wp-content/uploads/2018/07/podcasts-itunes-top-200.png"));
        slideModels.add(new SlideModel("https://images.pexels.com/photos/1742370/pexels-photo-1742370.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"));

        imageSlider.setImageList(slideModels, true);

    }
}