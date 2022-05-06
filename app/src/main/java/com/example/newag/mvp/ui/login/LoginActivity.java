package com.example.newag.mvp.ui.login;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;
import com.example.newag.mvp.ui.main.MainActivity;
import com.example.newag.utils.SharedPreferencesUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {
    Handler mHandler;
    private static String token;
    @BindView(R.id.enter1)
    EditText editText1;
    @BindView(R.id.enter2)
    EditText editText2;
    @BindView(R.id.btn1)
    Button log;
    @BindView(R.id.btn2)
    Button Register;

    @Override
    protected void initBaseData() {

    }

    @SuppressLint("HandlerLeak")
    @SuppressWarnings("deprecation")
    @Override
    protected void baseConfigView() {
        log.setOnClickListener(this::postSync);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        Log.e( "handleMessage: ", (String) msg.obj);
                        SharedPreferencesUtil.init(LoginActivity.this,"com.example.newag",MODE_PRIVATE);
                        SharedPreferencesUtil.getInstance().putString("token",(String) msg.obj).commit();
                }
            }
        };

        Register.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_log;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }

    private OkHttpClient buildHttpClient() {

        return new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public void postSync(View view) {

        android.util.Log.e("postSync: ", String.valueOf(editText1.getText()));
        android.util.Log.e("postSync: ", String.valueOf(editText2.getText()));
        final String uname = editText1.getText().toString();
        final String pwd = editText2.getText().toString();
        if(detailsCheck(uname, pwd)) {
        } else {
            Toast.makeText(LoginActivity.this,"账号或密码为空", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("password", String.valueOf(editText2.getText()));
        paramsMap.put("username", String.valueOf(editText1.getText()));
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            builder.add(key, Objects.requireNonNull(paramsMap.get(key)));
        }
        RequestBody formBody = builder.build();

        OkHttpClient httpClient = buildHttpClient();
        Request request = new Request.Builder().url("http://124.222.111.61:9000/basic/user/login")
                .post(formBody)
                .addHeader("Connection", "close")
                .addHeader("content-type", "application/json;charset:utf-8")
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                String ResponseData = response.body().string();
                android.util.Log.e("onResponse: ", ResponseData);
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject1 = new JSONObject(ResponseData);
                            Toast.makeText(LoginActivity.this, jsonObject1.getString("msg"),Toast.LENGTH_SHORT).show();
                            if(jsonObject1.getString("msg").equals("登录成功")){
                                Intent intent = new Intent();
                                token=jsonObject1.getJSONObject("data").getString("token");
                                Message message = new Message();
                                message.what = 1;
                                message.obj=token;
                                mHandler.sendMessage(message);
                                Log.e("abc: ", token);
                                Log.e("run: ", jsonObject1.getJSONObject("data").getString("token"));
                                intent.setClass(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                LoginActivity.this.finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                });
            }
        });
    }
    public Boolean detailsCheck(String uname, String pwd) {

        if(uname.isEmpty()||pwd.isEmpty()) {
            return false;
        } else {
            return true;
        }

    }

}
