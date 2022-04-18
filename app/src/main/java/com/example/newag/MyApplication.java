package com.example.newag;


import com.example.newag.base.BaseApplication;
import com.example.newag.di.component.AppComponent;
import com.example.newag.di.component.DaggerAppComponent;
import com.example.newag.di.module.AppModule;
import com.example.newag.di.module.AquApiModule;

public class MyApplication extends BaseApplication {

    private static MyApplication myApplication;

    public static MyApplication getMyApplication() {
        return myApplication;
    }

    private AppComponent appComponent;

    private void initComponent() {
        appComponent = DaggerAppComponent.builder()
                .aquApiModule(new AquApiModule())
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public static String USER_TOKEN = "";

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        initComponent();
    }
}
