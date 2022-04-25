package com.example.newag.mvp.model.api.service;

import com.example.newag.mvp.model.bean.LoginTranslation;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginApiService {

        @FormUrlEncoded
        @POST("basic/user/login")
        Call<LoginTranslation> login(@Field("username") String username, @Field("password") String password);

}
