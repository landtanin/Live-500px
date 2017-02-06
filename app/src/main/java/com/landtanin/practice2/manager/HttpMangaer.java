package com.landtanin.practice2.manager;

import android.content.Context;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;
import com.landtanin.practice2.manager.http.ApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class HttpMangaer {

    private static HttpMangaer instance;

    public static HttpMangaer getInstance() {
        if (instance == null)
            instance = new HttpMangaer();
        return instance;
    }

    private Context mContext;
    private ApiService service;

    private HttpMangaer() {
        mContext = Contextor.getInstance().getContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://nuuneoi.com/courses/500px/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ApiService.class);

    }

    public ApiService getService() {
        return service;
    }

}
