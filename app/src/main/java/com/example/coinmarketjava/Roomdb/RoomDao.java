package com.example.coinmarketjava.Roomdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.coinmarketjava.Roomdb.Entities.RoomAllMarket;
import com.example.coinmarketjava.model.repository.AllCoinMarket;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertToDb(RoomAllMarket roomAllMarket);

    @Query("SELECT * FROM ALLMARKET")
    Flowable<RoomAllMarket> getAllMarketFromDb();
}
