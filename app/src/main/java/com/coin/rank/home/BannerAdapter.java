package com.coin.rank.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rank.R;
import com.example.rank.databinding.SliderBannerBinding;

import java.util.ArrayList;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.sliderViewHolder> {

    ArrayList<Integer> arrayList;

    LayoutInflater inflater;

    public BannerAdapter(ArrayList<Integer> arrayList) {
        this.arrayList = arrayList;
    }


    @NonNull
    @Override

    public sliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        SliderBannerBinding sliderBannerBinding = DataBindingUtil.inflate(inflater, R.layout.slider_banner, parent, false);

        return new sliderViewHolder(sliderBannerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull sliderViewHolder holder, int position) {
        holder.bind(arrayList.get(position));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class sliderViewHolder extends RecyclerView.ViewHolder {
        SliderBannerBinding sliderBannerBinding;

        public sliderViewHolder(@NonNull SliderBannerBinding sliderBannerBinding) {
            super(sliderBannerBinding.getRoot());
            this.sliderBannerBinding = sliderBannerBinding;
        }

        void bind(int photo) {
            sliderBannerBinding.backgroundSlider.setVisibility(View.VISIBLE);
            Glide.with(sliderBannerBinding.getRoot().getContext())
                    .load(photo)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(sliderBannerBinding.roundedImageViewBanner);
//            sliderBannerBinding.roundedImageViewBanner.setActualImageResource(photo);
            sliderBannerBinding.executePendingBindings();
        }
    }

}
