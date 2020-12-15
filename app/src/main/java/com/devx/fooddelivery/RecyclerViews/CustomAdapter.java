package com.devx.fooddelivery.RecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.devx.fooddelivery.Model.OrderItem;
import com.devx.fooddelivery.R;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    List<OrderItem> foodId;
    Context context;

    public CustomAdapter(List<OrderItem> foodId, Context context) {
        this.foodId = foodId;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_history_fragment_ltem_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItem item = foodId.get(position);
        holder.textView.setText(item.getName() + " x " + item.getQuantity());
        holder.price.setText("$ "+item.getPrice());
    }

    @Override
    public int getItemCount() {
        return foodId.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView  textView, price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_name);
            price = itemView.findViewById(R.id.item_price);
        }
    }

}
