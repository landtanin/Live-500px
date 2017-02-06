package com.landtanin.practice2.manager.http;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by landtanin on 2/6/2017 AD.
 */

public interface ApiService {

    @POST("list")
    Call<Object>  loadPhotoList();

}
