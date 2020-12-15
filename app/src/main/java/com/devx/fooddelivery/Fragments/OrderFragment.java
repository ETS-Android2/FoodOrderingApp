package com.devx.fooddelivery.Fragments;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devx.fooddelivery.Cart;
import com.devx.fooddelivery.Model.FoodItem;
import com.devx.fooddelivery.R;
import com.devx.fooddelivery.RecyclerViews.FoodAdapter;
import com.devx.fooddelivery.ServiceGenerator.FirebaseService;
import com.devx.fooddelivery.VIewModel.CartViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    //UI and Layout
    View view;
    Context context;
    RecyclerView recyclerView;
    FoodAdapter foodAdapter;
    CartViewModel viewModel;

    CoordinatorLayout coordinatorLayout;
    ProgressBar progressBar;
    Snackbar snackBar;


    //Firebase Reference
    DatabaseReference databaseReference;

    //Data to RecyclerView
    List<FoodItem> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_order_fragment, container, false);
        context = view.getContext();
        data = new ArrayList<>();

        viewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance((Application) context.getApplicationContext())
                .create(CartViewModel.class);
        coordinatorLayout = view.findViewById(R.id.coordinator);
        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);
        databaseReference = FirebaseService.getDbInstance().getReference("FoodItems");
        getData();
//        viewModel.getAll().observeForever(new Observer<List<FoodItem>>() {
//            @Override
//            public void onChanged(List<FoodItem> foodItems) {
//                if(foodItems.size() != 0){
//                    snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
//                            "Go to Cart", Snackbar.LENGTH_INDEFINITE)
//                            .setAction("CART", snackBarListener)
//                            .setAnchorView(R.id.bottom_navigation);
//                    snackBar.show();
//                }else{
//                    if(snackBar != null) snackBar.dismiss();
//                }
//            }
//        });

        return view;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        viewModel.getAll().observeForever(new Observer<List<FoodItem>>() {
//            @Override
//            public void onChanged(List<FoodItem> foodItems) {
//                if(foodItems.size() != 0){
//                    snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
//                            "Go to Cart", Snackbar.LENGTH_INDEFINITE)
//                            .setAction("CART", snackBarListener)
//                            .setAnchorView(R.id.bottom_navigation);
//                    snackBar.show();
//                }else{
//                    if(snackBar != null) snackBar.dismiss();
//                }
//            }
//        });
//    }


    //    private View.OnClickListener cart = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
//                    "Go to Cart", Snackbar.LENGTH_LONG)
//                    .setAction("CART", snackBarListener)
//                    .setAnchorView(R.id.bottom_navigation);
//            snackBar.show();
//        }
//    };

//    private View.OnClickListener buttonClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
//                    "Go to Cart", Snackbar.LENGTH_LONG)
//                    .setAction("CART", snackBarListener)
//                    .setAnchorView(R.id.bottom_navigation);
//            snackBar.show();
//        }
//    };

//    private View.OnClickListener snackBarListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Intent intent = new Intent(getActivity(), Cart.class);
//            startActivity(intent);
//        }
//    };


    private void getData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item : snapshot.getChildren()){
                    data.add(item.getValue(FoodItem.class));
                }
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                initRecycler();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecycler() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        foodAdapter = new FoodAdapter(context, data);
        recyclerView.setAdapter(foodAdapter);
    }

}