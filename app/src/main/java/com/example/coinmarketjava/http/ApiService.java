package com.example.coinmarketjava.http;

import com.example.coinmarketjava.model.repository.AllCoinMarket;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface ApiService {

    @GET("https://api.coinmarketcap.com/data-api/v3/cryptocurrency/listing?start=1&limit=700&sortBy=market_cap&sortType=desc&convert=USD&cryptoType=all&tagType=all&audited=false&aux=ath,atl,high24h,low24h,num_market_pairs,cmc_rank,date_added,tags,platform,max_supply,circulating_supply,total_supply,volume_7d,volume_30d")
    Observable<AllCoinMarket> makeLatestCryptoList();
}
