package com.example.coinmarketjava.market;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coinmarketjava.Utils;
import com.example.coinmarketjava.MainActivity;
import com.example.coinmarketjava.R;
import com.example.coinmarketjava.Roomdb.Entities.RoomDataMarket;
import com.example.coinmarketjava.databinding.FragmentMarketBinding;
import com.example.coinmarketjava.market.adapter.AdapterMarketFragment;
import com.example.coinmarketjava.model.repository.AllCoinMarket;
import com.example.coinmarketjava.model.repository.CryptoDataMarket;
import com.example.coinmarketjava.model.repository.DataItem;
import com.example.coinmarketjava.viewModel.AppViewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MarketFragment extends Fragment implements AdapterMarketFragment.OnClickListenerEvent {

    FragmentMarketBinding fragmentMarketBinding;
    MainActivity mainActivity;
    AppViewModel appViewModel;
    AdapterMarketFragment adapterMarketFragment;
    CompositeDisposable compositeDisposable;
    ArrayList<DataItem> filtered;
    List<DataItem> dataItems;
    String searchBoxText = "";
    NavigationView navigationView;

    @Override
    public void onAttach(@NonNull Context context) {
        mainActivity = (MainActivity) context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationView = getActivity().findViewById(R.id.navigationView);
        navigationView.setCheckedItem(R.id.marketFragment);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentMarketBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_market, container, false);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        compositeDisposable = new CompositeDisposable();
        filtered = new ArrayList<>();

        setupAllCrypto();
        setupSearch();
        setupDataCrypto();


        return fragmentMarketBinding.getRoot();

    }

    private void setupDataCrypto() {
        Disposable disposable = appViewModel.getAllDataFromDb().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<RoomDataMarket>() {
                    @Override
                    public void accept(RoomDataMarket roomDataMarket) throws Throwable {
                        CryptoDataMarket cryptoDataMarket = roomDataMarket.getCryptoDataMarket();


                        fragmentMarketBinding.marketCapNumberTv.setText(cryptoDataMarket.getMarketCap());
                        fragmentMarketBinding.dailyVolumeNumberTv.setText(cryptoDataMarket.getVolume24h());
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void setupSearch() {
        fragmentMarketBinding.searchBoxMarketFragment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchBoxText = charSequence.toString();
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void filter(String name) {
        filtered.clear();
        for (DataItem item : dataItems) {
            if (item.getSymbol().toLowerCase().contains(name.toLowerCase()) || item.getName().toLowerCase().contains(name.toLowerCase())) {
                filtered.add(item);
            }
        }


        adapterMarketFragment.update(filtered);
        adapterMarketFragment.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkNull();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkNull();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkNull();
            }

            void checkNull() {
                if (adapterMarketFragment.getItemCount() == 0) {
                    fragmentMarketBinding.itemNotFoundMarketFragmentTv.setVisibility(View.VISIBLE);
                } else
                    fragmentMarketBinding.itemNotFoundMarketFragmentTv.setVisibility(View.GONE);
            }
        });
    }

    private void setupAllCrypto() {
        appViewModel.dataItemList.observe(requireActivity(), dataItems1 -> {
            dataItems = dataItems1;

            if (fragmentMarketBinding.marketFragmentRv.getAdapter() == null) {
                adapterMarketFragment = new AdapterMarketFragment((ArrayList<DataItem>) dataItems1, MarketFragment.this);
                fragmentMarketBinding.marketFragmentRv.setAdapter(adapterMarketFragment);
            } else {
                adapterMarketFragment = (AdapterMarketFragment) fragmentMarketBinding.marketFragmentRv.getAdapter();
                if (searchBoxText.isEmpty()) {
                    adapterMarketFragment.update((ArrayList<DataItem>) dataItems1);
                } else {
                    for (int i = 0; i < dataItems1.size(); i++) {
                        for (int j = 0; j < filtered.size(); j++) {
                            if (dataItems1.get(i).getSymbol().equals(filtered.get(j).getSymbol())) {
                                filtered.set(j, dataItems1.get(i));
                            }
                        }
                    }
                    adapterMarketFragment.update(filtered);
                }
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar(view);
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


    @Override
    public void itemClick(View view, DataItem dataItem) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Utils.KEY_SEND_DATA, dataItem);
                Navigation.findNavController(view).navigate(R.id.action_marketFragment_to_detailFragment, bundle);
            }
        });
    }

}