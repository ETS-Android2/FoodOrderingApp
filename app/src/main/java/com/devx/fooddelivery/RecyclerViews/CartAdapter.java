package com.devx.fooddelivery.RecyclerViews;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.devx.fooddelivery.Cart;
import com.devx.fooddelivery.Model.FoodItem;
import com.devx.fooddelivery.R;
import com.devx.fooddelivery.VIewModel.CartViewModel;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Context context;
    List<FoodItem> data;
    public Integer total;
    Cart cart;
    CartViewModel viewModel;

    public final static String TAG = "CART ADAPTER";

    public CartAdapter(List<FoodItem> data, Cart cart, Context context){
        this.data = data;
        this.context = context;
        this.cart = cart;
        viewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance((Application) context.getApplicationContext())
                .create(CartViewModel.class);
    }

    public CartAdapter() {

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        ConstraintLayout constraintLayout;
        TextView name, price, total, quantity;
        ImageView add, sub;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.cart_list);
            linearLayout = itemView.findViewById(R.id.linear);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            total = itemView.findViewById(R.id.total);
            quantity = itemView.findViewById(R.id.quantity);
            add = itemView.findViewById(R.id.add);
            sub = itemView.findViewById(R.id.sub);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "OnCreateViewHolder");
        View view = LayoutInflater.from(context).inflate(R.layout.cart_list_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "OnBindViewHolder");

        FoodItem item = data.get(position);
        holder.name.setText(item.getName());
        holder.price.setText("$" + String.valueOf(item.getPrice()));
        holder.quantity.setText(String.valueOf(item.getQuantity()));
        holder.total.setText("$"+String.valueOf(item.getPrice()*item.getQuantity()));
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = item.getQuantity();
                if(temp < 10){
                    temp += 1;
                    item.setQuantity(temp);
                    holder.quantity.setText(String.valueOf(temp));
                    int tot = item.getPrice() * item.getQuantity();
                    holder.total.setText("$"+String.valueOf(tot));
                    viewModel.updateItem(item.getId(), temp);
                }
                else{
                    Toast.makeText(context, "Maximum Quantity : 10", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer temp = item.getQuantity();
                if(temp > 0){
                    temp -= 1;
                    if(temp == 0){
                        viewModel.deleteItem(item.getId());
                        data.remove(position);
                        notifyItemRemoved(position);
                    }else {
                        item.setQuantity(temp);
                        holder.quantity.setText(String.valueOf(temp));
                        int tot = item.getPrice() * item.getQuantity();
                        holder.total.setText("$"+String.valueOf(tot));
                        viewModel.updateItem(item.getId(), temp);
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }
}
