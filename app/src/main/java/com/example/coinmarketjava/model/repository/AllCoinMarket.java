package com.example.coinmarketjava.model.repository;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllCoinMarket {
    public static class data {
        @SerializedName("totalCount")
        private String totalCount;

        public String getTotalCount() {
            return totalCount;
        }

        public List<DataItem> getCryptoList() {
            return cryptoList;
        }

        @SerializedName("cryptoCurrencyList")
        private List<DataItem> cryptoList;
    }

    public static class status {
        @SerializedName("timestamp")
        private String timeStamp;

        public String getTimeStamp() {
            return timeStamp;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public String getElapsed() {
            return elapsed;
        }

        public int getCreditCount() {
            return creditCount;
        }

        @SerializedName("error_code")
        private String errorCode;

        @SerializedName("error_message")
        private String errorMessage;

        @SerializedName("elapsed")
        private String elapsed;

        @SerializedName("credit_count")
        private int creditCount;
    }

}
