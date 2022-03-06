package com.coin.rank.Roomdb.Entities;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import com.coin.rank.model.repository.DataItem;

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
