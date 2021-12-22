package com.example.coinmarketjava;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.Utils;
import com.example.coinmarketjava.databinding.FragmentDetailBinding;
import com.example.coinmarketjava.model.repository.DataItem;

import java.text.DecimalFormat;


public class DetailFragment extends Fragment {
    FragmentDetailBinding fragmentDetailBinding;
    DataItem dataItem;

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
        DecimalFormat myFormatter = new DecimalFormat("###,###.###");


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