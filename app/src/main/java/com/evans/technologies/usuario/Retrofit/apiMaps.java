package com.evans.technologies.usuario.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface apiMaps {
    @GET
    Call<String> callMaps(@Url String url);

}
