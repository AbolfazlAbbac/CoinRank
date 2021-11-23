package com.example.coinmarketjava.hilt.module;

import android.content.Context;

import com.example.coinmarketjava.Roomdb.AppDataBase;
import com.example.coinmarketjava.Roomdb.RoomDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class HiltRoomModule {

    @Provides
    @Singleton
    public AppDataBase provideAppDataBase(@ApplicationContext Context context) {
        return AppDataBase.getInstance(context);
    }

    @Provides
    @Singleton
    RoomDao roomDb(AppDataBase appDataBase) {
        return appDataBase.roomDao();
    }
}
