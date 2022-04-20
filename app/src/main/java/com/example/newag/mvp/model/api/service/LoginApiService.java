package com.example.newag.mvp.model.api.service;

import android.icu.text.IDNA;

import com.example.newag.mvp.model.bean.ReqBackData;

import java.util.Map;

import io.reactivex.Single;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface LoginApiService {

//        @FormUrlEncoded
//        @POST("api/comments.163")
//        Call<Object> postDataCall(@Field("format") String format);

        @FormUrlEncoded
        @POST("basic/user/login")
        Call<ResponseBody> getPostData1(@FieldMap Map<String, String> map);

        @FormUrlEncoded
        @POST("basic/user/login")
        Call<ReqBackData<IDNA.Info>> getPostData2(@FieldMap Map<String, String> map);

}
