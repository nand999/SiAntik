package com.example.SiAntik;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserModel {

    @Expose
    @SerializedName("nik_user")
    private String nik_user;
    @Expose
    @SerializedName("nama_user")
    private String nama_user;
    @Expose
    @SerializedName("rt_rw")
    private String rt_rw;
    @Expose
    @SerializedName("no_rumah")
    private String no_rumah;
    @Expose
    @SerializedName("password_user")
    private String password_user;
//    @Expose
//    @SerializedName("alamat_user")
//    private String Alamat_user;
//    @Expose
//    @SerializedName("no.telp_user")
//    private String notelp_user;
//    @Expose
//    @SerializedName("user_foto")
//    private String userfoto;

    public UserModel(String idakun, String nama, String rt_rw, String no_rumah, String pass
//                     String alamatuser, String notelpuser, String userfoto
    ) {
        this.nik_user = idakun;
        this.nama_user = nama;
        this.password_user = pass;
        this.rt_rw = rt_rw;
        this.no_rumah = no_rumah;
//        this.Alamat_user = alamatuser;
//        this.notelp_user = notelpuser;
//        this.userfoto = userfoto;
    }

    public String getNik_user() {
        return nik_user;
    }

    public void setNik_user(String idakun) {
        this.nik_user = idakun;
    }

    public String getRt_rw() {
        return rt_rw;
    }

    public void setRt_rw(String alamat_user) {
        this.rt_rw = alamat_user;
    }

    public String getPassword_user() {
        return password_user;
    }

    public void setPassword_userl(String email) {
        this.password_user = email;
    }

    public String getNama_user() {
        return nama_user;
    }

    public void setNama_user(String nama_user) {
        this.nama_user = nama_user;
    }

    public String getNo_rumah() {
        return no_rumah;
    }

    public void setNo_rumah(String no_rumah) {
        this.no_rumah = no_rumah;
    }
//
//    public String getNotelp_user() {
//        return notelp_user;
//    }
//
//    public void setNotelp_user(String notelp_user) {
//        this.notelp_user = notelp_user;
//    }
//
//
//    public String getUserfoto() {
//        return userfoto;
//    }
//
//    public void setUserfoto(String userfoto) {
//        this.userfoto = userfoto;
//    }
}

