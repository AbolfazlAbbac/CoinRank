package com.example.coinmarketjava.Roomdb.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.coinmarketjava.model.repository.AllCoinMarket;

@Entity(tableName = "AllMarket")
public class RoomAllMarket {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "allMarket")
    public AllCoinMarket allCoinMarket;

    public RoomAllMarket(AllCoinMarket allCoinMarket) {
        this.allCoinMarket = allCoinMarket;
    }

    public int getId() {
        return id;
    }

    public AllCoinMarket getAllCoinMarket() {
        return allCoinMarket;
    }
}
