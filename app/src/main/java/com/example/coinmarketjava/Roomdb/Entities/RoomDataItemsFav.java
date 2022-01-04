package com.example.coinmarketjava.Roomdb.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.coinmarketjava.model.repository.AllCoinMarket;
import com.example.coinmarketjava.model.repository.DataItem;

import java.util.List;

public class RoomDataItemsFav {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "dataItem")
    public DataItem dataItem;

    public RoomDataItemsFav(DataItem dataItem) {
        this.dataItem = dataItem;
    }

    public int getId() {
        return id;
    }

    public DataItem getDataItem() {
        return dataItem;
    }
}
