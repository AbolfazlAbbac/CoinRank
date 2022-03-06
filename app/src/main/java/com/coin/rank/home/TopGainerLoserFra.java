package com.coin.rank.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.coin.rank.Roomdb.Entities.RoomAllMarket;
import com.coin.rank.Utils;
import com.coin.rank.model.repository.DataItem;
import com.coin.rank.viewModel.AppViewModel;
import com.example.rank.R;
import com.example.rank.databinding.FragmentTopGainerLoserBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TopGainerLoserFra extends Fragment implements TopGainLoseAdapterRv.OnClickListenerEvent {

    FragmentTopGainerLoserBinding fragmentTopGainerLoserBinding;
    AppViewModel viewModel;
    CompositeDisposable compositeDisposable;
    List<DataItem> data;
    int pos;
    TopGainerLoserAdapter topGainerLoserAdapter;
    TopGainLoseAdapterRv topGainLoseAdapterRv;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentTopGainerLoserBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_top_gainer_loser, container, false);

        Bundle args = getArguments();
        pos = args != null ? args.getInt("pos") : 1;

        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        compositeDisposable = new CompositeDisposable();

        setupRv(pos);
        return fragmentTopGainerLoserBinding.getRoot();
    }

    private void setupRv(int pos) {
        topGainerLoserAdapter = new TopGainerLoserAdapter(this);

        Disposable disposable = viewModel.getAllMarketFromDb(compositeDisposable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RoomAllMarket>() {
                    @Override
                    public void accept(RoomAllMarket roomAllMarket) throws Throwable {

                        viewModel.getAllDataItemFav().subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<List<DataItem>>() {
                                    @Override
                                    public void accept(List<DataItem> dataItems) throws Throwable {
                                        for (int i = 0; i < roomAllMarket.allCoinMarket.getRootData().getCryptoCurrencyList().size(); i++) {
                                            for (int k = 0; k < dataItems.size(); k++) {
                                                if (dataItems.get(k).getId() == roomAllMarket.allCoinMarket.getRootData().getCryptoCurrencyList().get(i).getId()) {
                                                    roomAllMarket.getAllCoinMarket().getRootData().getCryptoCurrencyList().get(i).setFav(true);
                                                }
                                            }
                                        }
                                    }
                                });
                        data = roomAllMarket.allCoinMarket.getRootData().getCryptoCurrencyList();

                        Collections.sort(data, new Comparator<DataItem>() {
                            @Override
                            public int compare(DataItem o1, DataItem o2) {
                                return Integer.valueOf((int) o1.getQuotes().get(0).getPercentChange24h()).compareTo((int) o2.getQuotes().get(0).getPercentChange24h());
                            }
                        });

                        ArrayList<DataItem> dataItems = new ArrayList<>();
                        if (pos == 0) {
                            for (int i = 0; i < 10; i++) {
                                int size = data.size();
                                Log.e("size abol", "accept: " + size);
                                dataItems.add(data.get(size - 1 - i));
                            }
                        } else if (pos == 1) {
                            for (int i = 0; i < 10; i++) {
                                dataItems.add(data.get(i));
                            }
                        }

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        fragmentTopGainerLoserBinding.rvTopGainerLoser.setLayoutManager(linearLayoutManager);

                        if (fragmentTopGainerLoserBinding.rvTopGainerLoser.getAdapter() == null) {
                            topGainLoseAdapterRv = new TopGainLoseAdapterRv(dataItems, TopGainerLoserFra.this);
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

    @Override
    public void clickEvent(View view, DataItem dataItem) {
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
