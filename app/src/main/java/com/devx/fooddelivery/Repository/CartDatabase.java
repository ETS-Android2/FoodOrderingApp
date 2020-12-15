package com.devx.fooddelivery.Repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.devx.fooddelivery.ItemDAO.ItemDAO;
import com.devx.fooddelivery.Model.FoodItem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {FoodItem.class}, version = 2, exportSchema = false)
public abstract class CartDatabase extends RoomDatabase {
    private static CartDatabase instance;

    private static final int NO_OF_THREADS = 4;

   static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NO_OF_THREADS);

    public abstract ItemDAO itemDAO();



    public static synchronized CartDatabase getInstance(Context context){
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), CartDatabase.class, "Item")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
