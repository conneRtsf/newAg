package com.example.newag.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.example.newag.base.Constant;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class RxCrashUtils implements UncaughtExceptionHandler {

    private volatile static RxCrashUtils mInstance;

    private UncaughtExceptionHandler mHandler;
    private boolean                  mInitialized;
//    private String crashDir;
    private String versionName;
    private int                      versionCode;

    private Context context;
    private RxCrashUtils(Context context) {
        this.context = context;
    }

    /**
     * 获取单例
     * <p>在Application中初始化{@code RxCrashUtils.getInstance().init(this);}</p>
     *
     * @return 单例
     */
    public static RxCrashUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (RxCrashUtils.class) {
                if (mInstance == null) {
                    mInstance = new RxCrashUtils(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化
     *
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public boolean init() {
        if (mInitialized) return true;
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//            crashDir = context.getExternalCacheDir().getPath() + File.separator + "crash" + File.separator;
//        } else {
//            crashDir = context.getCacheDir().getPath() + File.separator + "crash" + File.separator;
//        }
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            versionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        mHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        return mInitialized = true;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable throwable) {
        String now = new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        final String path = Constant.CRASH_FILE_DIR;
        final String fullPath = Constant.CRASH_FILE_DIR+now+".txt";
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
        }else {
            FileUtils.createFile(new File(fullPath));
        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(new FileWriter(fullPath, false));
                    pw.write(getCrashHead());
                    throwable.printStackTrace(pw);
                    Throwable cause = throwable.getCause();
                    while (cause != null) {
                        cause.printStackTrace(pw);
                        cause = cause.getCause();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    RxFileUtils.closeIO(pw);
                }
            }
        }).start();
        if (mHandler != null) {
            mHandler.uncaughtException(thread, throwable);
        }
    }

    /**
     * 获取崩溃头
     *
     * @return 崩溃头
     */
    private String getCrashHead() {
        return "\n************* Crash Log Head ****************" +
                "\nDevice Manufacturer: " + Build.MANUFACTURER +// 设备厂商
                "\nDevice Model       : " + Build.MODEL +// 设备型号
                "\nAndroid Version    : " + Build.VERSION.RELEASE +// 系统版本
                "\nAndroid SDK        : " + Build.VERSION.SDK_INT +// SDK版本
                "\nApp VersionName    : " + versionName +
                "\nApp VersionCode    : " + versionCode +
                "\n************* Crash Log Head ****************\n\n";
    }
}