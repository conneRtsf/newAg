package com.example.newag.di.component;

import android.content.Context;

import com.example.newag.di.module.AppModule;
import com.example.newag.di.module.AquApiModule;
import com.example.newag.mvp.model.api.ProjectApi;

import javax.inject.Named;

import dagger.Component;


@Component(modules = {AppModule.class, AquApiModule.class})
public interface AppComponent {

    Context getContext();

    //MyLog Gson解析
    @Named("api-0")
    ProjectApi getReaderApi();

    //生产端Log Gson解析
    @Named("api-1")
    ProjectApi getReaderApi1();

    //销售端Log Gson解析
    @Named("api-2")
    ProjectApi getReaderApi2();

    //MyLogLog String解析
    @Named("api-stringconver")
    ProjectApi getReaderApi3();

    //生产端 String解析
    @Named("api-4")
    ProjectApi getReaderApi4();

    //销售端Log String解析
    @Named("api-5")
    ProjectApi getReaderApi5();
}
