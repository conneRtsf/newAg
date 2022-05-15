package com.example.newag.mvp.model.api.service;

import com.example.newag.mvp.model.bean.SalesAccountingTranslation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SalesAccountingApiService {
    @GET("daily/sales/querySales")
    Call<SalesAccountingTranslation> SalesAccounting();
    @GET("daily/sales/querySales")
    Call<SalesAccountingTranslation> SalesAccounting(@Query("endTime") String endTime,@Query("startTime") String startTime);
    @DELETE("daily/sales/deleteSale/{id}")
    Call<SalesAccountingTranslation> Delete(@Path("id") int id);
}
