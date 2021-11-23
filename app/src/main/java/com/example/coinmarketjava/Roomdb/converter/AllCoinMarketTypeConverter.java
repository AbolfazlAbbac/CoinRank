package com.example.coinmarketjava.Roomdb.converter;

import androidx.room.TypeConverter;

import com.example.coinmarketjava.model.repository.AllCoinMarket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class AllCoinMarketTypeConverter {

    @TypeConverter
    public String toJson(AllCoinMarket allCoinMarket) {
        if (allCoinMarket == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<AllCoinMarket>() {
        }.getType();

        return gson.toJson(allCoinMarket, type);
    }

    @TypeConverter
    public AllCoinMarket toDataClass(String allMarket) {
        if (allMarket == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<AllCoinMarket>() {
        }.getType();
        return gson.fromJson(allMarket, type);
    }
}
