package com.example.newag.base;

import android.os.Environment;

import com.example.newag.MyApplication;

public class Constant {

    public final static String CRASH_FILE_DIR = getCrashFilePath();

    public static String API_BASE_URL = "http://124.222.59.40:7000/";

    public static String getCrashFilePath() {
        String filePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            filePath = MyApplication.getMyApplication().getExternalCacheDir().getPath() + "/";
        } else {
            filePath = MyApplication.getMyApplication().getCacheDir().getPath() + "/";
        }

        return filePath + "crash/";
    }
}
