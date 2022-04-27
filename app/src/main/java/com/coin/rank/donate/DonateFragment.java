package com.coin.rank.donate;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.rank.R;
import com.example.rank.databinding.FragmentDonateBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import me.ibrahimsn.lib.SmoothBottomBar;

public class DonateFragment extends Fragment {
    SmoothBottomBar smoothBottomBar;
    FragmentDonateBinding binding;
    CompositeDisposable compositeDisposable;
    ClipboardManager clipboardManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationView navigationView = requireActivity().findViewById(R.id.navigationView);
        navigationView.setCheckedItem(R.id.donate);
        hideBottomNavigation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_donate, container, false);
        compositeDisposable = new CompositeDisposable();
        clipboardManager = (ClipboardManager) requireActivity().getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);

        Toolbar toolbar = binding.toolbar2;
        toolbar.setNavigationIcon(R.drawable.icon_menu);

        toolbar.setNavigationOnClickListener(view -> {
            DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawerLayout);
            drawerLayout.open();
        });

        CopyTextInformationBank();


        return binding.getRoot();
    }

    private void CopyTextInformationBank() {
       binding.copyCartIVDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clipboardManager.setText(binding.cartTVDonate.getText().toString());
                Toast.makeText(requireContext().getApplicationContext(), "Copied :)", Toast.LENGTH_SHORT).show();
            }
        });
        binding.copyTetherIVDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clipboardManager.setText(binding.TetherTVDonateTRC.getText().toString());
                Toast.makeText(requireContext().getApplicationContext(), "Copied :)", Toast.LENGTH_SHORT).show();
            }
        });

        binding.copyTetherIVDonateERC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clipboardManager.setText(binding.TetherTVDonateERC.getText().toString());
                Toast.makeText(requireContext().getApplicationContext(), "Copied :)", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void hideBottomNavigation() {
        smoothBottomBar = requireActivity().findViewById(R.id.bottomNavigation);
        smoothBottomBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        smoothBottomBar.setVisibility(View.VISIBLE);
        compositeDisposable.clear();
    }
}