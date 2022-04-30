package com.example.newag.intercept;

import static com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newag.mvp.ui.login.LoginActivity;
import com.example.newag.mvp.ui.login.LoginActivity_ViewBinding;
import com.example.newag.utils.SharedPreferencesUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LoginIntercept extends AppCompatActivity implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = SharedPreferencesUtil.getInstance().getString("token", "null");
        Log.e("intercept: ", token);
        Request request=chain.request().newBuilder()
                .addHeader("token",token)
                .build();
        return chain.proceed(request);
    }
}
