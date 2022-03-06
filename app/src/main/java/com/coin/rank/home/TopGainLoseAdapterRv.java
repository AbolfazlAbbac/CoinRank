package com.coin.rank.home;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.coin.rank.model.repository.DataItem;
import com.example.rank.R;
import com.example.rank.databinding.ItemTopgainloseBinding;

import java.util.ArrayList;

public class TopGainLoseAdapterRv extends RecyclerView.Adapter<TopGainLoseAdapterRv.ViewHolder> {

    LayoutInflater inflater;

    ArrayList<DataItem> dataItems;

    OnClickListenerEvent onClickListenerEvent;

    public TopGainLoseAdapterRv(ArrayList<DataItem> dataItems, OnClickListenerEvent onClickListenerEvent) {
        this.dataItems = dataItems;
        this.onClickListenerEvent = onClickListenerEvent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        ItemTopgainloseBinding binding = ItemTopgainloseBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(dataItems.get(position));
        onClickListenerEvent.clickEvent(holder.itemView, dataItems.get(position));
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTopgainloseBinding binding;



        public ViewHolder(@NonNull ItemTopgainloseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(DataItem dataItem) {

            setupChart(dataItem);
            setupIconCrypto(dataItem);
            setupColorPriceAndPercent(dataItem);

            binding.nameItemTopGain.setText(dataItem.getName());
            binding.codeItemTopGain.setText(dataItem.getSymbol());

            setPrice(dataItem);

            if (dataItem.getQuotes().get(0).getPercentChange24h() > 0) {
                binding.percentChangeItemTopGain.setText(String.format("+%.2f", dataItem.getQuotes().get(0).getPercentChange24h()) + "%");
                binding.chartItemTopGain.setColorFilter(Color.GREEN);
                binding.arrowItemTopGain.setImageResource(R.drawable.dropup_arrows);
            } else {
                binding.percentChangeItemTopGain.setText(String.format("%.2f", dataItem.getQuotes().get(0).getPercentChange24h()) + "%");
                binding.chartItemTopGain.setColorFilter(Color.RED);
                binding.arrowItemTopGain.setImageResource(R.drawable.drop_arrows);
            }

            binding.executePendingBindings();
        }

        private void setPrice(@NonNull DataItem dataItem) {
            if (dataItem.getQuotes().get(0).getPrice() < 1) {
                binding.priceItemTopGain.setText("$" + String.format("%.6f", dataItem.getQuotes().get(0).getPrice()));
            } else if (dataItem.getQuotes().get(0).getPrice() < 10) {
                binding.priceItemTopGain.setText("$" + String.format("%.4f", dataItem.getQuotes().get(0).getPrice()));
            } else {
                binding.priceItemTopGain.setText("$" + String.format("%.2f", dataItem.getQuotes().get(0).getPrice()));
            }
        }

        private void setupColorPriceAndPercent(@NonNull DataItem dataItem) {
            if (dataItem.getQuotes().get(0).getPercentChange24h() > 0) {
                binding.priceItemTopGain.setTextColor(Color.GREEN);
                binding.percentChangeItemTopGain.setTextColor(Color.GREEN);
            } else if (dataItem.getQuotes().get(0).getPercentChange24h() < 0) {
                binding.priceItemTopGain.setTextColor(Color.RED);
                binding.percentChangeItemTopGain.setTextColor(Color.RED);
            }
        }

        private void setupIconCrypto(@NonNull DataItem dataItem) {
            Glide.with(binding.getRoot().getContext())
                    .load("https://s2.coinmarketcap.com/static/img/coins/32x32/" + dataItem.getId() + ".png")
                    .thumbnail(Glide.with(binding.getRoot().getContext()).load(R.drawable.loader))
                    .into(binding.iconCryptoItemTopGain);
        }

        private void setupChart(@NonNull DataItem dataItem) {
            Glide.with(binding.getRoot().getContext())
                    .load("https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/" + dataItem.getId() + ".png")
                    .thumbnail(Glide.with(binding.getRoot().getContext()).load(R.drawable.loader))
                    .into(binding.chartItemTopGain);
        }
    }

    public void update(ArrayList<DataItem> newData) {
        dataItems.clear();
        dataItems.addAll(newData);
        notifyDataSetChanged();
    }

    public interface OnClickListenerEvent {
        void clickEvent(View view, DataItem dataItem);
    }
}
