package com.example.SiAntik.cadangan;

import com.example.SiAntik.ImageModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("loginMobile.php") // Sesuaikan dengan endpoint login Anda
    Call<User> loginUser(
            @Field("nama_user") String nikUser,
            @Field("password") String password
    );

    @POST("upload_image.php") // Ganti dengan endpoint upload_image Anda
    Call<Void> uploadImage(@Body ImageModel image);
}

