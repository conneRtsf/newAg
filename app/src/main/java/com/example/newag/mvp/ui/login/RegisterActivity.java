package com.example.newag.mvp.ui.login;

import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void initBaseData() {

    }

    @Override
    protected void baseConfigView() {
        Button register = findViewById(R.id.btn1);
        register.setOnClickListener(this::postSync);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }

    private OkHttpClient buildHttpClient() {
        return new OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(30, TimeUnit.SECONDS).build();
    }

    public void postSync(View view) {

        EditText editText1 = findViewById(R.id.enter1);
        EditText editText2 = findViewById(R.id.enter2);
        EditText editText3 = findViewById(R.id.enter3);
        android.util.Log.e("postSync: ", String.valueOf(editText1.getText()));
        android.util.Log.e("postSync: ", String.valueOf(editText2.getText()));
        android.util.Log.e("postSync: ", String.valueOf(editText3.getText()));
        OkHttpClient httpClient = buildHttpClient();

        final String uname = editText1.getText().toString();
        final String pwd = editText2.getText().toString();
        final String icode = editText3.getText().toString();

        if(detailsCheck(uname, pwd, icode)) {

        } else {
            Toast.makeText(RegisterActivity.this,"账号、密码或邀请码为空", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("password", String.valueOf(editText2.getText()));
        paramsMap.put("username", String.valueOf(editText1.getText()));
        paramsMap.put("icode", String.valueOf(editText3.getText()));
        FormBody.Builder builder = new FormBody.Builder();

        for (String key : paramsMap.keySet()) {
            builder.add(key, Objects.requireNonNull(paramsMap.get(key)));
        }

        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url("http://ctos17.free.idcfengye.com/basic/user/register")
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
                Log.e("onResponse: ", ResponseData);
                RegisterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject1 = new JSONObject(ResponseData);
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(RegisterActivity.this);
                            builder1.setMessage(jsonObject1.getString("msg"));
                            builder1. setPositiveButton("确定", (dialog, which) -> {
                            });
                            AlertDialog alert = builder1.create();
                            alert.show();
                            if(jsonObject1.getString("msg").equals("注册成功")){
                                RegisterActivity.this.finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public Boolean detailsCheck(String uname, String pwd, String icode) {

        if(uname.isEmpty()||pwd.isEmpty()||icode.isEmpty()) {
            return false;
        } else {
            return true;
        }

    }

}
