package com.example.coinmarketjava.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.coinmarketjava.AppRepository;
import com.example.coinmarketjava.R;
import com.example.coinmarketjava.Roomdb.Entities.RoomAllMarket;
import com.example.coinmarketjava.model.repository.AllCoinMarket;

import java.util.ArrayList;
import java.util.concurrent.Future;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

@HiltViewModel
public class AppViewModel extends AndroidViewModel {
    MutableLiveData<ArrayList<Integer>> bannerData = new MutableLiveData<>();


    @Inject
    AppRepository appRepository;

    @Inject
    public AppViewModel(@NonNull Application application) {
        super(application);
        getBannerData();
    }

    public Future<Observable<AllCoinMarket>> marketFutureCall() {
        return appRepository.makeListFutureCall();
    }

    public void insertToRoomDb(AllCoinMarket allCoinMarket) {
        appRepository.insertAllMarket(allCoinMarket);
    }

    public Flowable<RoomAllMarket> getAllMarketFromDb() {
        return appRepository.getAllMarket();
    }


    private MutableLiveData<ArrayList<Integer>> getBannerData() {
        ArrayList<Integer> pics = new ArrayList<>();

        pics.add(R.drawable.banner);
        pics.add(R.drawable.banner_one);
        pics.add(R.drawable.banner_two);

        bannerData.postValue(pics);

        return bannerData;
    }

    public MutableLiveData<ArrayList<Integer>> getBannerLiveData() {
        return bannerData;
    }
}
