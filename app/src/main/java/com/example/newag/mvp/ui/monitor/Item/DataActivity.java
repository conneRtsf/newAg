package com.example.newag.mvp.ui.monitor.Item;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;
import com.example.newag.intercept.LoginIntercept;
import com.example.newag.mvp.ui.monitor.ItemActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DataActivity extends BaseActivity {
    @OnClick(R.id.tb2)
    void  onClick2(){
        finish();
    }
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.radius)
    TextView radius;
    @BindView(R.id.height)
    TextView height;
    @BindView(R.id.volume)
    TextView volume;
    @BindView(R.id.batch)
    TextView batch;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.fish)
    TextView fish;
    @BindView(R.id.feed)
    TextView feed;
    @Override
    protected void initBaseData() {

    }

    @Override
    protected void baseConfigView() {
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        int ID=bundle.getInt("ID");
        getItem(ID);
        getItemW(ID);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_data;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }
    private void getItemW(int id){
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept())
                .build();
        Request requestPond = new Request.Builder()
                .get()
                .url("http://124.222.111.61:9000/daily/period/queryPeriodById/"+id)
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
                    System.out.println(jsonObject);
                    JSONObject fin = jsonObject.getJSONObject("data");
                    DataActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                fish.setText(fin.getString("num"));
                                feed.setText(fin.getString("feedNum"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
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
                    DataActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                name.setText(fin.getString("name")+"数据展示");
                                location.setText(fin.getString("location"));
                                radius.setText(fin.getString("radius"));
                                height.setText(fin.getString("height"));
                                volume.setText(fin.getString("volume"));
                                batch.setText(fin.getString("batch"));
                                type.setText(fin.getString("product"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    Log.e("run: ", responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}