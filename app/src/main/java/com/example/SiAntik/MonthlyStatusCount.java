package com.example.SiAntik;

import com.google.gson.annotations.SerializedName;

public class MonthlyStatusCount {
    @SerializedName("month")
    private String month;

    @SerializedName("count")
    private int count;

    public MonthlyStatusCount(String month, int count) {
        this.month = month;
        this.count = count;
    }

    public String getMonth() {
        return month;
    }

    public int getCount() {
        return count;
    }
}
