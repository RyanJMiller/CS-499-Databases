package com.example.project2.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.project2.models.User;

@Dao
public interface UserDao {
    // Insert a new user into the user table
    @Insert
    void insert(User user);

    // Query to retrieve a user by their username
    @Query("SELECT * FROM user WHERE username = :username")
    User getUser(String username);

    // Query to count the number of users with a specific username
    @Query("SELECT COUNT(*) FROM user WHERE username = :username")
    int countUsersByUsername(String username);
}
