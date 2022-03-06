package com.coin.rank.Roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.coin.rank.Roomdb.Entities.RoomAllMarket;
import com.coin.rank.Roomdb.Entities.RoomDataMarket;
import com.coin.rank.Roomdb.converter.AllCoinMarketTypeConverter;
import com.coin.rank.Roomdb.converter.DataItemTypeConverter;
import com.coin.rank.Roomdb.converter.ListUSDConverter;
import com.coin.rank.Roomdb.converter.MarketCryptoDataTypeConverter;
import com.coin.rank.Roomdb.converter.TottalSupplyConverter;
import com.coin.rank.model.repository.DataItem;

@TypeConverters({AllCoinMarketTypeConverter.class, MarketCryptoDataTypeConverter.class, DataItemTypeConverter.class, ListUSDConverter.class, TottalSupplyConverter.class})
@Database(version = 7, entities = {RoomAllMarket.class, RoomDataMarket.class, DataItem.class}, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase instance;
    public final static String nameDataBase = "db_room";


    public static synchronized AppDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDataBase.class, nameDataBase)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract RoomDao roomDao();
}
