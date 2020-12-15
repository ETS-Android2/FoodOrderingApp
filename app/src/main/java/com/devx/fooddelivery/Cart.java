package com.devx.fooddelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.devx.fooddelivery.Model.FoodItem;
import com.devx.fooddelivery.Model.Order;
import com.devx.fooddelivery.Model.OrderItem;
import com.devx.fooddelivery.Model.User;
import com.devx.fooddelivery.Notifications.NotificationService;
import com.devx.fooddelivery.RecyclerViews.CartAdapter;
import com.devx.fooddelivery.VIewModel.CartViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Cart extends AppCompatActivity implements PaymentResultListener {

    RecyclerView recyclerView;
    CartViewModel viewModel;
    CartAdapter cartAdapter;
    Button place_order;
    TextView total;
    RecyclerView.LayoutManager manager;
    List<FoodItem> data;
    List<OrderItem> items = new ArrayList<>();
    User user;
    Context context;
    int grandTotal = 0;


    public final String TAG = "Cart";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);



        //Find ID's
        total = findViewById(R.id.grandtotalvalue);
        place_order = findViewById(R.id.place_order);
        recyclerView = findViewById(R.id.cart_recycler_view);
        context = this;


        if(recyclerView.getVisibility() == View.GONE){
            finish();
        }

        //RazorPay Preload
        Checkout.preload(getApplicationContext());


        //Recycler View Init
        data = new ArrayList<>();
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.hasFixedSize();

        //PlaceOrder Button
        place_order.setOnClickListener(placeOrder);


        //ViewModel to observe CartItems
        viewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance((Application) this.getApplicationContext())
                .create(CartViewModel.class);

        viewModel.getAll().observe(this, new Observer<List<FoodItem>>() {
            List<FoodItem> data = new ArrayList<>();

            @Override
            public void onChanged(List<FoodItem> foodItems) {
                cartAdapter = new CartAdapter(foodItems, (Cart) context, context);
                recyclerView.setAdapter(cartAdapter);
                setTotal(foodItems);
            }
        });

    }

    //Set GrandTotal in Cart
    private void setTotal(List<FoodItem> foodItems) {
        int totalp = 0;
        if(foodItems.size() == 0){
            finish();
        }
        for(FoodItem item : foodItems){
            totalp += item.getQuantity()*item.getPrice();
        }
        total.setText("$" + totalp);
        grandTotal = totalp;
    }


    private View.OnClickListener placeOrder = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            prepareOrder();
            startPayment();
        }
    };


    //Payment Processing
    private void startPayment() {
        final Activity activity = this;
        FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                       if(task.isSuccessful()){
                           if(task.getResult() != null){
                               setData(task.getResult().toObject(User.class));
                           }
                       }
                    }
                });

        final Checkout checkout = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Tomato");
            options.put("description", "Food Delivery");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", String.valueOf(grandTotal*100));

            checkout.open(activity, options);
            Checkout.clearUserData(getApplicationContext());
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }


    //Data from FireStore
    private void setData(User toObject) {
        user = new User();
        user.setName(toObject.getName());
        user.setEmail(toObject.getEmail());
        user.setNumber(toObject.getNumber());
        user.setAddress(toObject.getAddress());
        user.setId(toObject.getId());
    }


    @Override
    public void onPaymentSuccess(String PaymentID) {
        try {
            createOrder();
        } catch (Exception e) {
            Toast.makeText(context, "Payment Failed! Try Again", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }



    private void createOrder() {
        Order order = new Order(
                UUID.randomUUID().toString(),
                Timestamp.now(),
                FirebaseAuth.getInstance().getUid(),
                items,
                "RazorPay",
                grandTotal,
                0
        );

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("orders").document(order.getId()).set(order)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            showSuccessDialog();
                            showNotification();
                        }
                    }
                });
    }

    private void prepareOrder() {
        viewModel.getAll().observe(this, new Observer<List<FoodItem>>() {
            @Override
            public void onChanged(List<FoodItem> foodItems) {
                for (FoodItem item : foodItems){
                    extractItem(item);
                }
                viewModel.getAll().removeObserver(this::onChanged);
            }
        });
    }


    private void showNotification(){
        NotificationService notificationService = new NotificationService(this);
        notificationService.SuccessNotification();
    }

    private void extractItem(FoodItem item) {
        items.add(new OrderItem(item.getId(), item.getName(), item.getPrice(), item.getQuantity()));
    }

    private void showSuccessDialog() {
        ConstraintLayout constraintLayout = findViewById(R.id.cart_activity);
        constraintLayout.setVisibility(View.GONE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.payment_success, null);
        builder.setView(view);
        Button button1 = view.findViewById(R.id.okay);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.deleteAll();
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(200,200);
        dialog.show();
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

}