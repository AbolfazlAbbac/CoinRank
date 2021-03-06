package com.coin.rank.home;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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

import com.coin.rank.MainActivity;
import com.coin.rank.Utils;
import com.coin.rank.model.repository.DataItem;
import com.coin.rank.viewModel.AppViewModel;
import com.example.rank.R;
import com.example.rank.databinding.FragmentHomeBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

@AndroidEntryPoint
public class HomeFragment extends Fragment implements Top10Adapter.OnClickEvent {
    FragmentHomeBinding fragmentHomeBinding;

    MainActivity mainActivity;

    AppViewModel viewModel;

    public List<String> top10ListName = Arrays.asList("BTC", "ETH", "BNB", "SOL", "XRP", "LUNA", "ADA", "AVAX", "DOGE");

    Top10Adapter top10Adapter;

    CompositeDisposable compositeDisposable;

    @Override
    public void onAttach(@NonNull Context context) {
        mainActivity = (MainActivity) context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//         Inflate the layout for this fragment
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        NavigationView navigationView = requireActivity().findViewById(R.id.navigationView);
        navigationView.setCheckedItem(R.id.homeFragment);
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        compositeDisposable = new CompositeDisposable();
        setupViewPager2();
        getAllMarketFromDb();
        setupViewPager2TopGainLose();

        fragmentHomeBinding.swipedownMain.setOnRefreshListener(() -> {
            mainActivity.callRequestCrypto();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fragmentHomeBinding.swipedownMain.setRefreshing(false);
                }
            }, 2000);
        });

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
        viewModel.dataItemList.observe(requireActivity(), dataItems -> {


            ArrayList<DataItem> top10List = new ArrayList<>();
            for (int i = 0; i < dataItems.size(); i++) {
                for (int j = 0; j < top10ListName.size(); j++) {
                    String coin_name = top10ListName.get(j);
                    if (dataItems.get(i).getSymbol().equals(coin_name)) {
                        DataItem dataItem = dataItems.get(i);
                        top10List.add(dataItem);
                    }
                }
            }

            if (fragmentHomeBinding.top10Rv.getAdapter() != null) {
                top10Adapter = (Top10Adapter) fragmentHomeBinding.top10Rv.getAdapter();
                top10Adapter.updateItem(top10List);
            } else {
                top10Adapter = new Top10Adapter(top10List, HomeFragment.this);
                fragmentHomeBinding.top10Rv.setAdapter(top10Adapter);
            }
        });
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

    @Override
    public void onClick(View view, DataItem dataItem) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Utils.KEY_SEND_DATA, dataItem);

                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_detailFragment, bundle);
            }
        });
    }
}