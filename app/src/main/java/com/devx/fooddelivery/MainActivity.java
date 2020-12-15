package com.devx.fooddelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.devx.fooddelivery.Fragments.HistoryFragment;
import com.devx.fooddelivery.Fragments.OrderFragment;
import com.devx.fooddelivery.Fragments.ProfileFragment;
import com.devx.fooddelivery.LoginAndSignup.Login;
import com.devx.fooddelivery.Model.FoodItem;
import com.devx.fooddelivery.VIewModel.CartViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;


/**
 * This activity has Bottom Nav Bar
 * 1. Order Fragment
 * 2. History Fragment
 * 3. Profile Fragment
 *
 *
 *
 * */

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    CartViewModel viewModel;
    Snackbar snackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance((Application) this.getApplicationContext())
                .create(CartViewModel.class);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation_listener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrderFragment()).commit();

        onNewIntent(getIntent());

        getDataUpdate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataUpdate();
    }

    private void getDataUpdate() {
        viewModel.getAll().observeForever(new Observer<List<FoodItem>>() {
            @Override
            public void onChanged(List<FoodItem> foodItems) {
                if(foodItems.size() != 0){
                    snackBar = Snackbar.make(findViewById(android.R.id.content),
                            "Go to Cart", Snackbar.LENGTH_INDEFINITE)
                            .setAction("CART", snackBarListener)
                            .setAnchorView(R.id.bottom_navigation);
                    snackBar.show();
                }else{
                    if(snackBar != null) snackBar.dismiss();
                }
            }
        });
    }

    private View.OnClickListener snackBarListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), Cart.class);
            startActivity(intent);
        }
    };


    private BottomNavigationView.OnNavigationItemSelectedListener navigation_listener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        Fragment selectedFragment = null;

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()){
                case R.id.order:
                    selectedFragment = new OrderFragment();
                    break;
                case R.id.history:
                    selectedFragment = new HistoryFragment();
                    break;
                case R.id.profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };


    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, Login.class));
                break;
        }
        return true;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getStringExtra("data") != null) {
            if (intent.getStringExtra("data").equals("channel")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HistoryFragment()).commit();
            }
        }
    }
}