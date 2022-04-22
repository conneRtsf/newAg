package com.example.newag.mvp.model.api.service;

import android.icu.text.IDNA;

import com.example.newag.mvp.model.bean.Data;
import com.example.newag.mvp.model.bean.Translation;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoginApiService {

//        @FormUrlEncoded
//        @POST("api/comments.163")
//        Call<Object> postDataCall(@Field("format") String format);

        @FormUrlEncoded
        @POST("basic/user/login")
        Call<ResponseBody> getPostData1(@FieldMap Map<String, String> map);

        @FormUrlEncoded
        @POST("basic/user/login")
        Call<Data<IDNA.Info>> getPostData2(@FieldMap Map<String, String> map);

        @FormUrlEncoded
        @POST("basic/user/login")
        Call<Object> getPostData3(@FieldMap Map<String, String> map);

//        @FormUrlEncoded
//        @POST("basic/user/login")
//        Call<Translation> login(@FieldMap Map<String, String> map);

        @FormUrlEncoded
        @POST("basic/user/login")
        Call<Translation> login(@Field("username") String username, @Field("password") String password);

}
