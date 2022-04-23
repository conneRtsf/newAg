package com.example.newag;


import com.example.newag.base.BaseApplication;
import com.example.newag.di.component.AppComponent;
import com.example.newag.di.component.DaggerAppComponent;
import com.example.newag.di.module.AppModule;
import com.example.newag.di.module.AquApiModule;
import com.example.newag.utils.SdkDelayInitUtil;
import com.example.newag.utils.SharedPreferencesUtil;
import com.example.newag.utils.UMInitUtil;

public class MyApplication extends BaseApplication {

    private static MyApplication myApplication;

    public static MyApplication getMyApplication() {
        return myApplication;
    }

    private AppComponent appComponent;
    public boolean welcomedialogshowed = false;

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
        SharedPreferencesUtil.init(this,"com.example.newag",MODE_PRIVATE);
        UMInitUtil.init(this);
        SdkDelayInitUtil.getInstance().init();
        initComponent();
    }
}
