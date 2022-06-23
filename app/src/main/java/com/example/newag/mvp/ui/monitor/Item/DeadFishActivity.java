package com.example.newag.mvp.ui.monitor.Item;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;
import com.example.newag.intercept.LoginIntercept;
import com.example.newag.mvp.adapter.BucketAdapter;
import com.example.newag.mvp.model.bean.Icon;
import com.example.newag.mvp.model.bean.Item;
import com.example.newag.mvp.ui.monitor.ItemActivity;
import com.example.newag.mvp.ui.plus.DeadFishPlusActivity;
import com.example.newag.mvp.ui.plus.ReduceFishPlusActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DeadFishActivity extends BaseActivity {
    private Context mContext;
    private ListView list;
    private BucketAdapter mAdapter = null;
    private ArrayList<Item> mData = null;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @OnClick(R.id.plus)
    void onclick(){
        Intent intent=new Intent(DeadFishActivity.this, DeadFishPlusActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("ID", getMidId());
        intent.putExtras(bundle);
        startActivity(intent);
    }
    @Override
    protected void initBaseData() {

    }

    @Override
    protected void baseConfigView() {

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        int ID=bundle.getInt("ID");
        setMidId(ID);
        getData(ID);
        refreshLayout.setColorSchemeResources(R.color.blue,R.color.blue);//设置下拉刷新主题（最多支持三种颜色变换，这里两种)
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(false);
            getData(ID);
        });
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_dead_fish;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }
    private void getData(int id){
        mContext=DeadFishActivity.this;
        list = findViewById(R.id.list);
        mAdapter = new BucketAdapter<Item>(mData, R.layout.item_list) {
            @Override
            public void bindView(ViewHolder holder, Item obj) {
                holder.setText(R.id.time,obj.getTime());
                holder.setText(R.id.num, "死鱼数量："+obj.getNum());
                holder.setText(R.id.type,"死鱼种类："+obj.getName());
                holder.setText(R.id.reason,"死鱼原因："+obj.getReason());
            }
        };
        list.setAdapter(mAdapter);
        mData = new ArrayList<Item>();
        mData.clear();
        OkHttpClient httpClient=new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept())
                .build();
        Request requestPond=new Request.Builder()
                .get()
                .url("http://124.222.111.61:9000/daily//ponds/queryDeadRecord/"+id)
                .build();
        System.out.println("http://124.222.111.61:9000/daily//ponds/queryDeadRecord/"+id);
        new Thread(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                try {
                    Call call = httpClient.newCall(requestPond);
                    Response response = call.execute();
                    assert response.body() != null;
                    String responseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);Log.e("run: ", responseData);
                    JSONArray pond= jsonObject.getJSONArray("data");

                    for (int i = 0; i < pond.length(); i++) {
                        JSONObject jsonObject1 = (JSONObject) pond.get(i);
                        String name = jsonObject1.getString("unitName");
                        String num = jsonObject1.getString("num");
                        String reason=jsonObject1.getString("reason");
                        String time=jsonObject1.getString("time");
                        System.out.println(name+reason+time);
                        mData.add(new Item(num,reason,time,name));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        DeadFishActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}