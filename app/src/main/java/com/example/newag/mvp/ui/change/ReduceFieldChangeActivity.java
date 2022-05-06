package com.example.newag.mvp.ui.change;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

public class ReduceFieldChangeActivity extends BaseActivity {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.name)
    EditText name;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.length1)
    EditText length1;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.length2)
    EditText length2;
    @BindView(R.id.width1)
    EditText width1;
    @BindView(R.id.width2)
    EditText width2;
    @BindView(R.id.orientation)
    EditText orientation;
    @BindView(R.id.position)
    EditText position;
    @BindView(R.id.production)
    EditText production;
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
        String Id=String.valueOf(id);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> paramsMap = new HashMap<>();
                paramsMap.put("id", Id);
                paramsMap.put("name", String.valueOf(name.getText()));
                paramsMap.put("width", String.valueOf(width1.getText()));
                paramsMap.put("widthUsed", String.valueOf(width2.getText()));
                paramsMap.put("length", String.valueOf(length1.getText()));
                paramsMap.put("lengthUsed", String.valueOf(length2.getText()));
                paramsMap.put("orientation", String.valueOf(orientation.getText()));
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
                        .url("http://124.222.111.61:9000/daily/field/update/")
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
                                Toast.makeText(ReduceFieldChangeActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                                if(jsonObject1.getString("msg").equals("更新成功")){
                                    ReduceFieldChangeActivity.this.finish();
                                }
                                Looper.loop();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }).start();
                    }
                });
            }
        });
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_changereducefield;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }
}
