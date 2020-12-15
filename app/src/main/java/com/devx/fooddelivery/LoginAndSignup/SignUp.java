package com.devx.fooddelivery.LoginAndSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.devx.fooddelivery.MainActivity;
import com.devx.fooddelivery.Model.User;
import com.devx.fooddelivery.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class SignUp extends AppCompatActivity {

    EditText name, number, password, address, email;
    TextView redirectToLogin;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.rname);
        number = findViewById(R.id.rnum);
        password = findViewById(R.id.rpassword);
        email = findViewById(R.id.remail);
        address = findViewById(R.id.raddress);
        redirectToLogin = findViewById(R.id.toLogin);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        redirectToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void SignUp(View view) {
        String Name = name.getText().toString().trim();
        String Number = number.getText().toString().trim();
        String Password = password.getText().toString();
        String Email = email.getText().toString().trim();
        String Address = address.getText().toString().trim();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(Email, Password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        User user = new User(firebaseAuth.getCurrentUser().getUid(), Name, Number, Email, Address);
                        firestore.collection("users").document(user.getId()).set(user);
                        updateUI();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}