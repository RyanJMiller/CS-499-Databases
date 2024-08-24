package com.example.project2.activities;

/**
 * MainActivity.java
 * This file contains the MainActivity class, which manages the Login screen.
 **/

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project2.R;
import com.example.project2.models.User;
import com.example.project2.models.UserDatabase;
import com.example.project2.util.InputValidationService;
import com.example.project2.util.PasswordHandler;

public class MainActivity extends AppCompatActivity {

    // User database init
    private UserDatabase db;
    // local init of PasswordHandler class
    PasswordHandler handler = new PasswordHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize user database
        db = UserDatabase.getInstance(this);

        // Register button listener
        Button registerButton = findViewById(R.id.buttonRegister);
        registerButton.setOnClickListener(v -> registerUser());

        // Login button listener
        Button loginButton = findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(v -> loginUser());


    }

    // Method to handle user login: includes input validation and secure password validation
    public void loginUser() {
        new Thread(() -> {
            EditText usernameEditText = findViewById(R.id.editTextText);
            EditText passwordEditText = findViewById(R.id.editTextTextPassword);

            // get entered username and password
            String usernameLogin = usernameEditText.getText().toString().trim();
            String passwordLogin = passwordEditText.getText().toString().trim();

            User user = db.userDao().getUser(usernameLogin);

            // Check if input is valid
            if (!InputValidationService.isValidUsername(usernameLogin) ||
                !InputValidationService.isValidPassword(passwordLogin)) {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Invalid input", Toast.LENGTH_SHORT).show());
                return;
            }

            if (user != null) {
                // Check if the entered password is correct
                if (handler.onUserLogin(passwordLogin, user.hash, user.salt)) {
                    // Login Correct: Get the userID and open the InventoryActivity
                    int currentUserId = user.id;

                    Intent intent = new Intent(this, InventoryActivity.class);
                    intent.putExtra("USER_ID",currentUserId);
                    startActivity(intent);

                } else {
                    // Login Wrong
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show());
                }
            } else {
                // Login Wrong
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    // Method to handle user registration: includes input validation and password salt and hashing
    public void registerUser() {
        new Thread(() -> {
            try {
                EditText usernameEditText = findViewById(R.id.editTextText);
                EditText passwordEditText = findViewById(R.id.editTextTextPassword);

                String usernameRegister = usernameEditText.getText().toString().trim();
                String passwordRegister = passwordEditText.getText().toString().trim();

                if (!InputValidationService.isValidUsername(usernameRegister) ||
                        !InputValidationService.isValidPassword(passwordRegister)) {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Invalid input", Toast.LENGTH_SHORT).show());
                    return;
                }

                int userCount = db.userDao().countUsersByUsername(usernameRegister);
                if (userCount > 0) {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show());
                } else {
                    User newUser = new User();
                    newUser.username = usernameRegister;
                    newUser.salt = handler.getRegistrationSalt();
                    newUser.hash = handler.onUserRegistration(passwordRegister, newUser.salt);

                    db.userDao().insert(newUser);

                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Registration failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
                e.printStackTrace();  // Log the stack trace for debugging
            }
        }).start();
    }
}