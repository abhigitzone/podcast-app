package com.example.splash.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.splash.Adapter.CategoryRecyclerAdapter;
import com.example.splash.R;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {
    private static final int NUM_COLUMNS = 2;
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.categoryRecycler);
        CategoryRecyclerAdapter staggeredRecyclerViewAdapter =
                new CategoryRecyclerAdapter(getContext(), mNames, mImageUrls);
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

        mImageUrls.add("https://images.pexels.com/photos/841130/pexels-photo-841130.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");
        mNames.add("Workout");

        mImageUrls.add("https://images.pexels.com/photos/3512506/pexels-photo-3512506.png?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");
        mNames.add("Romantic");

        mImageUrls.add("https://images.pexels.com/photos/2405640/pexels-photo-2405640.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");
        mNames.add("Lifestyle");

        mImageUrls.add("https://images.pexels.com/photos/92083/pexels-photo-92083.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");
        mNames.add("Music");

        mImageUrls.add("https://images.pexels.com/photos/2805672/pexels-photo-2805672.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");
        mNames.add("Travel");

        mImageUrls.add("https://images.pexels.com/photos/159711/books-bookstore-book-reading-159711.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");
        mNames.add("Educational");

        mImageUrls.add("https://images.pexels.com/photos/2081166/pexels-photo-2081166.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");
        mNames.add("Technology");

        mImageUrls.add("https://images.pexels.com/photos/46798/the-ball-stadion-football-the-pitch-46798.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");
        mNames.add("Sports");

        mImageUrls.add("https://images.pexels.com/photos/273209/pexels-photo-273209.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");
        mNames.add("Business stories");

        mImageUrls.add("https://images.pexels.com/photos/3791983/pexels-photo-3791983.jpeg?auto=compress&cs=tinysrgb&dpr=3&h=750&w=1260");
        mNames.add("Comedy");

        mImageUrls.add("https://images.pexels.com/photos/3977529/pexels-photo-3977529.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");
        mNames.add("Arts &\n Entertainment");

        mImageUrls.add("https://images.pexels.com/photos/162389/lost-places-old-decay-ruin-162389.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");
        mNames.add("Scary &\nHorror");
    }

}
