package com.coin.rank.watchlist;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rank.R;
import com.example.rank.databinding.ItemWatchListBinding;
import com.coin.rank.model.repository.DataItem;

import java.util.List;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.ViewHolderWatch> {
    List<DataItem> dataItemList;
    static onClickListener onClickListener;

    public WatchListAdapter(List<DataItem> dataItems, onClickListener listener) {
        this.dataItemList = dataItems;
        onClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolderWatch onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemWatchListBinding binding = ItemWatchListBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false);

        return new ViewHolderWatch(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderWatch holder, int position) {
        holder.bind(dataItemList.get(position));
        holder.itemView.setOnClickListener(view -> onClickListener.onClickItem(dataItemList.get(position)));
    }

    @Override
    public int getItemCount() {
        return dataItemList.size();
    }

    static class ViewHolderWatch extends RecyclerView.ViewHolder {
        private final ItemWatchListBinding binding;

        public ViewHolderWatch(@NonNull ItemWatchListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(DataItem dataItem) {
            setupChart(dataItem);
            setupIconCrypto(dataItem);
            setupColorPriceAndPercent(dataItem);

            if (dataItem.isFav()) {
                binding.favWatchListFragment.setImageResource(R.drawable.ic_baseline_star_rate_24);
            } else {
                binding.favWatchListFragment.setImageResource(R.drawable.ic_baseline_star_outline_24);
            }

            binding.favWatchListFragment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.removeAndAddFav(dataItem);
                    if (dataItem.isFav())
                        binding.favWatchListFragment.setImageResource(R.drawable.ic_baseline_star_outline_24);
                    else
                        binding.favWatchListFragment.setImageResource(R.drawable.ic_baseline_star_rate_24);
                }
            });


            binding.nameCoinTv.setText(dataItem.getName() + dataItem.getSymbol());
            binding.rankWatchList.setText(String.valueOf(dataItem.getCmcRank()));

            setPrice(dataItem);

            if (dataItem.getQuotes().get(0).getPercentChange24h() > 0) {
                binding.percentChangeTv.setText(String.format("+%.2f", dataItem.getQuotes().get(0).getPercentChange24h()) + "%");
                binding.chartWatchListFragment.setColorFilter(Color.GREEN);
                binding.arrowMarketFragment.setImageResource(R.drawable.dropup_arrows);
            } else {
                binding.percentChangeTv.setText(String.format("%.2f", dataItem.getQuotes().get(0).getPercentChange24h()) + "%");
                binding.chartWatchListFragment.setColorFilter(Color.RED);
                binding.arrowMarketFragment.setImageResource(R.drawable.drop_arrows);
            }

            binding.executePendingBindings();
        }

        private void setPrice(@NonNull DataItem dataItem) {
            if (dataItem.getQuotes().get(0).getPrice() < 1) {
                binding.currentPriceTv.setText("$" + String.format("%.6f", dataItem.getQuotes().get(0).getPrice()));
            } else if (dataItem.getQuotes().get(0).getPrice() < 10) {
                binding.currentPriceTv.setText("$" + String.format("%.4f", dataItem.getQuotes().get(0).getPrice()));
            } else {
                binding.currentPriceTv.setText("$" + String.format("%.2f", dataItem.getQuotes().get(0).getPrice()));
            }
        }

        private void setupColorPriceAndPercent(@NonNull DataItem dataItem) {
            if (dataItem.getQuotes().get(0).getPercentChange24h() > 0) {
                binding.currentPriceTv.setTextColor(Color.WHITE);
                binding.percentChangeTv.setTextColor(Color.GREEN);
            } else if (dataItem.getQuotes().get(0).getPercentChange24h() < 0) {
                binding.currentPriceTv.setTextColor(Color.WHITE);
                binding.percentChangeTv.setTextColor(Color.RED);
            }
        }

        private void setupIconCrypto(@NonNull DataItem dataItem) {
            Glide.with(binding.getRoot().getContext())
                    .load("https://s2.coinmarketcap.com/static/img/coins/32x32/" + dataItem.getId() + ".png")
                    .thumbnail(Glide.with(binding.getRoot().getContext()).load(R.drawable.loader))
                    .into(binding.logoIconIv);
        }

        private void setupChart(@NonNull DataItem dataItem) {
            Glide.with(binding.getRoot().getContext())
                    .load("https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/" + dataItem.getId() + ".png")
                    .thumbnail(Glide.with(binding.getRoot().getContext()).load(R.drawable.loader))
                    .into(binding.chartWatchListFragment);
        }
    }

    public void update(List<DataItem> dataItems) {
        dataItems.clear();
        this.dataItemList = dataItems;
        notifyDataSetChanged();
    }

    interface onClickListener {
        void onClickItem(DataItem dataItem);

        void removeAndAddFav(DataItem dataItem);
    }
}
