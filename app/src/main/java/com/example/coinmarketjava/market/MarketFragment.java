package com.example.coinmarketjava.market;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.Utils;
import com.example.coinmarketjava.MainActivity;
import com.example.coinmarketjava.R;
import com.example.coinmarketjava.Roomdb.Entities.RoomAllMarket;
import com.example.coinmarketjava.databinding.FragmentMarketBinding;
import com.example.coinmarketjava.home.TopGainLoseAdapterRv;
import com.example.coinmarketjava.market.adapter.AdapterMarketFragment;
import com.example.coinmarketjava.model.repository.AllCoinMarket;
import com.example.coinmarketjava.model.repository.DataItem;
import com.example.coinmarketjava.viewModel.AppViewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MarketFragment extends Fragment {

    FragmentMarketBinding fragmentMarketBinding;
    MainActivity mainActivity;
    AppViewModel appViewModel;
    AdapterMarketFragment adapterMarketFragment;
    CompositeDisposable compositeDisposable;

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

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        compositeDisposable = new CompositeDisposable();

        setupAllCrypto();
        return fragmentMarketBinding.getRoot();

    }

    private void setupAllCrypto() {
        Disposable disposable = appViewModel.getAllMarketFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RoomAllMarket>() {
                    @Override
                    public void accept(RoomAllMarket roomAllMarket) throws Throwable {
                        List<DataItem> dataItems = new ArrayList<>();
                        AllCoinMarket allCoinMarket = roomAllMarket.getAllCoinMarket();
                        dataItems = allCoinMarket.getRootData().getCryptoCurrencyList();

                        if (adapterMarketFragment == null) {
                            adapterMarketFragment = new AdapterMarketFragment((ArrayList<DataItem>) dataItems);
                            fragmentMarketBinding.marketFragmentRv.setAdapter(adapterMarketFragment);
                        } else {
                            fragmentMarketBinding.marketFragmentRv.setAdapter(adapterMarketFragment);
                            adapterMarketFragment.update((ArrayList<DataItem>) dataItems);
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        setupToolbar(view);

        super.onViewCreated(view, savedInstanceState);
    }

    private void setupToolbar(View view) {
        CollapsingToolbarLayout collapsingToolbarLayout = fragmentMarketBinding.collapsingToolbarLayout;

        NavController navController = Navigation.findNavController(view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.marketFragment)
                .setOpenableLayout(mainActivity.drawerLayout)
                .build();

        Toolbar materialToolbar = view.findViewById(R.id.toolbarMarketFragment);

        NavigationUI.setupWithNavController(materialToolbar, navController, appBarConfiguration);

        Utils.ToolbarCustom(navController, getString(R.string.market), R.id.marketFragment, materialToolbar);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.marketFragment) {
                    collapsingToolbarLayout.setTitleEnabled(false);
                    materialToolbar.setTitle("Market");
                    materialToolbar.setTitleTextColor(Color.WHITE);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}