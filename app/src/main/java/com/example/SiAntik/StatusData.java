package com.example.SiAntik;

import com.google.gson.annotations.SerializedName;

public class StatusData {
    @SerializedName("countNull")
    private int countNull;

    @SerializedName("countZero")
    private int countZero;

    @SerializedName("countOne")
    private int countOne;

    public int getCountNull() {
        return countNull;
    }

    public int getCountZero() {
        return countZero;
    }

    public int getCountOne() {
        return countOne;
    }
}
