package com.example.coinmarketjava.model.repository;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataItem implements Parcelable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("symbol")
    private String symbol;

    @SerializedName("slug")
    private String slug;

    @SerializedName("cmcRank")
    private int cmcRank;

    @SerializedName("marketPairCount")
    private double marketPairCount;

    @SerializedName("circulatingSupply")
    private double circulatingSupply;

    @SerializedName("totalSupply")
    private Number totalSupply;

    @SerializedName("maxSupply")
    private double maxSupply;

    @SerializedName("ath")
    private double ath;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getSlug() {
        return slug;
    }

    public int getCmcRank() {
        return cmcRank;
    }

    public double getMarketPairCount() {
        return marketPairCount;
    }

    public double getCirculatingSupply() {
        return circulatingSupply;
    }

    public Number getTotalSupply() {
        return totalSupply;
    }

    public double getMaxSupply() {
        return maxSupply;
    }

    public double getAth() {
        return ath;
    }

    public double getAtl() {
        return atl;
    }

    public double getHigh24h() {
        return high24h;
    }

    public double getLow24h() {
        return low24h;
    }

    public int getIsActive() {
        return isActive;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public List<ListUSD> getQuotes() {
        return quotes;
    }

    public Boolean getAudited() {
        return isAudited;
    }

    public static Creator<DataItem> getCREATOR() {
        return CREATOR;
    }

    @SerializedName("atl")
    private double atl;

    @SerializedName("high24h")
    private double high24h;

    @SerializedName("low24h")
    private double low24h;

    @SerializedName("isActive")
    private int isActive;

    @SerializedName("lastUpdated")
    private String lastUpdated;

    @SerializedName("dateAdded")
    private String dateAdded;

    @SerializedName("quotes")
    private List<ListUSD> quotes;

    @SerializedName("isAudited")
    private Boolean isAudited;

    public DataItem(Parcel in) {
        id = in.readInt();
        name = in.readString();
        symbol = in.readString();
        slug = in.readString();
        cmcRank = in.readInt();
        marketPairCount = in.readDouble();
        circulatingSupply = in.readDouble();
        maxSupply = in.readDouble();
        ath = in.readDouble();
        atl = in.readDouble();
        high24h = in.readDouble();
        low24h = in.readDouble();
        isActive = in.readInt();
        lastUpdated = in.readString();
        dateAdded = in.readString();
        byte tmpIsAudited = in.readByte();
        isAudited = tmpIsAudited == 0 ? null : tmpIsAudited == 1;
    }

    public static final Creator<DataItem> CREATOR = new Creator<DataItem>() {
        @Override
        public DataItem createFromParcel(Parcel in) {
            return new DataItem(in);
        }

        @Override
        public DataItem[] newArray(int size) {
            return new DataItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(symbol);
        parcel.writeString(slug);
        parcel.writeInt(cmcRank);
        parcel.writeDouble(marketPairCount);
        parcel.writeDouble(circulatingSupply);
        parcel.writeDouble(maxSupply);
        parcel.writeDouble(ath);
        parcel.writeDouble(atl);
        parcel.writeDouble(high24h);
        parcel.writeDouble(low24h);
        parcel.writeInt(isActive);
        parcel.writeString(lastUpdated);
        parcel.writeString(dateAdded);
        parcel.writeByte((byte) (isAudited == null ? 0 : isAudited ? 1 : 2));
    }
}
