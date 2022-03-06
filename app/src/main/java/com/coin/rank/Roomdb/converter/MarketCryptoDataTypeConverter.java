package com.coin.rank.Roomdb.converter;

import androidx.room.TypeConverter;

import com.coin.rank.model.repository.CryptoDataMarket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class MarketCryptoDataTypeConverter {

    @TypeConverter
    public String toCryptoDataJson(CryptoDataMarket cryptoDataMarket) {
        if (cryptoDataMarket == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<CryptoDataMarket>() {
        }.getType();

        return gson.toJson(cryptoDataMarket, type);
    }

    @TypeConverter
    public CryptoDataMarket toCryptoDataClass(String cryptoDataMarket) {
        if (cryptoDataMarket == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<CryptoDataMarket>() {
        }.getType();
        return gson.fromJson(cryptoDataMarket, type);
    }
}
