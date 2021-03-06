package com.coin.rank.watchlist;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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

import com.coin.rank.MainActivity;
import com.coin.rank.Utils;
import com.example.rank.R;
import com.example.rank.databinding.FragmentWatchListBinding;
import com.coin.rank.model.repository.DataItem;
import com.coin.rank.viewModel.AppViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class WatchListFragment extends Fragment implements WatchListAdapter.onClickListener {
    FragmentWatchListBinding binding;
    MainActivity mainActivity;
    AppViewModel appViewModel;
    CompositeDisposable compositeDisposable;
    WatchListAdapter watchListAdapter;
    int items = 0;

    @Override
    public void onAttach(@NonNull Context context) {
        mainActivity = (MainActivity) context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationView navigationView = requireActivity().findViewById(R.id.navigationView);
        navigationView.setCheckedItem(R.id.watchListFragment);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_watch_list, container, false);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        compositeDisposable = new CompositeDisposable();
        favoriteItems();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        setupToolbar(view);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (items == 0) {
                    binding.swipedownWatchList.setEnabled(false);
                    binding.swipedownWatchList.setRefreshing(false);
                    Log.e("ItemsSize", "onViewCreated: " + items);
                } else {
                    binding.swipedownWatchList.setOnRefreshListener(() -> {
                        Log.e("ItemsSize1", "onViewCreated: " + items);
                        binding.swipedownWatchList.setEnabled(true);
                        mainActivity.callRequestCrypto();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                binding.swipedownWatchList.setRefreshing(false);
                            }
                        }, 2000);
                    });
                }
            }
        },200);

        super.onViewCreated(view, savedInstanceState);
    }

    private void favoriteItems() {
        appViewModel.getAllDataItemFav()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<DataItem>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<DataItem> dataItems) {
                        items = dataItems.size();

                        appViewModel.getAllDataItemFav().subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<List<DataItem>>() {
                                    @Override
                                    public void accept(List<DataItem> dataItems1) throws Throwable {
                                        for (int i = 0; i < dataItems.size(); i++) {
                                            for (int k = 0; k < dataItems1.size(); k++) {
                                                if (dataItems1.get(k).getId() == dataItems.get(i).getId()) {
                                                    dataItems.get(i).setFav(true);
                                                }
                                            }
                                        }
                                    }
                                });


                        if (dataItems.size() > 0) {
                            binding.conEmptyStateWatchList.setVisibility(View.GONE);
                        } else {
                            binding.conEmptyStateWatchList.setVisibility(View.VISIBLE);
                        }


                        if (binding.rvWatchListFragment.getAdapter() == null) {
                            watchListAdapter = new WatchListAdapter(dataItems, WatchListFragment.this);
                            binding.rvWatchListFragment.setAdapter(watchListAdapter);
                        } else {
                            binding.rvWatchListFragment.setAdapter(watchListAdapter);
                            watchListAdapter.update(dataItems);
                        }

                        Log.e("ItemFav", "onSuccess: " + dataItems.size());
                        for (DataItem dataItemList : dataItems) {
                            Log.e("ItemsSize", "Source: " + items);
                        }

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e("ItemFav", "onError: " + e.toString());
                    }
                });

    }

    private void setupToolbar(View view) {

        NavController navController = Navigation.findNavController(view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.watchListFragment)
                .setOpenableLayout(mainActivity.drawerLayout)
                .build();

        Toolbar toolbar = view.findViewById(R.id.toolbar);

        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        Utils.ToolbarCustom(navController, getString(R.string.watchlist), R.id.watchListFragment, toolbar);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void onClickItem(DataItem dataItem) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Utils.KEY_SEND_DATA, dataItem);
        Navigation.findNavController(requireView()).navigate(R.id.action_watchListFragment_to_detailFragment, bundle);
    }

    @Override
    public void removeAndAddFav(DataItem dataItem) {
        if (dataItem.isFav())
            appViewModel.deleteItemFav(dataItem);
        else
            appViewModel.addFav(dataItem);
    }
}