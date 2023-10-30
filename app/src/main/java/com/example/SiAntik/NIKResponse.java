package com.example.SiAntik;

import com.google.gson.annotations.SerializedName;

public class NIKResponse {
    @SerializedName("nik")
    private String nik; // Sesuaikan dengan nama field NIK dalam respons API

    // Buat konstruktor jika diperlukan
    public NIKResponse(String nik) {
        this.nik = nik;
    }

    public String getNIK() {
        return nik;
    }
}
