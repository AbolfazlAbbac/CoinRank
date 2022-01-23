package com.example.coinmarketjava.market.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coinmarketjava.R;
import com.example.coinmarketjava.databinding.ItemMarketfragmentBinding;
import com.example.coinmarketjava.model.repository.DataItem;

import java.util.ArrayList;

public class AdapterMarketFragment extends RecyclerView.Adapter<AdapterMarketFragment.ViewHolder> {

    LayoutInflater inflater;

    ArrayList<DataItem> dataItems;

    public OnClickListenerEvent onClickListenerEvent;

    public AdapterMarketFragment(ArrayList<DataItem> dataItems, OnClickListenerEvent onClickListenerEvent) {
        this.dataItems = dataItems;
        this.onClickListenerEvent = onClickListenerEvent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        ItemMarketfragmentBinding view = DataBindingUtil.inflate(inflater, R.layout.item_marketfragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(dataItems.get(position));
        onClickListenerEvent.itemClick(holder.itemView, dataItems.get(position));
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemMarketfragmentBinding binding;

        public ViewHolder(@NonNull ItemMarketfragmentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(DataItem dataItem) {

            setupChart(dataItem);
            setupIconCrypto(dataItem);
            setupColorPriceAndPercent(dataItem);

            binding.nameMarketFragment.setText(dataItem.getName());
            binding.codeMarketFragment.setText(dataItem.getSymbol());
            binding.counterMarketFragmentTv.setText(String.valueOf(dataItem.getCmcRank()));

            setPrice(dataItem);

            if (dataItem.getQuotes().get(0).getPercentChange24h() > 0) {
                binding.percentChangeMarketFragment.setText(String.format("+%.2f", dataItem.getQuotes().get(0).getPercentChange24h()) + "%");
                binding.chartMarketFragment.setColorFilter(Color.GREEN);
                binding.arrowMarketFragment.setImageResource(R.drawable.dropup_arrows);
            } else {
                binding.percentChangeMarketFragment.setText(String.format("%.2f", dataItem.getQuotes().get(0).getPercentChange24h()) + "%");
                binding.chartMarketFragment.setColorFilter(Color.RED);
                binding.arrowMarketFragment.setImageResource(R.drawable.drop_arrows);
            }

            binding.executePendingBindings();
        }

        private void setPrice(@NonNull DataItem dataItem) {
            if (dataItem.getQuotes().get(0).getPrice() < 1) {
                binding.priceMarketFragment.setText("$" + String.format("%.6f", dataItem.getQuotes().get(0).getPrice()));
            } else if (dataItem.getQuotes().get(0).getPrice() < 10) {
                binding.priceMarketFragment.setText("$" + String.format("%.4f", dataItem.getQuotes().get(0).getPrice()));
            } else {
                binding.priceMarketFragment.setText("$" + String.format("%.2f", dataItem.getQuotes().get(0).getPrice()));
            }
        }

        private void setupColorPriceAndPercent(@NonNull DataItem dataItem) {
            if (dataItem.getQuotes().get(0).getPercentChange24h() > 0) {
                binding.priceMarketFragment.setTextColor(Color.GREEN);
                binding.percentChangeMarketFragment.setTextColor(Color.GREEN);
            } else if (dataItem.getQuotes().get(0).getPercentChange24h() < 0) {
                binding.priceMarketFragment.setTextColor(Color.RED);
                binding.percentChangeMarketFragment.setTextColor(Color.RED);
            }
        }

        private void setupIconCrypto(@NonNull DataItem dataItem) {
            Glide.with(binding.getRoot().getContext())
                    .load("https://s2.coinmarketcap.com/static/img/coins/32x32/" + dataItem.getId() + ".png")
                    .thumbnail(Glide.with(binding.getRoot().getContext()).load(R.drawable.loader))
                    .into(binding.iconCryptoMarketFragment);
        }

        private void setupChart(@NonNull DataItem dataItem) {
            Glide.with(binding.getRoot().getContext())
                    .load("https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/" + dataItem.getId() + ".png")
                    .thumbnail(Glide.with(binding.getRoot().getContext()).load(R.drawable.loader))
                    .into(binding.chartMarketFragment);
        }
    }

    public void update(ArrayList<DataItem> newData) {
        dataItems.clear();
        dataItems.addAll(newData);
        notifyDataSetChanged();
    }

    public interface OnClickListenerEvent {
        void itemClick(View view, DataItem dataItem);
    }
}