package com.coin.rank;

import android.util.Log;

import com.coin.rank.Roomdb.Entities.RoomAllMarket;
import com.coin.rank.Roomdb.Entities.RoomDataMarket;
import com.coin.rank.Roomdb.RoomDao;
import com.coin.rank.http.ApiService;
import com.coin.rank.model.repository.AllCoinMarket;
import com.coin.rank.model.repository.DataItem;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AppRepository {
    ApiService apiService;
    RoomDao roomDao;

    public AppRepository(ApiService apiService, RoomDao roomDao) {
        this.apiService = apiService;
        this.roomDao = roomDao;
    }

    public Future<Observable<AllCoinMarket>> makeListFutureCall() {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();

        final Callable<Observable<AllCoinMarket>> FutureCallable = () -> apiService.makeLatestCryptoList();

        final Future<Observable<AllCoinMarket>> futureObservable = new Future<Observable<AllCoinMarket>>() {
            @Override
            public boolean cancel(boolean b) {
                if (b) {
                    executorService.shutdown();
                }
                return false;
            }

            @Override
            public boolean isCancelled() {
                return executorService.isShutdown();
            }

            @Override
            public boolean isDone() {
                return executorService.isTerminated();
            }

            @Override
            public Observable<AllCoinMarket> get() throws ExecutionException, InterruptedException {
                return executorService.submit(FutureCallable).get();
            }

            @Override
            public Observable<AllCoinMarket> get(long timeout, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
                return executorService.submit(FutureCallable).get(timeout, timeUnit);
            }

        };
        return futureObservable;
    }

    public void insertAllMarket(AllCoinMarket allCoinMarket) {
        Completable.fromAction(() -> roomDao.insertToDb(new RoomAllMarket(allCoinMarket)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e("AppRepository", "Subscribe -> ok: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.i("Add to db", "onComplete: ");
                        Log.e("AppRepository", "onComplete -> ok: ");

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("exist error add to db", "onError: ");
                    }
                });
    }

    public Flowable<RoomAllMarket> getAllMarket() {
        return roomDao.getAllMarketFromDb();

    }

    public Flowable<RoomDataMarket> getAllData() {
        return roomDao.getAllDataMarketFromDb();
    }

    public void insertCryptoDataInDb(RoomDataMarket roomDataMarket) {
        Completable.fromAction(() -> roomDao.insertDataToDb(roomDataMarket))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    public Completable addToFav(DataItem roomDataItemsFav) {
        return roomDao.insert(roomDataItemsFav);
    }

    public Completable deleteFromFav(DataItem dataItem) {
        return roomDao.deleteItemsFav(dataItem);

    }

    public Single<List<DataItem>> getAllFav() {
        return roomDao.getAllDataItemsFav();
    }
}
