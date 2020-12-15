package com.devx.fooddelivery.ItemDAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.devx.fooddelivery.Model.FoodItem;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface ItemDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItem(FoodItem foodItem);

    @Query("UPDATE Item SET quantity = :quantity where id = :id")
    void updateItem(String id, int quantity);

    @Query("DELETE FROM Item where id = :id")
    void deleteItem(String id);

    @Query("SELECT * FROM Item where id = :id")
    LiveData<FoodItem> getItem(String id);

    @Query("SELECT * FROM Item")
    LiveData<List<FoodItem>> getAll();

    @Query("DELETE FROM Item")
    void deleteAll();

}
