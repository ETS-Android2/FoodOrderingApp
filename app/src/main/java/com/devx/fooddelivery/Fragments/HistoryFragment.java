package com.devx.fooddelivery.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devx.fooddelivery.Model.Order;
import com.devx.fooddelivery.Model.OrderItem;
import com.devx.fooddelivery.R;
import com.devx.fooddelivery.RecyclerViews.OrderAdapter;
import com.devx.fooddelivery.ServiceGenerator.FirebaseService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;




public class HistoryFragment extends Fragment {

    //UI and Layout
    View view;
    Context context;
    RecyclerView recyclerView;
    OrderAdapter orderAdapter;
    ListenerRegistration listener;
    RecyclerView.LayoutManager layoutManager;

    //Firebase ref
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;

    private static final String TAG = "Orders";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_history_fragment, container, false);
        context = view.getContext();

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.history_recycler);

        //Recycler INIT
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));


        Query query = firestore.collection("orders")
                .whereEqualTo("userId", firebaseAuth.getCurrentUser().getUid())
                .orderBy("timestamp" , Query.Direction.DESCENDING);
        listener  = query.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(context, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    List<Order> getOrder = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshots : value) {
                        Order order = snapshots.toObject(Order.class);
                        getOrder.add(order);
                    }
                    recyclerView.setAdapter(new OrderAdapter(context, getOrder));
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listener.remove();
    }
}