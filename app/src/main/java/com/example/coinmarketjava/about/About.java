package com.example.coinmarketjava.about;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coinmarketjava.DetailFragment;
import com.example.coinmarketjava.R;
import com.example.coinmarketjava.databinding.FragmentAboutBinding;
import com.google.android.material.navigation.NavigationView;

import me.ibrahimsn.lib.SmoothBottomBar;


public class About extends Fragment {

    SmoothBottomBar smoothBottomBar;
    FragmentAboutBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationView navigationView = requireActivity().findViewById(R.id.navigationView);
        navigationView.setCheckedItem(R.id.about);
        hideBottomNavigation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = binding.toolbarAbout;
        toolbar.setNavigationIcon(R.drawable.icon_menu);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawerLayout);
                drawerLayout.open();
            }
        });

        binding.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mailto = "mailto:AbolfazlAbbasi185@gmail.com" +
                        "?cc=" +
                        "&subject=" + Uri.encode("your subject") +
                        "&body=" + Uri.encode("your mail body");
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(mailto));
                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(requireContext(), "Error to open email app", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.telegramID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=abolfazlabbac"));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/abolfazlabbac"));
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        smoothBottomBar.setVisibility(View.VISIBLE);
    }


    private void hideBottomNavigation() {
        smoothBottomBar = getActivity().findViewById(R.id.bottomNavigation);
        smoothBottomBar.setVisibility(View.GONE);
    }

}