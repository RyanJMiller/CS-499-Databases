package com.example.project2.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String username; // The username of the user

    public String hash; // The hash of the user's password

    public String salt; // The salt used for password hashing
}