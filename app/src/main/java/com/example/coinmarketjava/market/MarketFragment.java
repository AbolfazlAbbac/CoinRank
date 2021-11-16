package com.example.coinmarketjava.market;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.Utils;
import com.example.coinmarketjava.MainActivity;
import com.example.coinmarketjava.R;
import com.example.coinmarketjava.databinding.FragmentMarketBinding;


public class MarketFragment extends Fragment {

    FragmentMarketBinding fragmentMarketBinding;
    MainActivity mainActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        mainActivity = (MainActivity) context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentMarketBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_market, container, false);
        return fragmentMarketBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        setupToolbar(view);

        super.onViewCreated(view, savedInstanceState);
    }

    private void setupToolbar(View view) {
        NavController navController = Navigation.findNavController(view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.marketFragment)
                .setOpenableLayout(mainActivity.drawerLayout)
                .build();

        Toolbar materialToolbar = view.findViewById(R.id.toolbar);

        NavigationUI.setupWithNavController(materialToolbar, navController, appBarConfiguration);

        Utils.ToolbarCustom(navController, getString(R.string.market), R.id.marketFragment, materialToolbar);

    }
}