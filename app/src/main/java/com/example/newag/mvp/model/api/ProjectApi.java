package com.example.newag.mvp.model.api;

import com.example.newag.base.Constant;
import com.example.newag.mvp.model.api.service.LoginApiService;


import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class ProjectApi {

    //MyLog Gson解析
    public static ProjectApi instance0;

    //生产端Log Gson解析
    public static ProjectApi instance1;

    //销售端Log Gson解析
    public static ProjectApi instance2;

    //MyLogLog String解析
    public static ProjectApi instance3;

    //生产端 String解析
    public static ProjectApi instance4;

    //销售端Log String解析
    public static ProjectApi instance5;

    private LoginApiService loginApiService;

    public ProjectApi(OkHttpClient okHttpClient, Converter.Factory convertFactory) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(convertFactory) // 注入转换器
                .client(okHttpClient) //注入客户端
                .build();
        loginApiService = retrofit.create(LoginApiService.class);
    }

    public static ProjectApi getInstance0(OkHttpClient okHttpClient, Converter.Factory convertFactory) {
        if (instance0 == null) {
            instance0 = new ProjectApi(okHttpClient, convertFactory);
        }
        return instance0;
    }

    public static ProjectApi getInstance1(OkHttpClient okHttpClient, Converter.Factory convertFactory) {
        if (instance1 == null)
            instance1 = new ProjectApi(okHttpClient, convertFactory);
        return instance1;
    }

    public static ProjectApi getInstance2(OkHttpClient okHttpClient, Converter.Factory convertFactory) {
        if (instance2 == null)
            instance2 = new ProjectApi(okHttpClient, convertFactory);
        return instance2;
    }

    public static ProjectApi getInstance3(OkHttpClient okHttpClient, Converter.Factory convertFactory) {
        if (instance3 == null)
            instance3 = new ProjectApi(okHttpClient, convertFactory);
        return instance3;
    }

    public static ProjectApi getInstance4(OkHttpClient okHttpClient, Converter.Factory convertFactory) {
        if (instance4 == null)
            instance4 = new ProjectApi(okHttpClient, convertFactory);
        return instance4;
    }

    public static ProjectApi getInstance5(OkHttpClient okHttpClient, Converter.Factory convertFactory) {
        if (instance5 == null)
            instance5 = new ProjectApi(okHttpClient, convertFactory);
        return instance5;
    }

//    public Single<String> login(RequestBody usr, RequestBody pwd) {
//        return loginApiService.login(usr, pwd);
//    }

}
