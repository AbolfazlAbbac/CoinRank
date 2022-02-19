package com.example.coinmarketjava.model.repository;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ListUSD implements Parcelable {
    @SerializedName("price")
    private double price;

    @SerializedName("volume24h")
    private Number volume24h;

    @SerializedName("volume7d")
    private double volume7d;

    @SerializedName("volume30d")
    private double volume30d;

    @SerializedName("marketCap")
    private Number marketCap;

    @SerializedName("percentChange1h")
    private double percentChange1h;

    protected ListUSD(Parcel in) {
        price = in.readDouble();
        volume7d = in.readDouble();
        volume30d = in.readDouble();
        percentChange1h = in.readDouble();
        lastUpdated = in.readString();
        percentChange24h = in.readDouble();
        percentChange7d = in.readDouble();
        percentChange30d = in.readDouble();
        percentChange60d = in.readDouble();
        percentChange90d = in.readDouble();
        fullyDilluttedMarketCap = in.readDouble();
        dominance = in.readDouble();
        turnover = in.readDouble();
    }

    public static final Creator<ListUSD> CREATOR = new Creator<ListUSD>() {
        @Override
        public ListUSD createFromParcel(Parcel in) {
            return new ListUSD(in);
        }

        @Override
        public ListUSD[] newArray(int size) {
            return new ListUSD[size];
        }
    };

    public double getPrice() {
        return price;
    }

    public Number getVolume24h() {
        return volume24h;
    }

    public double getVolume7d() {
        return volume7d;
    }

    public double getVolume30d() {
        return volume30d;
    }

    public Number getMarketCap() {
        return marketCap;
    }

    public double getPercentChange1h() {
        return percentChange1h;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public double getPercentChange24h() {
        return percentChange24h;
    }

    public double getPercentChange7d() {
        return percentChange7d;
    }

    public double getPercentChange30d() {
        return percentChange30d;
    }

    public double getPercentChange60d() {
        return percentChange60d;
    }

    public double getPercentChange90d() {
        return percentChange90d;
    }

    public double getFullyDilluttedMarketCap() {
        return fullyDilluttedMarketCap;
    }

    public double getDominance() {
        return dominance;
    }

    public double getTurnover() {
        return turnover;
    }

    @SerializedName("lastUpdated")
    private String lastUpdated;

    @SerializedName("percentChange24h")
    private double percentChange24h;

    @SerializedName("percentChange7d")
    private double percentChange7d;

    @SerializedName("percentChange30d")
    private double percentChange30d;

    @SerializedName("percentChange60d")
    private double percentChange60d;

    @SerializedName("percentChange90d")
    private double percentChange90d;

    @SerializedName("fullyDilluttedMarketCap")
    private double fullyDilluttedMarketCap;

    @SerializedName("dominance")
    private double dominance;

    @SerializedName("turnover")
    private double turnover;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(price);
        dest.writeDouble(volume7d);
        dest.writeDouble(volume30d);
        dest.writeDouble(percentChange1h);
        dest.writeString(lastUpdated);
        dest.writeDouble(percentChange24h);
        dest.writeDouble(percentChange7d);
        dest.writeDouble(percentChange30d);
        dest.writeDouble(percentChange60d);
        dest.writeDouble(percentChange90d);
        dest.writeDouble(fullyDilluttedMarketCap);
        dest.writeDouble(dominance);
        dest.writeDouble(turnover);
    }
}
