package com.example.SiAntik;

import com.google.gson.annotations.SerializedName;

public class ImageModel {

    @SerializedName("image_data")
    private String imageData; // Ganti tipe data sesuai dengan kebutuhan (misalnya, String untuk representasi base64)

    // Konstruktor, setter, getter, dll.
    public ImageModel(String imageData) {
        this.imageData = imageData;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
}

