package com.example.SiAntik;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitEndPoint {
    @FormUrlEncoded
    @POST("loginMobile.php")
    Call<UserResponse> login (
            @Field("nama_user") String email,
            @Field("password_user") String password
    );

    @FormUrlEncoded
    @POST("registerMobile1.php")
    Call<UserResponse> register(
            @Field("nik_user") String nik_user,
            @Field("nama_user") String nama_user,
            @Field("rt_rw") String rt_rw,
            @Field("no_rumah") String no_rumah,
            @Field("password_user") String password_user
    );

    @FormUrlEncoded
    @POST("lupaSandiUser.php")
    Call<PasswordResetResponse> resetPassword(
            @Field("nik_user") String nikUser,
            @Field("nama_user") String namaUser,
            @Field("new_password") String newPassword
    );

    @GET("data_laporan.php")
    Call<StatusData> getStatusData();

    @GET("getStatusCounts.php")
    Call<List<MonthlyStatusCount>> getMonthlyStatusCount();

    @FormUrlEncoded
    @POST("uploadGambar.php")
    Call<Void> uploadImage(
            @Field("nik_user") String nik_user,
            @Field("foto") String imageData, // Data gambar dalam format base64
            @Field("deskripsi") String description // Deskripsi gambar
    );


    @GET("getNIK.php")
    Call<NIKResponse> getNIK(@Query("nama_user") String nama_user);

    @FormUrlEncoded
    @POST("updateProfile.php")
    Call<UserResponse> updateProfile(
            @Field("nik_user") String nik_user,
            @Field("nama_user") String nama_user,
            @Field("rt_rw") String rt_rw,
            @Field("no_rumah") String no_rumah
    );

    @FormUrlEncoded
    @POST("cekLaporan.php")
    Call<StatusData1> getStatusData(
            @Field("nik_user") String nik_user);


    @FormUrlEncoded
    @POST("getLaporanData.php")
    Call<List<LaporanData>> getLaporanData(
            @Field("nik_user") String nik_user);

    @FormUrlEncoded
    @POST("getDetailLaporan.php")
    Call<List<LaporanData>> getDetailLaporan(
            @Field("id_laporan") String id_laporan);

    @FormUrlEncoded
    @POST("hapusLaporan.php")
    Call<LaporanResponse> hapusLaporan(
            @Field("id_laporan") String id_laporan
    );
}



