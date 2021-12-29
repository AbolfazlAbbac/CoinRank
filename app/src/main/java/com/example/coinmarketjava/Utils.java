package com.example.coinmarketjava;

import android.graphics.Color;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;

import com.example.coinmarketjava.R;
import com.google.android.material.appbar.MaterialToolbar;

public class Utils {
    public static final String KEY_SEND_DATA = "data";

    public static void ToolbarCustom(NavController navController, String title, int idFragment, Toolbar toolbar) {
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == idFragment) {
                toolbar.setTitle(title);
                toolbar.setNavigationIcon(R.drawable.icon_menu);
            }
        });
    }
}
