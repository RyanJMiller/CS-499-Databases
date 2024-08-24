package com.example.project2.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "inventory_items")
public class Inventory {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "item_name")
    private String itemName;

    @ColumnInfo(name = "quantity")
    private int quantity;

    @ColumnInfo(name = "user_id")
    private int userId;  // Foreign key to reference the user who owns this item

    // Constructor to initialize an Inventory item with a name, userId, and quantity
    public Inventory(String itemName, int quantity, int userId) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.userId = userId;
    }

    // Setters
    public void setItemName(String name) {
        this.itemName = name;
    }

    public void setQuantity(int num) {
        this.quantity = num;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Getters
    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }
}
