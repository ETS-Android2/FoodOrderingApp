package com.devx.fooddelivery.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.devx.fooddelivery.Model.User;
import com.devx.fooddelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    View view;
    Context context;
    TextView name, email, address;
    ProgressBar progressBar;
    LinearLayout layout;

    FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_profile_fragment, container, false);
        context = view.getContext();

        name = view.findViewById(R.id.profile_name);
        email = view.findViewById(R.id.profile_email);
        address = view.findViewById(R.id.profile_address);
        progressBar = view.findViewById(R.id.progress);
        layout = view.findViewById(R.id.details);

        firestore = FirebaseFirestore.getInstance();
        progressBar.setVisibility(View.VISIBLE);

        DocumentReference docRef = firestore.collection("users").document(FirebaseAuth.getInstance().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    User user = snapshot.toObject(User.class);
                    name.setText(user.getName());
                    email.setText(user.getEmail());
                    address.setText(user.getAddress());
                    progressBar.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(context, ""+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}