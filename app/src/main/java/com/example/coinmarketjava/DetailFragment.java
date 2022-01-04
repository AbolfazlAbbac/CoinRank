package com.example.coinmarketjava;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.coinmarketjava.Roomdb.Entities.RoomDataItemsFav;
import com.example.coinmarketjava.databinding.FragmentDetailBinding;
import com.example.coinmarketjava.model.repository.DataItem;
import com.example.coinmarketjava.viewModel.AppViewModel;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class DetailFragment extends Fragment {
    FragmentDetailBinding fragmentDetailBinding;
    DataItem dataItem;
    MaterialButtonToggleGroup toggleGroup;
    WebView web;
    AppViewModel appViewModel;
    boolean isExpandedPercent = false;
    boolean isExpandedVolume = false;
    CompositeDisposable compositeDisposable;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_menu_toolbar, menu);
        MenuItem item = menu.findItem(R.id.favorite);
        if (item != null) {
            item.setVisible(true);
        }
        if (dataItem.isFav()) {
            item.setIcon(R.drawable.ic_baseline_star_rate_24);
        } else {
            item.setIcon(R.drawable.ic_baseline_star_outline_24);
        }
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                dataItem.setFav(!dataItem.isFav());
                if (dataItem.isFav()) {
                    menuItem.setIcon(R.drawable.ic_baseline_star_rate_24);
                    appViewModel.addFav(dataItem);
                } else {
                    menuItem.setIcon(R.drawable.ic_baseline_star_outline_24);
                    appViewModel.deleteItemFav(dataItem);
                }
                return true;
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        compositeDisposable = new CompositeDisposable();
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        dataItem = getArguments().getParcelable(Utils.KEY_SEND_DATA);
        Log.i("TAG", "onViewCreated: " + dataItem.getSymbol());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        Toolbar toolbar = fragmentDetailBinding.toolbarDetail;
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return fragmentDetailBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //binding data in textViews
        textDetailForEachCrypto();

        //show Charts
        chartShow();

        //setting in toggle button
        toggleGroup = fragmentDetailBinding.toggleGroupTimeFrameChart;
        toggleGroup.setSelectionRequired(true);
        toggleGroup.setSingleSelection(true);
        toggleGroup.check(R.id.dailyButtonChart);

        //setupDropDownArrow
        setupDropDownArrow();

    }

    private void setupDropDownArrow() {
        fragmentDetailBinding.iconDropDownPercentChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpandedPercent = !isExpandedPercent;
                if (isExpandedPercent) {
                    fragmentDetailBinding.conExpandedPercent.setVisibility(View.VISIBLE);
                    fragmentDetailBinding.iconDropDownPercentChange.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
                } else {
                    fragmentDetailBinding.conExpandedPercent.setVisibility(View.GONE);
                    fragmentDetailBinding.iconDropDownPercentChange.setImageResource(R.drawable.ic_baseline_arrow_right_24);
                }
            }
        });
        fragmentDetailBinding.iconDropDownVolumeChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpandedVolume = !isExpandedVolume;
                if (isExpandedVolume) {
                    fragmentDetailBinding.conExpandedVolume.setVisibility(View.VISIBLE);
                    fragmentDetailBinding.iconDropDownVolumeChange.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
                } else {
                    fragmentDetailBinding.conExpandedVolume.setVisibility(View.GONE);
                    fragmentDetailBinding.iconDropDownVolumeChange.setImageResource(R.drawable.ic_baseline_arrow_right_24);
                }
            }
        });
    }

    private void chartShow() {

        web = fragmentDetailBinding.chartWebView;
        web.getSettings().setJavaScriptEnabled(true);
        web.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                fragmentDetailBinding.webViewError.setVisibility(View.VISIBLE);
                fragmentDetailBinding.webViewErrorText.setVisibility(View.VISIBLE);
                super.onReceivedError(view, request, error);
            }
        });

        //Method Load Chart
        loadCharTimeFrame("D");

        //Change Time Frame with Toggle Button
        fragmentDetailBinding.toggleGroupTimeFrameChart.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (checkedId == R.id.min1ButtonChart) {
                loadCharTimeFrame("1");

            }
            if (checkedId == R.id.min30ButtonChart) {
                loadCharTimeFrame("30");

            }
            if (checkedId == R.id.hours1ButtonChart) {
                loadCharTimeFrame("1H");

            }
            if (checkedId == R.id.hours4ButtonChart) {
                loadCharTimeFrame("4H");

            }
            if (checkedId == R.id.dailyButtonChart) {
                loadCharTimeFrame("D");

            }
            if (checkedId == R.id.weeklyButtonChart) {
                loadCharTimeFrame("W");

            }
            if (checkedId == R.id.monthlyButtonChart) {
                loadCharTimeFrame("M");
            }

        });


    }

    public void loadCharTimeFrame(String time) {
        web.loadUrl("https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + dataItem.getSymbol() +
                "USD&interval=" + time + "&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=" +
                "Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign" +
                "=chart&utm_term=BTCUSDT");
    }


    private void textDetailForEachCrypto() {
        DecimalFormat myFormatter = new DecimalFormat("###,###.###");
        DecimalFormat formatDollar = new DecimalFormat("$" + "###,###.###");
        if (dataItem.getQuotes().get(0).getPercentChange24h() > 0) {
            fragmentDetailBinding.percentChangeTextDetailToolbar.setText(String.format("+%.2f", dataItem.getQuotes().get(0).getPercentChange24h()) + "%");
            fragmentDetailBinding.percentChangeTextDetailToolbar.setTextColor(getResources().getColor(R.color.Green));
        } else {
            fragmentDetailBinding.percentChangeTextDetailToolbar.setText(String.format("%.2f", dataItem.getQuotes().get(0).getPercentChange24h()) + "%");
            fragmentDetailBinding.percentChangeTextDetailToolbar.setTextColor(getResources().getColor(R.color.Red));
        }

        fragmentDetailBinding.volume24hFragmentDetailTv.setText(formatDollar.format(dataItem.getQuotes().get(0).getVolume24h()));
        fragmentDetailBinding.volume7dFragmentDetailTv.setText(formatDollar.format(dataItem.getQuotes().get(0).getVolume7d()));
        fragmentDetailBinding.volume30dFragmentDetailTv.setText(formatDollar.format(dataItem.getQuotes().get(0).getVolume30d()));

        if (dataItem.getQuotes().get(0).getPercentChange24h() > 0) {
            fragmentDetailBinding.percentChange24hFragmentDetailTv.setText(String.format("+%.2f", dataItem.getQuotes().get(0).getPercentChange24h()) + "%");
            fragmentDetailBinding.percentChange24hFragmentDetailTv.setTextColor(getResources().getColor(R.color.Green));
        } else {
            fragmentDetailBinding.percentChange24hFragmentDetailTv.setText(String.format("%.2f", dataItem.getQuotes().get(0).getPercentChange24h()) + "%");
            fragmentDetailBinding.percentChange24hFragmentDetailTv.setTextColor(getResources().getColor(R.color.Red));
        }
        if (dataItem.getQuotes().get(0).getPercentChange7d() > 0) {
            fragmentDetailBinding.percentChange7dFragmentDetailTv.setText(String.format("+%.2f", dataItem.getQuotes().get(0).getPercentChange7d()) + "%");
            fragmentDetailBinding.percentChange7dFragmentDetailTv.setTextColor(getResources().getColor(R.color.Green));
        } else {
            fragmentDetailBinding.percentChange7dFragmentDetailTv.setText(String.format("%.2f", dataItem.getQuotes().get(0).getPercentChange7d()) + "%");
            fragmentDetailBinding.percentChange7dFragmentDetailTv.setTextColor(getResources().getColor(R.color.Red));
        }
        if (dataItem.getQuotes().get(0).getPercentChange30d() > 0) {
            fragmentDetailBinding.percentChange30dFragmentDetailTv.setText(String.format("+%.2f", dataItem.getQuotes().get(0).getPercentChange30d()) + "%");
            fragmentDetailBinding.percentChange30dFragmentDetailTv.setTextColor(getResources().getColor(R.color.Green));
        } else {
            fragmentDetailBinding.percentChange30dFragmentDetailTv.setText(String.format("%.2f", dataItem.getQuotes().get(0).getPercentChange30d()) + "%");
            fragmentDetailBinding.percentChange30dFragmentDetailTv.setTextColor(getResources().getColor(R.color.Red));
        }
        if (dataItem.getQuotes().get(0).getPercentChange60d() > 0) {
            fragmentDetailBinding.percentChange2mFragmentDetailTv.setText(String.format("+%.2f", dataItem.getQuotes().get(0).getPercentChange60d()) + "%");
            fragmentDetailBinding.percentChange2mFragmentDetailTv.setTextColor(getResources().getColor(R.color.Green));
        } else {
            fragmentDetailBinding.percentChange2mFragmentDetailTv.setText(String.format("%.2f", dataItem.getQuotes().get(0).getPercentChange60d()) + "%");
            fragmentDetailBinding.percentChange2mFragmentDetailTv.setTextColor(getResources().getColor(R.color.Red));
        }
        if (dataItem.getQuotes().get(0).getPercentChange90d() > 0) {
            fragmentDetailBinding.percentChange3mFragmentDetailTv.setText(String.format("+%.2f", dataItem.getQuotes().get(0).getPercentChange90d()) + "%");
            fragmentDetailBinding.percentChange3mFragmentDetailTv.setTextColor(getResources().getColor(R.color.Green));
        } else {
            fragmentDetailBinding.percentChange3mFragmentDetailTv.setText(String.format("%.2f", dataItem.getQuotes().get(0).getPercentChange90d()) + "%");
            fragmentDetailBinding.percentChange3mFragmentDetailTv.setTextColor(getResources().getColor(R.color.Red));
        }


        fragmentDetailBinding.priceDetailFragmentTv.setText(formatDollar.format(dataItem.getQuotes().get(0).getPrice()));
        fragmentDetailBinding.marketCapDetailFragmentTv.setText(formatDollar.format(dataItem.getQuotes().get(0).getMarketCap()));
        fragmentDetailBinding.low24hFragmentDetailTv.setText(formatDollar.format(dataItem.getLow24h()));
        fragmentDetailBinding.high24hFragmentDetailTv.setText(formatDollar.format(dataItem.getHigh24h()));
        if (dataItem.getQuotes().get(0).getDominance() > 0) {
            fragmentDetailBinding.marketDominanceFragmentDetailTv.setText(myFormatter.format(dataItem.getQuotes().get(0).getDominance()));
        } else {
            fragmentDetailBinding.marketDominanceFragmentDetailTv.setText(getResources().getString(R.string.no_data));
        }
        fragmentDetailBinding.rankCryptoDetailToolbar.setText(String.valueOf("#" + dataItem.getCmcRank()));
        fragmentDetailBinding.titleTextDetailToolbar.setText(dataItem.getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}