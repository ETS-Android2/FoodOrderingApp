package com.devx.fooddelivery.VIewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.devx.fooddelivery.Model.FoodItem;
import com.devx.fooddelivery.Repository.CartRepository;

import java.util.List;


public class CartViewModel extends AndroidViewModel {
    private CartRepository cartRepository;

    public CartViewModel(@NonNull Application application) {
        super(application);
        cartRepository = new CartRepository(application);
    }

    public void insertItem(FoodItem foodItem){
        cartRepository.insertItem(foodItem);
    }

    public void updateItem(String id, int quantity){
        cartRepository.updateItem(id, quantity);
    }

    public void deleteItem(String id){
        cartRepository.deleteItem(id);
    }

    public void deleteAll(){cartRepository.deleteAll();}

    public LiveData<FoodItem> getItem(String id){
       return cartRepository.getItem(id);
    }

    public LiveData<List<FoodItem>> getAll(){
        return cartRepository.getAll();
    }

}
