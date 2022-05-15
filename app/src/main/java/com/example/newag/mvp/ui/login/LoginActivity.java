package com.example.newag.mvp.ui.login;


import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;

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
import com.example.newag.mvp.model.api.HttpClientUtils;
import com.example.newag.mvp.model.api.service.LoginApiService;
import com.example.newag.mvp.model.bean.LoginTranslation;
import com.example.newag.mvp.model.bean.RegisterTranslation;
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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
                if (msg.what == 1) {
                    Log.e("handleMessage: ", (String) msg.obj);
                    SharedPreferencesUtil.init(LoginActivity.this, "com.example.newag", MODE_PRIVATE);
                    SharedPreferencesUtil.getInstance().putString("token", (String) msg.obj).commit();
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
        if (detailsCheck(uname, pwd)) {
        } else {
            Toast.makeText(LoginActivity.this, "账号或密码为空", Toast.LENGTH_SHORT).show();
            return;
        }


        Retrofit retrofit = HttpClientUtils.getRetrofitWithGsonAdapter();
        LoginApiService loginApiService = retrofit.create(LoginApiService.class);
        retrofit2.Call<LoginTranslation> call = loginApiService.login(uname, pwd);

        call.enqueue(new retrofit2.Callback<LoginTranslation>() {
            @Override
            public void onResponse(retrofit2.Call<LoginTranslation> call, retrofit2.Response<LoginTranslation> response) {
                LoginTranslation registerTranslation = response.body();
                Integer code = registerTranslation.getCode();
                String msg = registerTranslation.getMsg();
                Object body = response.body();
                if (body == null) return;
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                            if (msg.equals("登录成功")) {
                                Intent intent = new Intent();
                                LoginTranslation.DataDTO outside=registerTranslation.getData();
                                String token=outside.getToken();
                                android.util.Log.e("ServerRet: ", code.toString() + " " + msg);
                                Message message = new Message();
                                message.what = 1;
                                message.obj = token;
                                mHandler.sendMessage(message);
                                Log.e("abc: ", token);
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

            @Override
            public void onFailure(retrofit2.Call<LoginTranslation> call, Throwable throwable) {
                Log.e(TAG, "info：" + throwable.getMessage() + "," + throwable.toString());
                Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
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
