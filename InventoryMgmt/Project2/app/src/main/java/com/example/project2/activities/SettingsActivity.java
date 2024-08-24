package com.example.project2.activities;

/**
 * SettingsActivity.java
 * This file contains the SettingsActivity class, which manages the Settings screen.
 **/

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.Manifest;
import android.widget.Switch;

import com.example.project2.R;

public class SettingsActivity extends AppCompatActivity {

    // Permission constants
    private static final String SEND_SMS = Manifest.permission.SEND_SMS;
    private static final int SMS_PERMISSION_REQUEST_CODE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    // Method to request SMS permission
    private void requestSmsPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.SEND_SMS},
                SMS_PERMISSION_REQUEST_CODE
        );
    }


    // Method to handle the SMS switch click event
    public void onSmsSwitchClicked(View view) {
        Switch smsSwitch = (Switch) view;
        boolean smsNotificationsEnabled = smsSwitch.isChecked();

        if (smsNotificationsEnabled) {
            // User wants to enable SMS notifications, request permission
            requestSmsPermission();
        }
    }

    // Method to open the InventoryActivity
    public void openInventoryActivity(View view) {
        Intent intent = new Intent(this, InventoryActivity.class);
        startActivity(intent);
    }
}