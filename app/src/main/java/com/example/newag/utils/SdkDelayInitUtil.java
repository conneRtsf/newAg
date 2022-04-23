package com.example.newag.utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.newag.MyApplication;

/**
 * @className: SdkDelayInit
 * @author:
 * @createDate:
 * @description: SDK延迟初始化
 */
public class SdkDelayInitUtil {

    private static final String TAG = "SdkDelayInit";

    private static volatile SdkDelayInitUtil sdkDelayInitUtil;

    public static SdkDelayInitUtil getInstance() {
        if (sdkDelayInitUtil == null) {
            synchronized (SdkDelayInitUtil.class) {
                if (sdkDelayInitUtil == null) {
                    sdkDelayInitUtil = new SdkDelayInitUtil();
                }
            }
        }
        return sdkDelayInitUtil;
    }

    public SdkDelayInitUtil() {
    }

    public void init() {
        init(MyApplication.getMyApplication());
    }

    private void init(@NonNull Context context) {
        Context appContext = context.getApplicationContext();
        if (appContext instanceof Application) {
            init((Application) appContext);
        }
    }

    private void init(Application application) {

        // 用户同意了隐私协议
        boolean welcomedialog = SharedPreferencesUtil.getInstance().getBoolean("welcomedialog", false);
        Log.d(TAG, "isAgreed = " + welcomedialog);
        if (welcomedialog) {
            realInit(application);
        }
    }

    private void realInit(Application application) {
        Log.e(TAG, "realInit");

        //假设有搜集设备id需要
        ClientId(application);
        //假设有推送sdk
//        MyApplication.getMyApplication().initPushService();
    }

    public void ClientId(Application application) {
        String deviceId = "";

    }

    public void registerPush(Application application) {
        try {

        } catch (Exception e) {
            Log.e("SdkDelayInit", "registerPush err: " + e.toString());
            e.printStackTrace();
        }
    }
}
