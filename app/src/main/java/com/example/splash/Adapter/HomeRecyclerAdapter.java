package com.example.splash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.splash.R;

import java.util.ArrayList;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.MyViewHolder> {

    public Context mContext;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mUrls = new ArrayList<>();

    public HomeRecyclerAdapter(Context mContext, ArrayList<String> mNames, ArrayList<String> mUrls) {
        this.mContext = mContext;
        this.mNames = mNames;
        this.mUrls = mUrls;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.simple_home_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        RequestOptions requestOptions = new RequestOptions().placeholder(R.color.colorWhite);
        Glide.with(mContext).load(mUrls.get(position)).apply(requestOptions).into(holder.podImg);

        holder.podTitle.setText(mNames.get(position));
        holder.podImg.setOnClickListener(v -> Toast.makeText(mContext, mNames.get(position), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return mUrls.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView podTitle;
        public ImageView podImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            podTitle = itemView.findViewById(R.id.podTitle);
            podImg = itemView.findViewById(R.id.podImg);
        }
    }
}
