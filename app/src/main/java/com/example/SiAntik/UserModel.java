package com.example.SiAntik;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserModel {

    @Expose
    @SerializedName("nik_user")
    private String idakun;
    @Expose
    @SerializedName("nama_user")
    private String nama_user;
    @Expose
    @SerializedName("rt_rw")
    private String rt_rw;
    @Expose
    @SerializedName("password_user")
    private String Password;
//    @Expose
//    @SerializedName("alamat_user")
//    private String Alamat_user;
//    @Expose
//    @SerializedName("no.telp_user")
//    private String notelp_user;
//    @Expose
//    @SerializedName("user_foto")
//    private String userfoto;

    public UserModel(String idakun, String nama, String rt_rw, String pass
//                     String alamatuser, String notelpuser, String userfoto
    ) {
        this.idakun = idakun;
        this.nama_user = nama;
        this.Password = pass;
        this.rt_rw = rt_rw;
//        this.Alamat_user = alamatuser;
//        this.notelp_user = notelpuser;
//        this.userfoto = userfoto;
    }

    public String getIdakun() {
        return idakun;
    }

    public void setIdakun(String idakun) {
        this.idakun = idakun;
    }

    public String getRt_rw() {
        return rt_rw;
    }

    public void setRt_rw(String alamat_user) {
        this.rt_rw = alamat_user;
    }

    public String getEmail() {
        return Password;
    }

    public void setEmail(String email) {
        this.Password = email;
    }

    public String getNama_user() {
        return nama_user;
    }

    public void setNama_user(String nama_user) {
        this.nama_user = nama_user;
    }

//    public String getPassword() {
//        return Alamat_user;
//    }
//
//    public void setPassword(String password) {
//        this.Alamat_user = password;
//    }
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

