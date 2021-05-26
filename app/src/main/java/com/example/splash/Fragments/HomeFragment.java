package com.example.splash.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.splash.Adapter.HomeRecyclerAdapter;
import com.example.splash.Authentication.AuthHome;
import com.example.splash.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private static final int NUM_COLUMNS = 2;
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.homeRecycler);
        HomeRecyclerAdapter staggeredRecyclerViewAdapter =
                new HomeRecyclerAdapter(getContext(), mNames, mImageUrls);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(staggeredRecyclerViewAdapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImageBitmap();
    }

    private void initImageBitmap() {

        mImageUrls.add("https://api.time.com/wp-content/uploads/2019/12/best_podcasts_1619.jpg?w=800&quality=85");
        mNames.add("1619");

        mImageUrls.add("https://wi-images.condecdn.net/image/QKg5d1aZlzW/crop/600/f/script-apart.gif");
        mNames.add("Script aparts");

        mImageUrls.add("https://wi-images.condecdn.net/image/ok97VNKObJZ/crop/900/f/https___contentproductioncdnart19com_images_9d_ab_7e_ff_9dab7eff-1931-4f98-b4d0-50d7ab3da2d8_6352c0552420a9cbfdd9587b1280209a9f32d52459a7909b0b4358fd0f82a627a3312f9263fc5c89da0d4077911ae0b7ffbf06487e43e877f867ac1f9dab5a05.jpg");
        mNames.add("Dead Eyes");

        mImageUrls.add("https://wi-images.condecdn.net/image/8geXmVlwjB1/crop/900/f/screenshot.jpg");
        mNames.add("UnEarthen");

        mImageUrls.add("https://wi-images.condecdn.net/image/2Yy7BRYDArZ/crop/600/f/static1squarespace.jpg");
        mNames.add("Always Takes Notes");

        mImageUrls.add("https://api.time.com/wp-content/uploads/2019/02/the-argument-best-podcasts-2019.jpg?w=800&quality=85");
        mNames.add("The arguments");

        mImageUrls.add("https://wi-images.condecdn.net/image/R588WnvOY5e/crop/900/f/keen-on-andrew-keen-bsytqzwwb8_-dzacet-noya1400x1400.jpg");
        mNames.add("Keen On");

        mImageUrls.add("https://api.time.com/wp-content/uploads/2019/02/bear-brook-best-podcasts-2019.jpg?w=800&quality=85");
        mNames.add("Bear brook");

        mImageUrls.add("https://api.time.com/wp-content/uploads/2019/02/believed-best-podcasts-2019.jpg?w=800&quality=85");
        mNames.add("Believed");

        mImageUrls.add("https://api.time.com/wp-content/uploads/2019/02/american-fiasco-best-podcasts-2019.jpg?w=800&quality=85");
        mNames.add("American fiasco");

    }

}
