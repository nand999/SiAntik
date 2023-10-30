package com.example.SiAntik;

import com.google.gson.annotations.SerializedName;

public class ImageModel {
    private String nik_user;
    private byte[] foto;
    private String deskripsi;

    public ImageModel(String nik_user, byte[] foto, String deskripsi) {
        this.nik_user = nik_user;
        this.foto = foto;
        this.deskripsi = deskripsi;
    }

    public String getNikUser() {
        return nik_user;
    }

    public void setNikUser(String nik_user) {
        this.nik_user = nik_user;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}


