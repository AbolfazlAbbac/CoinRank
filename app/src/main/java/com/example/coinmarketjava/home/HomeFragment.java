package com.example.coinmarketjava.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import com.example.Utils;
import com.example.coinmarketjava.MainActivity;
import com.example.coinmarketjava.R;
import com.example.coinmarketjava.Roomdb.Entities.RoomAllMarket;
import com.example.coinmarketjava.databinding.FragmentHomeBinding;
import com.example.coinmarketjava.model.repository.AllCoinMarket;
import com.example.coinmarketjava.model.repository.DataItem;
import com.example.coinmarketjava.viewModel.AppViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class HomeFragment extends Fragment {
    FragmentHomeBinding fragmentHomeBinding;

    MainActivity mainActivity;

    AppViewModel viewModel;

    public List<String> top10ListName = Arrays.asList("BTC", "ADA", "ETH", "BNB", "DOGE", "COSMOS", "CAKE", "WIN", "BLOK");

    Top10Adapter top10Adapter;

    CompositeDisposable compositeDisposable;

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
        compositeDisposable = new CompositeDisposable();
        setupViewPager2();
        getAllMarketFromDb();
        setupViewPager2TopGainLose();

        return fragmentHomeBinding.getRoot();
    }

    private void setupViewPager2TopGainLose() {
        TopGainerLoserAdapter topGainerLoserAdapter = new TopGainerLoserAdapter(this);
        fragmentHomeBinding.viewPagerTopGainLose.setAdapter(topGainerLoserAdapter);

        fragmentHomeBinding.viewPagerTopGainLose.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });

        new TabLayoutMediator(fragmentHomeBinding.tabLayoutFragmentHome, fragmentHomeBinding.viewPagerTopGainLose, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    tab.setText(getResources().getString(R.string.topgainer));
                    tab.setIcon(R.drawable.dropup_arrows);
                } else {
                    tab.setText(getResources().getString(R.string.toploser));
                    tab.setIcon(R.drawable.drop_arrows);
                }
            }
        }).attach();

    }

    private void getAllMarketFromDb() {
        Disposable disposable = viewModel.getAllMarketFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RoomAllMarket>() {
                    @Override
                    public void accept(RoomAllMarket roomAllMarket) throws Throwable {
                        AllCoinMarket allCoinMarket = roomAllMarket.getAllCoinMarket();

                        ArrayList<DataItem> top10List = new ArrayList<>();
                        for (int i = 0; i < allCoinMarket.getRootData().getCryptoCurrencyList().size(); i++) {
                            for (int j = 0; j < top10ListName.size(); j++) {
                                String coin_name = top10ListName.get(j);
                                if (allCoinMarket.getRootData().getCryptoCurrencyList().get(i).getSymbol().equals(coin_name)) {
                                    DataItem dataItem = allCoinMarket.getRootData().getCryptoCurrencyList().get(i);
                                    top10List.add(dataItem);
                                }
                            }
                        }

                        if (fragmentHomeBinding.top10Rv.getAdapter() != null) {
                            top10Adapter = (Top10Adapter) fragmentHomeBinding.top10Rv.getAdapter();
                            top10Adapter.updateItem(top10List);
                        } else {
                            top10Adapter = new Top10Adapter(top10List);
                            fragmentHomeBinding.top10Rv.setAdapter(top10Adapter);
                        }

                    }
                });
        compositeDisposable.add(disposable);
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