package com.devx.fooddelivery.RecyclerViews;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.devx.fooddelivery.Model.FoodItem;
import com.devx.fooddelivery.R;
import com.devx.fooddelivery.ServiceGenerator.FirebaseService;
import com.devx.fooddelivery.VIewModel.CartViewModel;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder>{

    Context context;
    List<FoodItem> data, allData;
    CartViewModel viewModel;
   // LifecycleOwner lifecycleOwner;
    StorageReference storageReference = FirebaseService.getStorageRef().getReference("foodImages");

    public FoodAdapter() {
    }

    public FoodAdapter(Context context, List<FoodItem> data) {
        this.context = context;
        this.data = data;
        allData = new ArrayList<>(data);
        viewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance((Application) context.getApplicationContext())
                .create(CartViewModel.class);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout constraintLayout;
        ImageView image;
        TextView price, name, category;
        Button addToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.parent_layout);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            category = itemView.findViewById(R.id.category);
            addToCart = itemView.findViewById(R.id.cart);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_list_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem item = data.get(position);
        item.setQuantity(0);

        RequestOptions requestOptions = new RequestOptions()
                .transform(new RoundedCorners(15))
                .placeholder(R.drawable.ic_launcher_foreground);

        Glide.with(context)
                .load(storageReference.child(item.getImage()))
                .apply(requestOptions)
                .into(holder.image);

        holder.name.setText(item.getName());
        holder.price.setText("$" + String.valueOf(item.getPrice()));
        holder.category.setText(item.getCuisine());

        Observer<FoodItem> observer = new Observer<FoodItem>() {
            @Override
            public void onChanged(FoodItem foodItem) {
                if(foodItem == null){
                    item.setQuantity(0);
                    holder.addToCart.setText("Add +");
                }
                else{
                    holder.addToCart.setText("Added");
                }
            }
        };

        viewModel.getItem(item.getId()).observeForever(observer);

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.getQuantity() == 0){
                    item.setQuantity(1);
                   FoodItem dbItem = new FoodItem(
                           item.getId(),
                           item.getName(),
                           item.getPrice(),
                           item.getDiscount(),
                           item.getQuantity(),
                           item.getCuisine(),
                           item.getImage(),
                           item.isVegan(),
                           item.isAvailability()
                   );
                   viewModel.insertItem(dbItem);
                }else{
                    viewModel.deleteItem(item.getId());
                    item.setQuantity(0);
                }
            }

        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
