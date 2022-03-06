package com.coin.rank.Roomdb.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.coin.rank.model.repository.CryptoDataMarket;

@Entity(tableName = "dateMarket")
public class RoomDataMarket {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "dataMarket")
    public CryptoDataMarket cryptoDataMarket;

    public RoomDataMarket(CryptoDataMarket cryptoDataMarket) {
        this.cryptoDataMarket = cryptoDataMarket;
    }

    public CryptoDataMarket getCryptoDataMarket() {
        return cryptoDataMarket;
    }

    public int getId() {
        return id;
    }
}
