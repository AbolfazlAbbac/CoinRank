package com.coin.rank.model.repository;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class AllCoinMarket implements Parcelable {

    @SerializedName("data")
    private RootData data;

    @SerializedName("status")
    private ListStatus listStatus;

    protected AllCoinMarket(Parcel in) {
    }

    public static final Creator<AllCoinMarket> CREATOR = new Creator<AllCoinMarket>() {
        @Override
        public AllCoinMarket createFromParcel(Parcel in) {
            return new AllCoinMarket(in);
        }

        @Override
        public AllCoinMarket[] newArray(int size) {
            return new AllCoinMarket[size];
        }
    };

    public RootData getRootData() {
        return data;
    }

    public ListStatus getStatus() {
        return listStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }


    public static class ListStatus {

        @SerializedName("error_message")
        private String errorMessage;

        @SerializedName("elapsed")
        private int elapsed;

        @SerializedName("total_count")
        private int totalCount;

        @SerializedName("credit_count")
        private int creditCount;

        @SerializedName("error_code")
        private int errorCode;

        @SerializedName("timestamp")
        private String timestamp;

        @SerializedName("notice")
        private Object notice;

        public String getErrorMessage() {
            return errorMessage;
        }

        public int getElapsed() {
            return elapsed;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public int getCreditCount() {
            return creditCount;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public Object getNotice() {
            return notice;
        }
    }


    public static class RootData {

        @SerializedName("cryptoCurrencyList")
        private List<DataItem> cryptoCurrencyList;

        @SerializedName("totalCount")
        private int totalCount;

        public List<DataItem> getCryptoCurrencyList() {
            return cryptoCurrencyList;
        }

        public int getTotalCount() {
            return totalCount;
        }
    }
}
