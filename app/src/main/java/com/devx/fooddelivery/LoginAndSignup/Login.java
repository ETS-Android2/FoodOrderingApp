package com.devx.fooddelivery.LoginAndSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.devx.fooddelivery.MainActivity;
import com.devx.fooddelivery.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText email, password;
    TextView redirect;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        redirect = findViewById(R.id.redirect);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            updateUI();
        }

        redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               redirect();
            }
        });
    }

    public void Login(View view) {
        if(firebaseAuth.getCurrentUser() != null){
            updateUI();
        }
        else{
            String AuthEmail = email.getText().toString().trim();
            String AuthPassword = password.getText().toString().trim();

            if(!AuthEmail.equals("") && (!AuthPassword.equals(""))){
                firebaseAuth.signInWithEmailAndPassword(AuthEmail, AuthPassword)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            updateUI();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Login.this, "Invalid Credentials"+e.getMessage()+e.getCause() , Toast.LENGTH_SHORT).show();
                        }
                    });
            }
            else{
                Toast.makeText(this, "Fill the Details", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void updateUI() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void redirect(){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {

    }

    public void forgotPassword(View view) {
        Toast.makeText(this, "Contact Administrator for password change", Toast.LENGTH_SHORT).show();
    }
}