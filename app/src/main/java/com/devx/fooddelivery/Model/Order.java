package com.devx.fooddelivery.Model;

import com.google.firebase.Timestamp;

import java.util.List;

public class Order {
    String id;
    Timestamp timestamp;
    String userId;
    List<OrderItem> foodId;
    String Payment;
    int total;
    int status;

    public Order() {
    }

    public Order(String id, Timestamp timestamp, String userId, List<OrderItem> foodId, String payment, int total, int status) {
        this.id = id;
        this.timestamp = timestamp;
        this.userId = userId;
        this.foodId = foodId;
        Payment = payment;
        this.total = total;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<OrderItem> getFoodId() {
        return foodId;
    }

    public void setFoodId(List<OrderItem> foodId) {
        this.foodId = foodId;
    }

    public String getPayment() {
        return Payment;
    }

    public void setPayment(String payment) {
        Payment = payment;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
