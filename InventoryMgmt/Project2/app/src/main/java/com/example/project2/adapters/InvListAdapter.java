package com.example.project2.adapters;

/**
 * InvListAdapter
 * This is the InvListAdapter, the adapter responsible for the inventory list
 **/

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.project2.R;
import com.example.project2.dao.InventoryDao;
import com.example.project2.models.Inventory;

import java.util.ArrayList;
import java.util.List;

public class InvListAdapter extends ArrayAdapter<Inventory>{

    private InventoryDao inventoryDao;
    private Context context;
    public InvListAdapter(@NonNull Context context, int resource, @NonNull List<Inventory> inventories, InventoryDao inventoryDao) {
        super(context, resource, inventories);
        this.inventoryDao = inventoryDao;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridView = convertView;

        if(gridView == null) {
            // Inflate the layout for each item in the GridView
            gridView = LayoutInflater.from(getContext()).inflate(R.layout.inventory_item, parent, false);
        }

        // Views within the item layout
        TextView itemName = gridView.findViewById(R.id.itemName);
        TextView quantityView = gridView.findViewById(R.id.quantity);
        Button minusButton = gridView.findViewById(R.id.minusButton);
        Button plusButton = gridView.findViewById(R.id.plusButton);
        ImageButton deleteButton = gridView.findViewById(R.id.deleteButton);

        // Get the current Inventory item
        Inventory currentItem = getItem(position);

        // Set the item name and quantity
        itemName.setText(currentItem.getItemName());
        quantityView.setText(String.valueOf(currentItem.getQuantity()));

        // Define the click listeners for the buttons
        minusButton.setOnClickListener(v -> {
            int quantity = currentItem.getQuantity();
            if (quantity > 0) {
                new Thread(() -> {
                    currentItem.setQuantity(quantity - 1);
                    inventoryDao.updateItem(currentItem); // Update the item in the database
                    // Update the UI on the main thread
                    ((Activity) getContext()).runOnUiThread(() -> {
                        quantityView.setText(String.valueOf(currentItem.getQuantity()));
                    });
                }).start();
            }
        });

        plusButton.setOnClickListener(v -> {
            new Thread(() -> {
                int quantity = currentItem.getQuantity();
                currentItem.setQuantity(quantity + 1);
                inventoryDao.updateItem(currentItem); // Update the item in the database

                // Update the UI on the main thread
                ((Activity) getContext()).runOnUiThread(() -> {
                    quantityView.setText(String.valueOf(currentItem.getQuantity()));
                });
            }).start();
        });

        deleteButton.setOnClickListener(v -> {
            new Thread(() -> {
                inventoryDao.deleteItem(currentItem); // Delete the item from the database
                // UI update on the main thread
                ((Activity) getContext()).runOnUiThread(() -> {
                    remove(currentItem);
                    notifyDataSetChanged();
                });
            }).start();
        });

        return gridView;
    }
}
