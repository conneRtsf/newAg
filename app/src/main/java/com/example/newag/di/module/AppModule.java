package com.example.newag.di.module;

import android.content.Context;

import com.example.newag.MyApplication;

import dagger.Module;
import dagger.Provides;


@Module
public class AppModule {

    private Context context;

    public AppModule(MyApplication context) {
        this.context = context;
    }

    @Provides
    public Context provideContext() {
        return context;
    }
}
