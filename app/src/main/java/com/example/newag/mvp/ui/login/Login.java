package com.example.newag.mvp.ui.login;

import static android.content.ContentValues.TAG;

import android.content.Intent;
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
import com.example.newag.mvp.model.api.service.LoginApiService;
import com.example.newag.mvp.ui.main.MainActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    @BindView(R.id.enter1)
    EditText editText1;
    @BindView(R.id.enter2)
    EditText editText2;
    @BindView(R.id.btn1)
    Button log;
    @BindView(R.id.btn2)
    Button Register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        ButterKnife.bind(this);

        log.setOnClickListener(this::postSync);
        Register.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(Login.this, Register.class);
            startActivity(intent);
        });

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
            Toast.makeText(Login.this,"账号或密码为空", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("password", String.valueOf(editText2.getText()));
        paramsMap.put("username", String.valueOf(editText1.getText()));

        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            builder.add(key, Objects.requireNonNull(paramsMap.get(key)));
        }

//        //步骤4:创建Retrofit对象
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://ctos17.free.idcfengye.com/") // 设置网络请求baseUrl
//                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析
//                .build();
//
//        // 步骤5:创建网络请求接口的实例
//        LoginApiService request = retrofit.create(LoginApiService.class);
//
//        //步骤6：对发送请求进行封装:
//        Call<Translation> call = request.postDataCall("JSON");
//
//        //步骤7:发送网络请求(异步)
//        call.enqueue(new Callback<Translation>() {
//            //请求成功时回调
//            @Override
//            public void onResponse(Call<Translation> call, Response<Translation> response) {
//                //步骤8：请求处理,输出结果
//                Object body = response.body();
//                if (body == null) return;
//                mTextView.setText("返回的数据：" + "\n\n" + response.body().toString());
//                Toast.makeText(Login.this, "post回调成功:异步执行", Toast.LENGTH_SHORT).show();
//            }
//
//            //请求失败时回调
//            @Override
//            public void onFailure(Call<Translation> call, Throwable throwable) {
//                Log.e(TAG, "post回调失败：" + throwable.getMessage() + "," + throwable.toString());
//                Toast.makeText(Login.this, "post回调失败", Toast.LENGTH_SHORT).show();
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
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject1 = new JSONObject(ResponseData);
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(Login.this);
                            builder1.setMessage(jsonObject1.getString("msg"));
                            builder1. setPositiveButton("确定", (dialog, which) -> {
                            });
                            AlertDialog alert = builder1.create();
                            alert.show();
                            if(jsonObject1.getString("msg").equals("登录成功")){
                                Thread.sleep(1500);
                                Intent intent = new Intent();
                                intent.setClass(Login.this, MainActivity.class);
                                startActivity(intent);
                                Login.this.finish();
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
