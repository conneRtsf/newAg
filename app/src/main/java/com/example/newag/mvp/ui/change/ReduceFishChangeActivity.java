package com.example.newag.mvp.ui.change;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;
import com.example.newag.intercept.LoginIntercept;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReduceFishChangeActivity extends BaseActivity {
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.radius)
    EditText radius;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.height1)
    EditText height1;
    @BindView(R.id.height2)
    EditText height2;
    @BindView(R.id.position)
    EditText position;
    @BindView(R.id.production)
    EditText production;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.commit)
    Button commit;

    @Override
    protected void initBaseData() {

    }

    @Override
    protected void baseConfigView() {
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        int id= (int) bundle.getSerializable("data");
        System.out.println(id);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> paramsMap = new HashMap<>();
                paramsMap.put("name", String.valueOf(name.getText()));
                paramsMap.put("height", String.valueOf(height1.getText()));
                paramsMap.put("heightUsed", String.valueOf(height2.getText()));
                paramsMap.put("radius", String.valueOf(radius.getText()));
                paramsMap.put("location", String.valueOf(position.getText()));
                paramsMap.put("product", String.valueOf(production.getText()));
                FormBody.Builder builder = new FormBody.Builder();
                for (String key : paramsMap.keySet()) {
                    builder.add(key, Objects.requireNonNull(paramsMap.get(key)));
                }
                RequestBody formBody = builder.build();
                OkHttpClient httpClient = new OkHttpClient.Builder()
                        .addInterceptor(new LoginIntercept())
                        .build();
                Request request = new Request.Builder()
                        .put(formBody)
                        .url("http://124.222.111.61:9000/daily/ponds/update/"+id)
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
                        new Thread(() -> {
                            try {
                                Looper.prepare();
                                JSONObject jsonObject1 = new JSONObject(ResponseData);
                                Toast.makeText(ReduceFishChangeActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                                if(jsonObject1.getString("msg").equals("修改成功")){
                                    ReduceFishChangeActivity.this.finish();
                                }
                                Looper.loop();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }).start();
                    }
                });
                finish();
            }
        });
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_changereducefish;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }
}