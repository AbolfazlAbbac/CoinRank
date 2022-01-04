package com.example.coinmarketjava.Roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.coinmarketjava.Roomdb.Entities.RoomAllMarket;
import com.example.coinmarketjava.Roomdb.Entities.RoomDataItemsFav;
import com.example.coinmarketjava.Roomdb.Entities.RoomDataMarket;
import com.example.coinmarketjava.model.repository.DataItem;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(DataItem dataItem);


    @Delete
    Completable deleteItemsFav(DataItem dataItem);

    @Query("SELECT * FROM dataItemsFav")
    Single<List<DataItem>> getAllDataItemsFav();
}
