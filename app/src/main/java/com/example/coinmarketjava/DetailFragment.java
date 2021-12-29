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

import com.example.coinmarketjava.databinding.FragmentDetailBinding;
import com.example.coinmarketjava.model.repository.DataItem;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.text.DecimalFormat;


public class DetailFragment extends Fragment {
    FragmentDetailBinding fragmentDetailBinding;
    DataItem dataItem;
    String intervalChart = "D";
    MaterialButtonToggleGroup toggleGroup;

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
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        textDetailForEachCrypto();
        chartShow(dataItem);
        toggleGroup = fragmentDetailBinding.toggleGroupTimeFrameChart;
        toggleGroup.setSelectionRequired(true);
        toggleGroup.setSingleSelection(true);
        toggleGroup.check(R.id.monthlyButtonChart);


    }

    private void chartShow(DataItem dataItem) {

        WebView web = fragmentDetailBinding.chartWebView;
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
        web.loadUrl("https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + dataItem.getSymbol() +
                "USD&interval=1&hidesidetoolbar=M&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=" +
                "Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign" +
                "=chart&utm_term=BTCUSDT");

        fragmentDetailBinding.toggleGroupTimeFrameChart.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (checkedId == R.id.min1ButtonChart) {
                web.loadUrl("https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + dataItem.getSymbol() +
                        "USD&interval=1&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=" +
                        "Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign" +
                        "=chart&utm_term=BTCUSDT");
            }
            if (checkedId == R.id.min30ButtonChart) {
                web.loadUrl("https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + dataItem.getSymbol() +
                        "USD&interval=30&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=" +
                        "Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign" +
                        "=chart&utm_term=BTCUSDT");
            }
            if (checkedId == R.id.hours1ButtonChart) {
                web.loadUrl("https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + dataItem.getSymbol() +
                        "USD&interval=1H&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=" +
                        "Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign" +
                        "=chart&utm_term=BTCUSDT");
            }
            if (checkedId == R.id.hours4ButtonChart) {
                web.loadUrl("https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + dataItem.getSymbol() +
                        "USD&interval=4H&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=" +
                        "Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign" +
                        "=chart&utm_term=BTCUSDT");
            }
            if (checkedId == R.id.dailyButtonChart) {
                web.loadUrl("https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + dataItem.getSymbol() +
                        "USD&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=" +
                        "Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign" +
                        "=chart&utm_term=BTCUSDT");
            }
            if (checkedId == R.id.weeklyButtonChart) {
                web.loadUrl("https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + dataItem.getSymbol() +
                        "USD&interval=W&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=" +
                        "Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign" +
                        "=chart&utm_term=BTCUSDT");
            }
            if (checkedId == R.id.monthlyButtonChart) {
                web.loadUrl("https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + dataItem.getSymbol() +
                        "USD&interval=M&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=" +
                        "Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign" +
                        "=chart&utm_term=BTCUSDT");
            }

        });

    }


    private void textDetailForEachCrypto() {
        DecimalFormat myFormatter = new DecimalFormat("###,###.###");
        if (dataItem.getQuotes().get(0).getPercentChange24h() > 0) {
            fragmentDetailBinding.percentChangeTextDetailToolbar.setText(String.format("+%.2f", dataItem.getQuotes().get(0).getPercentChange24h()) + "%");
            fragmentDetailBinding.percentChangeTextDetailToolbar.setTextColor(getResources().getColor(R.color.Green));
        } else {
            fragmentDetailBinding.percentChangeTextDetailToolbar.setText(String.format("%.2f", dataItem.getQuotes().get(0).getPercentChange24h()) + "%");
            fragmentDetailBinding.percentChangeTextDetailToolbar.setTextColor(getResources().getColor(R.color.Red));
        }
        fragmentDetailBinding.priceDetailFragmentTv.setText(myFormatter.format(dataItem.getQuotes().get(0).getPrice()));
        fragmentDetailBinding.marketCapDetailFragmentTv.setText(myFormatter.format(dataItem.getQuotes().get(0).getMarketCap()));
        fragmentDetailBinding.low24hFragmentDetailTv.setText(myFormatter.format(dataItem.getLow24h()));
        fragmentDetailBinding.high24hFragmentDetailTv.setText(myFormatter.format(dataItem.getHigh24h()));
        if (dataItem.getQuotes().get(0).getDominance() > 0) {
            fragmentDetailBinding.marketDominanceFragmentDetailTv.setText(myFormatter.format(dataItem.getQuotes().get(0).getDominance()));
        } else {
            fragmentDetailBinding.marketDominanceFragmentDetailTv.setText(getResources().getString(R.string.no_data));
        }
        fragmentDetailBinding.rankCryptoDetailToolbar.setText(String.valueOf("#" + dataItem.getCmcRank()));
        fragmentDetailBinding.titleTextDetailToolbar.setText(dataItem.getName());
    }
}