package com.example.newag.mvp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;
import com.example.newag.mvp.ui.main.MainActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.enter1)
    EditText editText1;
    @BindView(R.id.enter2)
    EditText editText2;
    @BindView(R.id.btn1)
    Button log;
    @BindView(R.id.btn2)
    Button Register;
    @OnClick(R.id.pass)
    void cl(){
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void initBaseData() {

    }

    @Override
    protected void baseConfigView() {
        log.setOnClickListener(this::postSync);
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

        return new OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(30, TimeUnit.SECONDS).build();

    }

    public void postSync(View view) {

        android.util.Log.e("postSync: ", String.valueOf(editText1.getText()));
        android.util.Log.e("postSync: ", String.valueOf(editText2.getText()));
        OkHttpClient httpClient = buildHttpClient();

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

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://ctos17.free.idcfengye.com/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        LoginApiService request = retrofit.create(LoginApiService.class);
//
//        Call<Translation> call = request.postDataCall("JSON");
//
//        call.enqueue(new Callback<Translation>() {

//            @Override
//            public void onResponse(Call<Translation> call, Response<Translation> response) {
//                Object body = response.body();
//                if (body == null) return;
//                mTextView.setText("info：" + "\n\n" + response.body().toString());
//                Toast.makeText(Login.this, "success", Toast.LENGTH_SHORT).show();

//            }
//
//            @Override
//            public void onFailure(Call<Translation> call, Throwable throwable) {
//                Log.e(TAG, "info：" + throwable.getMessage() + "," + throwable.toString());
//                Toast.makeText(Login.this, "error", Toast.LENGTH_SHORT).show();
//            }
//        });
//
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url("http://ctos17.free.idcfengye.com/basic/user/login")
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
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                            builder1.setMessage(jsonObject1.getString("msg"));
                            builder1. setPositiveButton("确定", (dialog, which) -> {
                            });
                            AlertDialog alert = builder1.create();
                            alert.show();
                            if(jsonObject1.getString("msg").equals("登录成功")){
                                Thread.sleep(1500);
                                Intent intent = new Intent();
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
