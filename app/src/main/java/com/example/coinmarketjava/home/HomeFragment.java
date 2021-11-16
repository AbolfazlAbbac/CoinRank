package com.example.coinmarketjava.home;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.Utils;
import com.example.coinmarketjava.MainActivity;
import com.example.coinmarketjava.R;
import com.example.coinmarketjava.databinding.FragmentHomeBinding;
import com.example.coinmarketjava.viewModel.AppViewModel;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {
    FragmentHomeBinding fragmentHomeBinding;
    MainActivity mainActivity;
    AppViewModel viewModel;

    @Inject
    @Named("family")
    String name;

    @Override
    public void onAttach(@NonNull Context context) {
        mainActivity = (MainActivity) context;
        super.onAttach(context);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        setupViewPager2();

        return fragmentHomeBinding.getRoot();
    }

    private void setupViewPager2() {
        viewModel.getBannerLiveData().observe(getViewLifecycleOwner(), arrayList -> {
            fragmentHomeBinding.viewPager2Banner.setAdapter(new BannerAdapter(arrayList));
            fragmentHomeBinding.viewPager2Banner.setOffscreenPageLimit(3);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);

        setupToolbar(view);
    }

    private void setupToolbar(View view) {
        NavController navController = Navigation.findNavController(view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment)
                .setOpenableLayout(mainActivity.drawerLayout)
                .build();

        Toolbar toolbar = view.findViewById(R.id.toolbar);

        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        Utils.ToolbarCustom(navController, getString(R.string.home), R.id.homeFragment, toolbar);


    }
}