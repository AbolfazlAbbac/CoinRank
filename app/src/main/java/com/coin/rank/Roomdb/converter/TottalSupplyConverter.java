package com.coin.rank.Roomdb.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class TottalSupplyConverter {

    @TypeConverter
    public String toJson(Number dataItem) {
        if (dataItem == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Number>() {
        }.getType();

        return gson.toJson(dataItem, type);
    }

    @TypeConverter
    public Number toDataClass(String dataItems) {
        if (dataItems == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Number>() {
        }.getType();
        return gson.fromJson(dataItems, type);
    }


}
