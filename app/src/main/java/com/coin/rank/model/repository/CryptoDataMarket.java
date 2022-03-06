package com.coin.rank.model.repository;

public class CryptoDataMarket {
    private String marketCap;
    private String volume24h;
    private String dominance_btc;
    private String dominance_eth;

    public CryptoDataMarket(String marketCap, String volume24h, String dominance_btc, String dominance_eth) {
        this.marketCap = marketCap;
        this.volume24h = volume24h;
        this.dominance_btc = dominance_btc;
        this.dominance_eth = dominance_eth;
    }

    public String getMarketCap() {
        return marketCap;
    }


    public String getVolume24h() {
        return volume24h;
    }


    public String getDominance_btc() {
        return dominance_btc;
    }


    public String getDominance_eth() {
        return dominance_eth;
    }

}
