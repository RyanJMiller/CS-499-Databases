package com.example.project2.activities;
/**
 * InventoryActivity.java
 * This file contains the InventoryActivity class, which manages the inventory screen.
 **/

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.project2.R;
import com.example.project2.adapters.InvListAdapter;
import com.example.project2.dao.InventoryDao;
import com.example.project2.models.Inventory;
import com.example.project2.models.UserDatabase;

import java.util.ArrayList;

public class InventoryActivity extends AppCompatActivity {

    // Data fields
    private ArrayList<Inventory> inventories;
    private InventoryDao inventoryDao;
    private InvListAdapter adapter;

    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // Initialize the database and InventoryDao
        inventoryDao = UserDatabase.getInstance(this).inventoryDao();

        // Initialize the list of inventory items
        inventories = new ArrayList<>();

        // Initialize the adapter for the GridView
        adapter = new InvListAdapter(this, R.layout.inventory_item, inventories, inventoryDao);

        GridView gridView = (GridView)findViewById(R.id.inventoryGridView);

        // Set the adapter for the GridView
        gridView.setAdapter(adapter);

        // Retrieve the currentUserId from the Intent
        currentUserId = getIntent().getIntExtra("USER_ID", -1);

        if (currentUserId == -1) {
            // Handle the error case where the user ID wasn't passed properly
            Toast.makeText(this, "Error: User ID not found", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class); // Redirect to login screen
            startActivity(intent);
            finish();
        }

        // Load the initial data
        loadData(currentUserId);

        // Set up the Spinner for sorting
        Spinner spinner = findViewById((R.id.spinner));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        sortByNameAscending();
                        break;
                    case 1:
                        sortByNameDescending();
                        break;
                    case 2:
                        sortByQuantityAscending();
                        break;
                    case 3:
                        sortByQuantityDescending();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
            }
        });

        // Set up SearchView
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchItems(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchItems(newText);
                return false;
            }
        });

        // Add new item listener
        Button addButton = findViewById(R.id.newEntryButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewItemDialog(); // Show the dialog to add a new item
            }
        });
    }



    // Load inventory items from the database
    private void loadData(int currentUserId) {
        inventoryDao.getItemsForUser(currentUserId).observe(this, newInventories -> {
            inventories.clear();
            inventories.addAll(newInventories);
            adapter.notifyDataSetChanged();
        });
    }

    public void searchItems(String query) {
        inventoryDao.searchItemsByName(currentUserId, query).observe(this, newInventories -> {
            inventories.clear();
            inventories.addAll(newInventories);
            adapter.notifyDataSetChanged();
        });
    }

    public void sortByNameAscending() {
        inventoryDao.getItemsForUserSortedByNameAsc(currentUserId).observe(this, newInventories -> {
            inventories.clear();
            inventories.addAll(newInventories);
            adapter.notifyDataSetChanged();
        });
    }

    public void sortByQuantityAscending() {
        inventoryDao.getItemsForUserSortedByQuantityAsc(currentUserId).observe(this, newInventories -> {
            inventories.clear();
            inventories.addAll(newInventories);
            adapter.notifyDataSetChanged();
        });
    }

    public void sortByNameDescending() {
        inventoryDao.getItemsForUserSortedByNameDesc(currentUserId).observe(this, newInventories -> {
            inventories.clear();
            inventories.addAll(newInventories);
            adapter.notifyDataSetChanged();
        });
    }

    public void sortByQuantityDescending() {
        inventoryDao.getItemsForUserSortedByQuantityDesc(currentUserId).observe(this, newInventories -> {
            inventories.clear();
            inventories.addAll(newInventories);
            adapter.notifyDataSetChanged();
        });
    }

    // Display a dialog to add a new item to the inventory
    public void showNewItemDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_new_item, null);

        EditText inputItemName = dialogView.findViewById(R.id.input_item_name);
        EditText inputQuantity = dialogView.findViewById(R.id.input_quantity);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Item")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String itemName = inputItemName.getText().toString();
                    String quantityString = inputQuantity.getText().toString();
                    int quantity = quantityString.isEmpty() ? 0 : Integer.parseInt(quantityString);

                    //int currentUserId = 1;

                    // Create a new Inventory object and insert it into the database
                    Inventory newItem = new Inventory(itemName, quantity, currentUserId);
                    new Thread(() -> inventoryDao.insertItem(newItem)).start();

                    // Refresh the data displayed in the GridView
                    refreshData(currentUserId);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .create()
                .show();
    }

    // Refresh the data displayed in the GridView
    private void refreshData(int currentUserId) {
        loadData(currentUserId);
    }

    // Open SettingsActivity
    public void openSettingsActivity(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
