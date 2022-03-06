package com.coin.rank.model.repository;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "dataItemsFav")
public class DataItem implements Parcelable {


    public DataItem(int id, String name, String symbol, String slug, int cmcRank, double marketPairCount, double circulatingSupply, Number totalSupply, double maxSupply, double ath, double atl, double high24h, double low24h, int isActive, String lastUpdated, String dateAdded, List<ListUSD> quotes, Boolean isAudited, boolean isFav) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.slug = slug;
        this.cmcRank = cmcRank;
        this.marketPairCount = marketPairCount;
        this.circulatingSupply = circulatingSupply;
        this.totalSupply = totalSupply;
        this.maxSupply = maxSupply;
        this.ath = ath;
        this.atl = atl;
        this.high24h = high24h;
        this.low24h = low24h;
        this.isActive = isActive;
        this.lastUpdated = lastUpdated;
        this.dateAdded = dateAdded;
        this.quotes = quotes;
        this.isAudited = isAudited;
        this.isFav = isFav;
    }

    @PrimaryKey()
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

    private boolean isFav = false;

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.symbol);
        dest.writeString(this.slug);
        dest.writeInt(this.cmcRank);
        dest.writeDouble(this.marketPairCount);
        dest.writeDouble(this.circulatingSupply);
        dest.writeSerializable(this.totalSupply);
        dest.writeDouble(this.maxSupply);
        dest.writeDouble(this.ath);
        dest.writeDouble(this.atl);
        dest.writeDouble(this.high24h);
        dest.writeDouble(this.low24h);
        dest.writeInt(this.isActive);
        dest.writeString(this.lastUpdated);
        dest.writeString(this.dateAdded);
        dest.writeList(this.quotes);
        dest.writeValue(this.isAudited);
        dest.writeByte(this.isFav ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
        this.symbol = source.readString();
        this.slug = source.readString();
        this.cmcRank = source.readInt();
        this.marketPairCount = source.readDouble();
        this.circulatingSupply = source.readDouble();
        this.totalSupply = (Number) source.readSerializable();
        this.maxSupply = source.readDouble();
        this.ath = source.readDouble();
        this.atl = source.readDouble();
        this.high24h = source.readDouble();
        this.low24h = source.readDouble();
        this.isActive = source.readInt();
        this.lastUpdated = source.readString();
        this.dateAdded = source.readString();
        this.quotes = new ArrayList<ListUSD>();
        source.readList(this.quotes, ListUSD.class.getClassLoader());
        this.isAudited = (Boolean) source.readValue(Boolean.class.getClassLoader());
        this.isFav = source.readByte() != 0;
    }

    protected DataItem(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.symbol = in.readString();
        this.slug = in.readString();
        this.cmcRank = in.readInt();
        this.marketPairCount = in.readDouble();
        this.circulatingSupply = in.readDouble();
        this.totalSupply = (Number) in.readSerializable();
        this.maxSupply = in.readDouble();
        this.ath = in.readDouble();
        this.atl = in.readDouble();
        this.high24h = in.readDouble();
        this.low24h = in.readDouble();
        this.isActive = in.readInt();
        this.lastUpdated = in.readString();
        this.dateAdded = in.readString();
        this.quotes = new ArrayList<ListUSD>();
        in.readList(this.quotes, ListUSD.class.getClassLoader());
        this.isAudited = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isFav = in.readByte() != 0;
    }

    public static final Creator<DataItem> CREATOR = new Creator<DataItem>() {
        @Override
        public DataItem createFromParcel(Parcel source) {
            return new DataItem(source);
        }

        @Override
        public DataItem[] newArray(int size) {
            return new DataItem[size];
        }
    };
}
