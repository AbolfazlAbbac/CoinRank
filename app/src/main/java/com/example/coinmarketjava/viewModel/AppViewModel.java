package com.example.coinmarketjava.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.coinmarketjava.AppRepository;
import com.example.coinmarketjava.R;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AppViewModel extends AndroidViewModel {
    MutableLiveData<ArrayList<Integer>> bannerData = new MutableLiveData<>();


    @Inject
    AppRepository appRepository;

    @Inject
    public AppViewModel(@NonNull Application application) {
        super(application);
        getBannerData();
    }


    private MutableLiveData<ArrayList<Integer>> getBannerData() {
        ArrayList<Integer> pics = new ArrayList<>();

        pics.add(R.drawable.banner);
        pics.add(R.drawable.banner_one);
        pics.add(R.drawable.banner_two);

        bannerData.postValue(pics);

        return bannerData;
    }

    public MutableLiveData<ArrayList<Integer>> getBannerLiveData() {
        return bannerData;
    }
}
