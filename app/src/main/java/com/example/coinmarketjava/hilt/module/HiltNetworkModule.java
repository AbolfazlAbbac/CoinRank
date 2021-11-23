package com.example.coinmarketjava.hilt.module;

import com.example.coinmarketjava.AppRepository;
import com.example.coinmarketjava.Roomdb.RoomDao;
import com.example.coinmarketjava.http.ApiService;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class HiltNetworkModule {

    String Base_Url = "https://pro-api.coinmarketcap.com";

    @Provides
    @Singleton
    OkHttpClient ProvidesOkHttpClient() {
        return new OkHttpClient().newBuilder().addInterceptor(chain -> {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("X-CMC_PRO_API_KEY", "2f7b1b18-cbeb-4861-8515-bca6b05e3777")
                    .build();
            return chain.proceed(request);
        }).build();
    }


    @Provides
    @Singleton
    Retrofit ProvidesRetrofitClient(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(Base_Url)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    ApiService apiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

    @Provides
    @Singleton
    AppRepository appRepository(ApiService apiService, RoomDao roomDao) {
        return new AppRepository(apiService, roomDao);
    }
}
