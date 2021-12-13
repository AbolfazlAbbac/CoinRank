package com.example.coinmarketjava;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.coinmarketjava.databinding.ActivityMainBinding;
import com.example.coinmarketjava.home.Top10Adapter;
import com.example.coinmarketjava.model.repository.AllCoinMarket;
import com.example.coinmarketjava.model.repository.CryptoDataMarket;
import com.example.coinmarketjava.viewModel.AppViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;

    @Inject
    ConnectivityManager connectivityManager;

    @Inject
    NetworkRequest networkRequest;

    NavHostFragment navHostFragment;

    NavController navController;

    AppBarConfiguration appBarConfiguration;

    public DrawerLayout drawerLayout;

    AppViewModel appViewModel;

    CompositeDisposable compositeDisposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        drawerLayout = activityMainBinding.drawerLayout;
        compositeDisposable = new CompositeDisposable();

        smoothBottomBar();
        setupViewModel();
        networkCheck();


    }

    private void setupJsoup() {
        Completable.fromRunnable(new Runnable() {
            @Override
            public void run() {
                try {
                    Document pageSrc = Jsoup.connect("https://coinmarketcap.com/").get();
                    Elements scrapingMarketData = pageSrc.getElementsByClass("cmc-link");

                    String exChange = scrapingMarketData.get(1).text();
                    String marketCap = scrapingMarketData.get(2).text();
                    String value24H = scrapingMarketData.get(3).text();
                    String[] dominance = scrapingMarketData.get(4).text().split(" ");

                    String dominance_Btc = dominance[1];
                    String dominance_Eth = dominance[3];

                    CryptoDataMarket cryptoDataMarket = new CryptoDataMarket(marketCap, value24H, dominance_Btc, dominance_Eth);
                    appViewModel.insertDataToDb(cryptoDataMarket);

                    Log.e("marketCap", "run: " + marketCap);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

    }

    private void networkCheck() {
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@androidx.annotation.NonNull Network network) {
                callRequestCrypto();
                setupJsoup();

            }

            @Override
            public void onLost(@androidx.annotation.NonNull Network network) {
                Snackbar.make(activityMainBinding.navHostContainer, "Internet is Lost", Snackbar.LENGTH_LONG).show();
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        } else
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }


    private void callRequestCrypto() {
        Observable.interval(20, TimeUnit.SECONDS)
                .flatMap(n -> appViewModel.marketFutureCall().get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AllCoinMarket>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
//                        Log.e("MainActivity", "Subscribe-> ok: ");

                    }

                    @Override
                    public void onNext(@NonNull AllCoinMarket allCoinMarket) {
                        appViewModel.insertToRoomDb(allCoinMarket);
//                        Log.e("MainActivity", "getCrypto - onNext -> ok: ");

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("Error,MainActivity,room", "onError: ");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setupViewModel() {
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
    }


    private void smoothBottomBar() {
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_container);
        navController = navHostFragment.getNavController();

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment, R.id.marketFragment, R.id.watchListFragment)
                .setOpenableLayout(activityMainBinding.drawerLayout)
                .build();

        NavigationUI.setupWithNavController(activityMainBinding.navigationView, navController);


        setupSmoothBottomBar();
    }

    private void setupSmoothBottomBar() {
        PopupMenu popupMenu = new PopupMenu(this, null);
        popupMenu.inflate(R.menu.menu_main);

        Menu menu = popupMenu.getMenu();

        activityMainBinding.bottomNavigation.setupWithNavController(menu, navController);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}