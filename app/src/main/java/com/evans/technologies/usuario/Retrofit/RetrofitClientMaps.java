package com.evans.technologies.usuario.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClientMaps {

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/directions/";
    private static RetrofitClientMaps mInstance;
    private Retrofit retrofit;

    private RetrofitClientMaps(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClientMaps getInstance(){
        if(mInstance == null){
            mInstance = new RetrofitClientMaps();
        }
        return mInstance;
    }

    public apiMaps getApi(){
        return retrofit.create(apiMaps.class);
    }

}
