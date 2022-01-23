package com.example.coinmarketjava.viewModel;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.coinmarketjava.AppRepository;
import com.example.coinmarketjava.R;
import com.example.coinmarketjava.Roomdb.Entities.RoomAllMarket;
import com.example.coinmarketjava.Roomdb.Entities.RoomDataMarket;
import com.example.coinmarketjava.model.repository.AllCoinMarket;
import com.example.coinmarketjava.model.repository.CryptoDataMarket;
import com.example.coinmarketjava.model.repository.DataItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class AppViewModel extends AndroidViewModel {
    MutableLiveData<ArrayList<Integer>> bannerData = new MutableLiveData<>();
    public MutableLiveData<List<DataItem>> dataItemList = new MutableLiveData<>();
    public MutableLiveData<RoomAllMarket> dataItemTopLose = new MutableLiveData<>();


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

    public Flowable<RoomAllMarket> getAllMarketFromDb(CompositeDisposable compositeDisposable) {
        ArrayList<Integer> integers = new ArrayList<>();
        return appRepository.getAllMarket().flatMap(roomAllMarket -> {

            dataItemList.postValue(roomAllMarket.getAllCoinMarket().getRootData().getCryptoCurrencyList());
            dataItemTopLose.postValue(roomAllMarket);

            appRepository.getAllFav().observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new SingleObserver<List<DataItem>>() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<DataItem> dataItems) {
                            for (int i = 0; i < roomAllMarket.getAllCoinMarket().getRootData().getCryptoCurrencyList().size(); i++) {
                                integers.add(roomAllMarket.getAllCoinMarket().getRootData().getCryptoCurrencyList().get(i).getId());
                            }


                            for (int i = 0; i < 700; i++) {
                                for (int k = 0; k < dataItems.size(); k++) {
                                    if (dataItems.get(k).getId() == integers.get(i)) {
                                        roomAllMarket.getAllCoinMarket().getRootData().getCryptoCurrencyList().get(i).setFav(true);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                        }
                    });

            integers.clear();

            return appRepository.getAllMarket();
        });
    }

    public Flowable<RoomDataMarket> getAllDataFromDb() {
        return appRepository.getAllData();
    }

    public void insertDataToDb(CryptoDataMarket cryptoDataMarket) {
        appRepository.insertCryptoDataInDb(new RoomDataMarket(cryptoDataMarket));
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


    public void addFav(DataItem dataItem) {
        appRepository.addToFav(dataItem).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        dataItem.setFav(true);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }
                });
    }

    public void deleteItemFav(DataItem dataItem) {
        appRepository.deleteFromFav(dataItem).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        dataItem.setFav(false);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }
                });
    }

    public Single<List<DataItem>> getAllDataItemFav() {
        return appRepository.getAllFav();
    }

}
