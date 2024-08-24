package com.example.project2.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.project2.models.Inventory;

import java.util.List;

@Dao
public interface InventoryDao {

    // Query to retrieve all items for the current user
    @Query("SELECT * FROM inventory_items WHERE user_id = :userId")
    LiveData<List<Inventory>> getItemsForUser(int userId);


    // Search items by name
    @Query("SELECT * FROM inventory_items WHERE user_id = :userId AND item_name LIKE '%' || :searchQuery || '%'")
    LiveData<List<Inventory>> searchItemsByName(int userId, String searchQuery);

    // Sort items by name in ascending order
    @Query("SELECT * FROM inventory_items WHERE user_id = :userId ORDER BY item_name ASC")
    LiveData<List<Inventory>> getItemsForUserSortedByNameAsc(int userId);

    // Sort items by name in descending order
    @Query("SELECT * FROM inventory_items WHERE user_id = :userId ORDER BY item_name DESC")
    LiveData<List<Inventory>> getItemsForUserSortedByNameDesc(int userId);

    // Sort items by quantity in ascending order
    @Query("SELECT * FROM inventory_items WHERE user_id = :userId ORDER BY quantity ASC")
    LiveData<List<Inventory>> getItemsForUserSortedByQuantityAsc(int userId);

    // Sort items by quantity in descending order
    @Query("SELECT * FROM inventory_items WHERE user_id = :userId ORDER BY quantity DESC")
    LiveData<List<Inventory>> getItemsForUserSortedByQuantityDesc(int userId);


    // Insert a new item into the inventory_items table
    @Insert
    void insertItem(Inventory inventory);

    // Update an existing item in the inventory_items table
    @Update
    void updateItem(Inventory inventory);

    // Delete an item from the inventory_items table
    @Delete
    void deleteItem(Inventory inventory);
}
