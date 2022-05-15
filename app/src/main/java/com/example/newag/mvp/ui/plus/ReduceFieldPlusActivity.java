package com.example.newag.mvp.ui.plus;

import android.annotation.SuppressLint;
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
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReduceFieldPlusActivity extends BaseActivity {
    @OnClick(R.id.tb2)
    void  onClick2(){
        finish();
    }
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
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postSync();
            }
        });
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_plusreducefield;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }
    public void postSync() {
        HashMap<String, String> paramsMap = new HashMap<>();
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
                .post(formBody)
                .url("http://124.222.111.61:9000/daily/field/add")
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
                        Toast.makeText(ReduceFieldPlusActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        if(jsonObject1.getString("msg").equals("添加成功")){
                            ReduceFieldPlusActivity.this.finish();
                        }
                        Looper.loop();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });
    }
}
