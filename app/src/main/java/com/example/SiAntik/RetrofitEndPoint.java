package com.example.SiAntik;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitEndPoint {
    @FormUrlEncoded
    @POST("loginMobile.php")
    Call<UserResponse> login (
            @Field("nama_user") String email,
            @Field("password_user") String password
    );
}