package com.example.SiAntik;

import com.google.gson.annotations.SerializedName;

public class StatusData1 {
    @SerializedName("status")
    private String status;

    public StatusData1(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
