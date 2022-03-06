package com.coin.rank.Roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.coin.rank.Roomdb.Entities.RoomAllMarket;
import com.coin.rank.Roomdb.Entities.RoomDataMarket;
import com.coin.rank.model.repository.DataItem;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
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
