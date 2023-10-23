package com.example.SiAntik;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitEndPoint {
    @FormUrlEncoded
    @POST("loginMobile.php")
    Call<UserResponse> login (
            @Field("nama_user") String email,
            @Field("password_user") String password
    );

    @FormUrlEncoded
    @POST("registerMobile.php")
    Call<UserResponse> register(
            @Field("nik_user") String nik_user,
            @Field("nama_user") String nama_user,
            @Field("rt_rw") String rt_rw,
            @Field("password_user") String password_user
    );

    @FormUrlEncoded
    @POST("lupaSandiUser.php")
    Call<PasswordResetResponse> resetPassword(
            @Field("nik_user") String nikUser,
            @Field("nama_user") String namaUser,
            @Field("new_password") String newPassword
    );

    @GET("getDataLaporan.php")
    Call<List<getLaporanResponse>> getDataLaporan();
}