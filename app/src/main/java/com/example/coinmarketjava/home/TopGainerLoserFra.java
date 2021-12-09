package com.example.coinmarketjava.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coinmarketjava.R;
import com.example.coinmarketjava.Roomdb.Entities.RoomAllMarket;
import com.example.coinmarketjava.databinding.FragmentTopGainerLoserBinding;
import com.example.coinmarketjava.model.repository.AllCoinMarket;
import com.example.coinmarketjava.model.repository.DataItem;
import com.example.coinmarketjava.viewModel.AppViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TopGainerLoserFra extends Fragment {

    FragmentTopGainerLoserBinding fragmentTopGainerLoserBinding;
    AppViewModel viewModel;
    CompositeDisposable compositeDisposable;
    List<DataItem> data;
    TopGainerLoserAdapter topGainerLoserAdapter;
    TopGainLoseAdapterRv topGainLoseAdapterRv;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentTopGainerLoserBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_top_gainer_loser, container, false);

        Bundle args = getArguments();
        int pos = args != null ? args.getInt("pos") : 1;

        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        compositeDisposable = new CompositeDisposable();
        setupRv(pos);

        return fragmentTopGainerLoserBinding.getRoot();
    }

    private void setupRv(int pos) {
        data = new ArrayList<>();
        topGainerLoserAdapter = new TopGainerLoserAdapter(this);
        Disposable disposable = viewModel.getAllMarketFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RoomAllMarket>() {
                    @Override
                    public void accept(RoomAllMarket roomAllMarket) throws Throwable {
                        AllCoinMarket allCoinMarket = roomAllMarket.getAllCoinMarket();
                        data = allCoinMarket.getRootData().getCryptoCurrencyList();


                        Collections.sort(data, new Comparator<DataItem>() {
                            @Override
                            public int compare(DataItem o1, DataItem o2) {
                                return Integer.valueOf((int) o1.getQuotes().get(0).getPercentChange24h()).compareTo((int) o2.getQuotes().get(0).getPercentChange24h());
                            }
                        });

                        ArrayList<DataItem> dataItems = new ArrayList<>();
                        if (pos == 0) {
                            for (int i = 0; i < 10; i++) {
                                dataItems.add(data.get(data.size() - 1 - i));
                            }
                        } else if (pos == 1) {
                            for (int i = 0; i < 10; i++) {
                                dataItems.add(data.get(i));
                            }
                        }

                        Log.i("dataok", "accept: " + dataItems.get(0).getSymbol());
                        Log.i("dataok", "accept: " + dataItems.get(1).getSymbol());
                        Log.i("dataok", "accept: " + dataItems.get(2).getSymbol());
                        Log.i("dataok", "accept: " + dataItems.get(3).getSymbol());

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        fragmentTopGainerLoserBinding.rvTopGainerLoser.setLayoutManager(linearLayoutManager);

                        if (fragmentTopGainerLoserBinding.rvTopGainerLoser.getAdapter() == null) {
                            topGainLoseAdapterRv = new TopGainLoseAdapterRv(dataItems);
                            fragmentTopGainerLoserBinding.rvTopGainerLoser.setAdapter(topGainLoseAdapterRv);
                        } else {
                            topGainLoseAdapterRv = (TopGainLoseAdapterRv) fragmentTopGainerLoserBinding.rvTopGainerLoser.getAdapter();
                            topGainLoseAdapterRv.update(dataItems);
                        }
                        fragmentTopGainerLoserBinding.loaderTopGainerLoser.setVisibility(View.GONE);
                    }

                });

        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}