/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.newag.utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.newag.BuildConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.example.newag.MyApplication;
import com.example.newag.utils.SharedPreferencesUtil;

/**
 * @className:
 * @author: neymar
 * @createDate: 2021/8/8
 * @description:
 */
public final class UMInitUtil {

    private UMInitUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static String DEFAULT_CHANNEL_ID = "";

    /**
     * 初始化SDK,合规指南【先进行预初始化，如果用户隐私同意后可以初始化UmengSDK进行信息上报】
     */
    public static void init() {
        init(MyApplication.getMyApplication());
    }

    /**
     * 初始化SDK,合规指南【先进行预初始化，如果用户隐私同意后可以初始化UmengSDK进行信息上报】
     */
    public static void init(@NonNull Context context) {
        Context appContext = context.getApplicationContext();
        if (appContext instanceof Application) {
            init((Application) appContext);
        }
    }

    /**
     * 初始化SDK,合规指南【先进行预初始化，如果用户隐私同意后可以初始化UmengSDK进行信息上报】
     */
    public static void init(Application application) {
        // 运营统计数据调试运行时不初始化
        UMConfigure.setLogEnabled(false);
        UMConfigure.preInit(application, "6263f0ff30a4f67780b3137e","");
        Log.d("init umeng", "preInit");
        // 用户同意了隐私协议
        boolean welcomedialog = SharedPreferencesUtil.getInstance().getBoolean("welcomedialog", false);
        Log.d("init umeng", "isAgreed = " + welcomedialog);
        if (welcomedialog) {
            realInit(application);
        }
    }

    /**
     * 真实的初始化UmengSDK【进行设备信息的统计上报，必须在获得用户隐私同意后方可调用】
     */
    private static void realInit(Application application) {
        UMConfigure.init(application, "6263f0ff30a4f67780b3137e", BuildConfig.FLAVOR,  UMConfigure.DEVICE_TYPE_PHONE,"");
        Log.e("init umeng", "realInit");
        //统计SDK是否支持采集在子进程中打点的自定义事件，默认不支持
        //支持多进程打点
        UMConfigure.setProcessEvent(true);
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
    }

    /**
     * 获取渠道信息
     *
     * @param context
     * @return
     */
//    public static String getChannel(final Context context) {
//        return XX.getChannel(context, DEFAULT_CHANNEL_ID);
//    }
}
