package com.example.newag.mvp.ui.monitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;
import com.example.newag.intercept.LoginIntercept;
import com.example.newag.mvp.ui.monitor.Item.DataActivity;
import com.example.newag.mvp.ui.monitor.Item.DeadFishActivity;
import com.example.newag.mvp.ui.monitor.Item.FeedSowActivity;
import com.example.newag.mvp.ui.monitor.Item.SowActivity;
import com.example.newag.mvp.ui.plus.ReduceFieldPlusActivity;
import com.example.newag.mvp.ui.reduce.ReduceFishAddActivity;

import org.json.JSONArray;
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

public class ItemActivity extends BaseActivity {
    private int midId;

    public int getMidId() {
        return midId;
    }

    public void setMidId(int midId) {
        this.midId = midId;
    }
    @OnClick(R.id.feedSow)
    void o3(){
        Intent intent=new Intent(ItemActivity.this, FeedSowActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("ID", getMidId());
        intent.putExtras(bundle);
        startActivity(intent);
    }
    @OnClick(R.id.fishSow)
    void on2(){
        Intent intent=new Intent(ItemActivity.this, SowActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("ID", getMidId());
        intent.putExtras(bundle);
        startActivity(intent);
    }
    @OnClick(R.id.deadFish)
    void on(){
        Intent intent=new Intent(ItemActivity.this, DeadFishActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("ID", getMidId());
        intent.putExtras(bundle);
        startActivity(intent);
    }
    @OnClick(R.id.change)
    void onclick2(){
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("id", String.valueOf(getMidId()));
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
                .url("http://124.222.111.61:9000/daily/operation/dividePonds")
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
                        Toast.makeText(ItemActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        if(jsonObject1.getString("msg").equals("分桶成功")){
                            ItemActivity.this.finish();
                        }
                        Looper.loop();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });
    }
    @OnClick(R.id.data)
    void onclick(){
        Intent intent=new Intent(ItemActivity.this, DataActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("ID", getMidId());
        intent.putExtras(bundle);
        startActivity(intent);
    }
    @OnClick(R.id.delete)
    void onclick1(){
        OkHttpClient httpClient=new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept()).build();
        Request request=new Request.Builder()
                .delete()
                .url("http://124.222.111.61:9000/daily/ponds/delete/"+getMidId())
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call call = httpClient.newCall(request);
                    Response response = call.execute();
                    assert response.body() != null;
                    String responsePond = response.body().string();
                    Log.e("run: ", responsePond);
                    JSONObject jsonObject = new JSONObject(responsePond);
                    String fd=jsonObject.getString("msg");
                    ItemActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ItemActivity.this, fd,Toast.LENGTH_SHORT).show();
                            if(fd.equals("删除成功")){
                                finish();
                            }
                        }
                    });
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @OnClick(R.id.tb2)
    void  onClick2(){
        finish();
    }
    @BindView(R.id.name)
    TextView name;
    @Override
    protected void initBaseData() {

    }

    @Override
    protected void baseConfigView() {
        //获取上一个页面传递过来的参数
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        int RTableCategoryID=bundle.getInt("RTableCategoryID");
        setMidId(RTableCategoryID);
        getItem(RTableCategoryID);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_item;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }
    private void getItem(int id) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept())
                .build();
        Request requestPond = new Request.Builder()
                .get()
                .url("http://124.222.111.61:9000/daily/ponds/query/"+id)
                .build();
        new Thread(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                try {
                    Call call = httpClient.newCall(requestPond);
                    Response response = call.execute();
                    assert response.body() != null;
                    String responseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONObject fin=jsonObject.getJSONObject("data");
                    ItemActivity.this.runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            try {
                                name.setText(fin.getString("name")+"桶");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    setMidId(fin.getInt("id"));
                    Log.e("run: ", responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}