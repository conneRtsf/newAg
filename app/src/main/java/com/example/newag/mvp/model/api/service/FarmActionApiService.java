package com.example.newag.mvp.model.api.service;

import com.example.newag.mvp.model.bean.SalesAccountingTranslation;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FarmActionApiService {
    @GET("daily/queryOperation")
    Call<SalesAccountingTranslation> queryOperation();
    @GET("daily/queryOperation")
    Call<SalesAccountingTranslation> queryOperation(@Query("endTime") String endTime, @Query("startTime") String startTime);
    @DELETE("daily/deleteOperation/{id}")
    Call<SalesAccountingTranslation> Delete(@Path("id") int id);

}
