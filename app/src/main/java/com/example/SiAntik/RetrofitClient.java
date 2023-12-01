package com.example.SiAntik;

import androidx.annotation.NonNull;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



import java.util.concurrent.TimeUnit;
public class RetrofitClient {

//    public static final String BASE_URL = "http://192.168.137.1:8080/siantik/mobile/";

    public static final String BASE_URL = "https://si-antik.tifnganjuk.com/SI-antik/mobile/";

    public static final String USER_PHOTO_URL = BASE_URL + "public/img/user-photo/";

    public static final String SUCCESSFUL_RESPONSE = "success";



    /**
     * connect to the rest server
     */
    public static Retrofit getConnection() {

        Gson gson = new GsonBuilder().setLenient().create();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }


}
