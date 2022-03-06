package com.coin.rank.Roomdb.converter;

import androidx.room.TypeConverter;

import com.coin.rank.model.repository.DataItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class DataItemTypeConverter {

    @TypeConverter
    public String toJson(DataItem dataItem) {
        if (dataItem == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<DataItem>() {
        }.getType();

        return gson.toJson(dataItem, type);
    }

    @TypeConverter
    public DataItem toDataClass(String dataItems) {
        if (dataItems == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<DataItem>() {
        }.getType();
        return gson.fromJson(dataItems, type);
    }


}
