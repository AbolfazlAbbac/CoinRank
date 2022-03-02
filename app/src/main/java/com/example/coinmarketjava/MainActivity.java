package com.example.coinmarketjava;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.coinmarketjava.about.About;
import com.example.coinmarketjava.databinding.ActivityMainBinding;
import com.example.coinmarketjava.model.repository.AllCoinMarket;
import com.example.coinmarketjava.model.repository.CryptoDataMarket;
import com.example.coinmarketjava.viewModel.AppViewModel;
import com.google.android.material.navigation.NavigationView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
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
        Observable.interval(20, TimeUnit.SECONDS)
                .flatMap(item -> Observable.fromRunnable(new Runnable() {
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
                }))


                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void networkCheck() {
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@androidx.annotation.NonNull Network network) {
                callRequestCrypto();
                setupJsoup();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activityMainBinding.thereisnotconnection.setVisibility(View.GONE);
                    }
                });

            }

            @Override
            public void onLost(@androidx.annotation.NonNull Network network) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activityMainBinding.thereisnotconnection.setVisibility(View.VISIBLE);
                    }
                });

            }
        };
        if (!isNetworkAvailable(MainActivity.this)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activityMainBinding.thereisnotconnection.setVisibility(View.VISIBLE);
                }
            });
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        } else
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }


    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void callRequestCrypto() {
        Observable.interval(0, 20, TimeUnit.SECONDS)
                .flatMap(n -> appViewModel.marketFutureCall().get())
                .subscribeOn(Schedulers.io())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Throwable {
                        activityMainBinding.internetLoader.setVisibility(View.GONE);
                    }
                })
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
                        activityMainBinding.internetLoader.setVisibility(View.GONE);

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

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment, R.id.marketFragment, R.id.watchListFragment, R.id.about)
                .setOpenableLayout(activityMainBinding.drawerLayout)
                .build();

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, activityMainBinding.drawerLayout, R.string.Open_drawer, R.string.Close_drawer);

        activityMainBinding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationUI.setupWithNavController(activityMainBinding.navigationView, navController);
        Log.e("TAG", "smoothBottomBar: " + getSupportFragmentManager().findFragmentById(R.id.nav_host_container));
        activityMainBinding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@androidx.annotation.NonNull MenuItem item) {
                if (item.getItemId() == R.id.exit) {
                    finishAffinity();
                } else {
                    NavigationUI.onNavDestinationSelected(item, navController);
                    drawerLayout.closeDrawers();
                }

                return false;
            }
        });

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