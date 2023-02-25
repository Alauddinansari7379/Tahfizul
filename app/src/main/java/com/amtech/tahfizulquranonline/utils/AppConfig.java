package com.amtech.tahfizulquranonline.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shourav Paul on 01-01-2022.
 **/
public class AppConfig {
    private static String BASE_URL = "https://www.tahfizulquranonline.com/";
    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
