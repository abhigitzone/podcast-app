package com.example.splash.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.splash.Authentication.AuthHome;
import com.example.splash.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class AccountFragment extends Fragment {
    Button logout;
    FirebaseAuth mFirebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAuth.signOut();
                Objects.requireNonNull(getActivity()).finishAffinity();//finish all the activities..
                startActivity(new Intent(getActivity(), AuthHome.class));
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                Toast.makeText(getContext(), "Logout successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
