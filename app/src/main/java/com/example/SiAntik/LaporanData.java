package com.example.SiAntik;

import com.google.gson.annotations.SerializedName;

public class LaporanData {
    @SerializedName("id_laporan")
    private String idLapor;

    @SerializedName("nik_user")
    private String nikUser;

    @SerializedName("tanggal_laporan")
    private String tanggalLaporan;

    @SerializedName("tanggal_pemantauan")
    private String tanggalPemantauan;

    @SerializedName("foto")
    private String foto;

    @SerializedName("deskripsi")
    private String deskripsi;

    @SerializedName("status")
    private String status;

    public LaporanData(String nikUser, String tanggalLaporan, String status) {
        this.nikUser = nikUser;
        this.tanggalLaporan = tanggalLaporan;
        this.status = status;
    }

    public String getIdLapor() {
        return idLapor;
    }

    public void setIdLapor(String idLapor) {
        this.idLapor = idLapor;
    }

    public String getNikUser() {
        return nikUser;
    }

    public void setNikUser(String nikUser) {
        this.nikUser = nikUser;
    }

    public String getTanggalLaporan() {
        return tanggalLaporan;
    }

    public void setTanggalLaporan(String tanggalLaporan) {
        this.tanggalLaporan = tanggalLaporan;
    }

    public String getStatuss() {
        return status;
    }

    public void setStatuss(String status) {
        this.status = status;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFoto() {
        return foto;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTanggalPemantauan() {
        return tanggalPemantauan;
    }

    public void setTanggalPemantauan(String tanggalPemantauan) {
        this.tanggalPemantauan = tanggalPemantauan;
    }
}
