package com.devx.fooddelivery.Repository;

import android.app.Application;
import android.os.AsyncTask;


import androidx.lifecycle.LiveData;

import com.devx.fooddelivery.ItemDAO.ItemDAO;
import com.devx.fooddelivery.Model.FoodItem;

import java.util.List;



public class CartRepository {
    private ItemDAO itemDAO;
    private CartDatabase cartDatabase;
    LiveData<List<FoodItem>> foodLiveData;


    public CartRepository(Application application){
        cartDatabase = CartDatabase.getInstance(application);
        itemDAO = cartDatabase.itemDAO();
        foodLiveData = itemDAO.getAll();
    }

    public LiveData<FoodItem> getItem(String id) {
       return itemDAO.getItem(id);
    }


    public LiveData<List<FoodItem>> getAll(){
        return foodLiveData;
    }

    public void insertItem(FoodItem foodItem){
        CartDatabase.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                itemDAO.insertItem(foodItem);
            }
        });
    }



    public void deleteItem(String id){
        CartDatabase.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                itemDAO.deleteItem(id);
            }
        });
    }


    public void deleteAll(){
        CartDatabase.databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                itemDAO.deleteAll();
            }
        });
    }



    public void updateItem(String id, int quantity){
        new updateAsyn(itemDAO, id).execute(quantity);
    }

    private static class updateAsyn extends AsyncTask<Integer, Void, Void>{
        private ItemDAO itemDAO;
        private String id;

        public updateAsyn(ItemDAO itemDAO, String id) {
            this.id = id;
            this.itemDAO = itemDAO;
        }


        @Override
        protected Void doInBackground(Integer... integers) {
            int quantity = integers[0];
            itemDAO.updateItem(id, quantity);
            return null;
        }
    }




}
