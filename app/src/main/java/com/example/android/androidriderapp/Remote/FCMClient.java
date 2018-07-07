package com.example.android.androidriderapp.Remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jesus on 07/07/2018.
 */

public class FCMClient {
    private static Retrofit retrofit;
    public static Retrofit getClient(String baseURL){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
