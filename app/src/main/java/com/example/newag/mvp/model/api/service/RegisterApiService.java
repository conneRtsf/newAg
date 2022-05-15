package com.example.newag.mvp.model.api.service;

import com.example.newag.mvp.model.bean.RegisterTranslation;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterApiService {

    @FormUrlEncoded
    @POST("basic/user/register")
    Call<RegisterTranslation> register(@Field("username") String username, @Field("password") String password, @Field("icode") String icode);

}
