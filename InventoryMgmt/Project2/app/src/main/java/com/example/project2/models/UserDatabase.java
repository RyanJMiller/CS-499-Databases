package com.example.project2.models;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.project2.dao.InventoryDao;
import com.example.project2.dao.UserDao;

@Database(entities = {User.class, Inventory.class}, version = 3)
public abstract class UserDatabase extends RoomDatabase {
    public abstract UserDao userDao();  // Access to the UserDao
    public abstract InventoryDao inventoryDao();    // Access to the InventoryDao

    private static volatile UserDatabase INSTANCE; // Declaration of the INSTANCE variable

    // Singleton to ensure each activity that uses the database uses the same single instance
    public static UserDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (UserDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    UserDatabase.class, "database-name")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
