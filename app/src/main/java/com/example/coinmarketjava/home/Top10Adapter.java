package com.example.coinmarketjava.home;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coinmarketjava.R;
import com.example.coinmarketjava.databinding.Top10adapterBinding;
import com.example.coinmarketjava.model.repository.DataItem;

import java.util.ArrayList;

public class Top10Adapter extends RecyclerView.Adapter<Top10Adapter.Top10ViewHolder> {
    ArrayList<DataItem> dataItems;
    LayoutInflater inflater;

    public Top10Adapter(ArrayList<DataItem> dataItems) {
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public Top10ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater =
                    LayoutInflater.from(parent.getContext());
        }
        Top10adapterBinding binding = Top10adapterBinding.inflate(inflater, parent, false);
        return new Top10ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Top10ViewHolder holder, int position) {
        holder.bind(dataItems.get(position));
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public static class Top10ViewHolder extends RecyclerView.ViewHolder {
        private final Top10adapterBinding binding;

        public Top10ViewHolder(@NonNull Top10adapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        public void bind(DataItem dataItem) {
            loadCoinLogo(dataItem);
            setColorText(dataItem);
            setDecimalPrice(dataItem);
            binding.nameCoinTv.setText(String.format("%s/USD", dataItem.getSymbol()));

            if (dataItem.getQuotes().get(0).getPercentChange24h() > 0) {
                binding.percentChangeTv.setText("+" + String.format("%.2f", dataItem.getQuotes().get(0).getPercentChange24h()));
            } else
                binding.percentChangeTv.setText(String.format("%.2f", dataItem.getQuotes().get(0).getPercentChange24h()));

            binding.executePendingBindings();
        }

        private void loadCoinLogo(@NonNull DataItem dataItem) {
            Glide.with(binding.getRoot().getContext())
                    .load("https://s2.coinmarketcap.com/static/img/coins/32x32/" + dataItem.getId() + ".png")
                    .thumbnail(Glide.with(binding.getRoot().getContext()).load(R.drawable.loader))
                    .into(binding.logoIconIv);
        }

        private void setDecimalPrice(@NonNull DataItem dataItem) {
            if (dataItem.getQuotes().get(0).getPrice() < 1) {
                binding.currentPriceTv.setText(String.format("%.6f", dataItem.getQuotes().get(0).getPrice()));
            } else if (dataItem.getQuotes().get(0).getPrice() < 10) {
                binding.currentPriceTv.setText(String.format("%.4f", dataItem.getQuotes().get(0).getPrice()));
            } else
                binding.currentPriceTv.setText(String.format("%.2f", dataItem.getQuotes().get(0).getPrice()));
        }


        private void setColorText(@NonNull DataItem dataItem) {

            if (dataItem.getQuotes().get(0).getPercentChange24h() < 0) {
                binding.percentChangeTv.setTextColor(Color.RED);
                binding.currentPriceTv.setTextColor(Color.RED);
            } else {
                binding.percentChangeTv.setTextColor(Color.GREEN);
                binding.currentPriceTv.setTextColor(Color.GREEN);
            }
        }
    }


    public void updateItem(ArrayList<DataItem> newItems) {
        dataItems.clear();
        dataItems.addAll(newItems);
        notifyDataSetChanged();
    }
}
