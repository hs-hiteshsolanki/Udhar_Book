package com.ub.udharbook.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static String BASE_URL="http://192.168.2.68/udhar_book_api/";
//    private static String BASE_URL="https://udharbook.000webhostapp.com/Udhar_book_Api/";
    private static RetrofitClient retrofitClient;
    private static Retrofit retrofit;

    private RetrofitClient(){
        retrofit =new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
    public static synchronized RetrofitClient getInstance(){
        if(retrofitClient==null){
            retrofitClient=new RetrofitClient();
        }
        return retrofitClient;
    }
    public Api getApi(){
        return retrofit.create(Api.class);
    }
}
