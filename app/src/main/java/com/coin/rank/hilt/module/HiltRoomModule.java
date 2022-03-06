package com.coin.rank.hilt.module;

import android.content.Context;

import com.coin.rank.Roomdb.AppDataBase;
import com.coin.rank.Roomdb.RoomDao;

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
