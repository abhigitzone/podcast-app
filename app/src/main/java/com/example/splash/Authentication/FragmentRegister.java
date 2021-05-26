package com.example.splash.Authentication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.splash.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FragmentRegister extends Fragment implements View.OnClickListener {
    EditText regName;
    EditText regEmail;
    EditText regPass;
    EditText regConfirmPass;
    Button register;
    CheckBox privacyCheck;
    ProgressBar regProgressBar;

    //Firebase Auth instances..
    FirebaseAuth mFirebaseAuth;

    //Firebase FireStore instances..
    public FirebaseFirestore fStore;
    public String userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Assigning Ids...
        regName = view.findViewById(R.id.regName);
        regEmail = view.findViewById(R.id.regEmail);
        regPass = view.findViewById(R.id.regPass);
        regConfirmPass = view.findViewById(R.id.regConfirmPass);
        register = view.findViewById(R.id.register);
        privacyCheck = view.findViewById(R.id.privacy);
        regProgressBar = view.findViewById(R.id.regProgressBar);

        //Firebase init..
        mFirebaseAuth = FirebaseAuth.getInstance();
        //FireStore instances..
        fStore = FirebaseFirestore.getInstance();

        //Clicking register button..
        register.setOnClickListener(this);

    }

    //Handling register button...
    @Override
    public void onClick(View v) {
        final String nameReg = regName.getText().toString().trim();
        final String emailReg = regEmail.getText().toString().trim();
        String passReg = regPass.getText().toString().trim();
        String confirmPassReg = regConfirmPass.getText().toString().trim();

        regProgressBar.setVisibility(View.VISIBLE);

        if (nameReg.isEmpty() || emailReg.isEmpty()) {
            regName.setError(getString(R.string.mandatory));
            regEmail.setError(getString(R.string.mandatory));
            regProgressBar.setVisibility(View.INVISIBLE);
        } else if (passReg.isEmpty() || confirmPassReg.isEmpty()) {
            regPass.setError(getString(R.string.mandatory));
            regConfirmPass.setError(getString(R.string.mandatory));
            regProgressBar.setVisibility(View.INVISIBLE);
        } else if (!passReg.equals(confirmPassReg)) {
            regPass.setError(getString(R.string.passMisMatched));
            regPass.setText("");
            regConfirmPass.setText("");
            regProgressBar.setVisibility(View.INVISIBLE);
        } else if (!privacyCheck.isChecked()) {
            privacyCheck.setError(getString(R.string.privacyErr));
            regProgressBar.setVisibility(View.INVISIBLE);
        } else {
            mFirebaseAuth.createUserWithEmailAndPassword(emailReg, passReg).addOnCompleteListener(getActivity(), task -> {
                if (task.isSuccessful()) {
                    mFirebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {
                        userID = mFirebaseAuth.getCurrentUser().getUid();  //getting the user id from authentication.
                        DocumentReference documentReference = fStore.collection("users").document(userID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("Fname", nameReg);
                        user.put("email", emailReg); //Add more section as you want..
                        documentReference.set(user).addOnSuccessListener(aVoid ->
                                Log.d("TAG", "onSuccess: user profile is created for : " + userID));
                        Toast.makeText(getContext(), R.string.emailLinkVerr, Toast.LENGTH_SHORT).show();
                        regProgressBar.setVisibility(View.INVISIBLE);
                    });
                } else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
