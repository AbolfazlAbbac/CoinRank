package com.example.coinmarketjava.Roomdb.converter;

import androidx.room.TypeConverter;

import com.example.coinmarketjava.model.repository.ListUSD;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ListUSDConverter {

    @TypeConverter
    public String toJson(List<ListUSD> dataItem) {
        if (dataItem == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<ListUSD>>() {
        }.getType();

        return gson.toJson(dataItem, type);
    }

    @TypeConverter
    public List<ListUSD> toDataClass(String dataItems) {
        if (dataItems == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<ListUSD>>() {
        }.getType();
        return gson.fromJson(dataItems, type);
    }


}
