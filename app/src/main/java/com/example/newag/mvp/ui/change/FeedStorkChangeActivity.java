package com.example.newag.mvp.ui.change;

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

public class FeedStorkChangeActivity extends BaseActivity {
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.inventory)
    EditText inventory;
    @BindView(R.id.inventoryUnit)
    EditText inventoryUnit;
    @BindView(R.id.factory)
    EditText factory;
    @BindView(R.id.note)
    EditText note;
    @BindView(R.id.commit)
    Button commit;

    @Override
    protected void initBaseData() {
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postSync();
            }
        });
    }

    @Override
    protected void baseConfigView() {

    }

    @Override
    protected int layoutId() {
        return R.layout.activity_changestork;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }
    public void postSync() {
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        int id= (int) bundle.getSerializable("data");
        String Id=String.valueOf(id);
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("id", Id);
        paramsMap.put("name", String.valueOf(name.getText()));
        paramsMap.put("inventory", String.valueOf(inventory.getText()));
        paramsMap.put("inventoryUnit", String.valueOf(inventoryUnit.getText()));
        paramsMap.put("factory", String.valueOf(factory.getText()));
        paramsMap.put("note", String.valueOf(note.getText()));
        paramsMap.put("type", "feed");
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
                .url("http://124.222.111.61:9000/daily/input/update")
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
                        Toast.makeText(FeedStorkChangeActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        if(jsonObject1.getString("msg").equals("更新成功")){
                            FeedStorkChangeActivity.this.finish();
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
