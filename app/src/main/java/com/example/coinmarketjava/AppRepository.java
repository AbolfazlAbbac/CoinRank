package com.example.coinmarketjava;

import com.example.coinmarketjava.http.ApiService;
import com.example.coinmarketjava.model.repository.AllCoinMarket;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.rxjava3.core.Observable;

public class AppRepository {
    ApiService apiService;

    public AppRepository(ApiService apiService) {
        this.apiService = apiService;
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
}
