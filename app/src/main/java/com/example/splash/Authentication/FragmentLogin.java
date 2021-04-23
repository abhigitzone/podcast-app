package com.example.splash.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.splash.MainActivity;
import com.example.splash.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class FragmentLogin extends Fragment {
    EditText loginEmail;
    EditText loginPass;
    Button login;
    TextView forgetPass;
    ProgressBar progressBar;

    //Firebase Authentication...
    FirebaseAuth mFirebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Assigning Id's
        login = view.findViewById(R.id.login);
        loginEmail = view.findViewById(R.id.loginEmail);
        loginPass = view.findViewById(R.id.loginPass);
        forgetPass = view.findViewById(R.id.forgetPass);
        progressBar = view.findViewById(R.id.progressBar);

        mFirebaseAuth = FirebaseAuth.getInstance();

        //Handling login stuff..
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailLogin = loginEmail.getText().toString().trim();
                String passLogin = loginPass.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);
                if (emailLogin.isEmpty()) {
                    loginEmail.setError(getString(R.string.mandatory));
                    progressBar.setVisibility(View.INVISIBLE);
                } else if (passLogin.isEmpty()) {
                    loginPass.setError(getString(R.string.mandatory));
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    mFirebaseAuth.signInWithEmailAndPassword(emailLogin, passLogin).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).isEmailVerified()) {
                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                    Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    getActivity().finish();
                                }
                            } else {
                                Toast.makeText(getContext(), "Error while login", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
    }
}
