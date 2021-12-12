package com.example.coinmarketjava.Roomdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.coinmarketjava.Roomdb.Entities.RoomAllMarket;
import com.example.coinmarketjava.Roomdb.Entities.RoomDataMarket;
import com.example.coinmarketjava.model.repository.AllCoinMarket;
import com.example.coinmarketjava.model.repository.CryptoDataMarket;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertToDb(RoomAllMarket roomAllMarket);

    @Query("SELECT * FROM AllMarket")
    Flowable<RoomAllMarket> getAllMarketFromDb();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDataToDb(RoomDataMarket roomDataMarket);

    @Query("SELECT * FROM dateMarket")
    Flowable<RoomDataMarket> getAllDataMarketFromDb();
}
