package com.devx.fooddelivery.RecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devx.fooddelivery.Model.Order;
import com.devx.fooddelivery.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{

    Context context;
    List<Order> data;

    public OrderAdapter(Context context, List<Order> data){
        this.context = context;
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderDate, orderAmount, orderStatus;
        RecyclerView listView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.order_id);
            orderDate = itemView.findViewById(R.id.order_date);
            orderAmount = itemView.findViewById(R.id.order_amount);
            orderStatus = itemView.findViewById(R.id.order_status);
            listView = itemView.findViewById(R.id.food_items);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_history_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = data.get(position);
        holder.orderId.setText(order.getId());
        holder.orderAmount.setText("$ "+String.valueOf(order.getTotal()));
        holder.orderDate.setText(order.getTimestamp().toDate().toString());

        setOrderStatus(holder, order);

        CustomAdapter customAdapter = new CustomAdapter(order.getFoodId(), context);
        holder.listView.setLayoutManager(new LinearLayoutManager(context));
        holder.listView.setAdapter(customAdapter);

    }

    private void setOrderStatus(@NonNull ViewHolder holder, Order order) {
        if(order.getStatus() == 0)holder.orderStatus.setText("Processing");
        if(order.getStatus() == 1)holder.orderStatus.setText("Processed");
        if(order.getStatus() == 2)holder.orderStatus.setText("Delivered");
        if(order.getStatus() == -1)holder.orderStatus.setText("Failed");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}