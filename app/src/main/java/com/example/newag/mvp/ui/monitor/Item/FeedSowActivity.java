package com.example.newag.mvp.ui.monitor.Item;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;
import com.example.newag.intercept.LoginIntercept;
import com.example.newag.mvp.adapter.AllTextMasterAdapter;
import com.example.newag.mvp.model.bean.AllText;
import com.example.newag.mvp.model.bean.AllTextMaster;
import com.example.newag.mvp.ui.farmwork.RaiseActivity;
import com.example.newag.mvp.ui.plus.RaisePlusActivity;
import com.example.newag.mvp.ui.plus.SowPlusActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FeedSowActivity extends BaseActivity {
    @OnClick(R.id.plus)
    void onClick1(View view) {
        Intent intent=new Intent(FeedSowActivity.this, RaisePlusActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("ID", getMidId());
        intent.putExtras(bundle);
        startActivity(intent);
    }
    @OnClick(R.id.tb2)
    void  onClick2(){
        finish();
    }
    @BindView(R.id.view_one)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.left)
    android.widget.Button Button;
    private final List<AllTextMaster> data_1=new ArrayList<>();//????????????1,????????????
    private PopupWindow newPopWindow;//???
    private AllTextMasterAdapter adapter;
    @Override
    protected void initBaseData() {

    }

    @Override
    protected void baseConfigView() {
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        int ID=bundle.getInt("ID");
        setMidId(ID);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);//????????????????????????cv??????
        recyclerView.setLayoutManager(linearLayoutManager);//???recycleview????????????????????????cv
        adapter=new AllTextMasterAdapter(this,data_1);//?????????????????????????????????AllTextMasterAdapter??????????????????????????????
        recyclerView.setAdapter(adapter);//???????????????
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View rootView;
                View view2= LayoutInflater.from(FeedSowActivity.this).inflate(R.layout.ppw_delete,null);
                newPopWindow=new PopupWindow(view2,RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT,false);
                adapter.setCheckbox(true);
                adapter.notifyDataSetChanged();
                rootView= LayoutInflater.from(FeedSowActivity.this).inflate(R.layout.activity_sowing,null);
                newPopWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
                android.widget.Button button_cancel=view2.findViewById(R.id.cancel);
                button_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newPopWindow.dismiss();
//                        data_1.clear();
                        postSync();
                        FeedSowActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                        adapter.setCheckbox(false);
                    }
                });
                Button button_delete=(Button) view2.findViewById(R.id.delete);
                button_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapter.setCheckbox(false);
                        OkHttpClient httpClient=new OkHttpClient.Builder()
                                .addInterceptor(new LoginIntercept()).build();
                        for (int i = 0; i < adapter.idList.size(); i++) {
                            Request request=new Request.Builder()
                                    .delete()
                                    .url("http://124.222.111.61:9000/daily/operation/deleteOperation/"+adapter.idList.get(i))
                                    .build();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Call call = httpClient.newCall(request);
                                        Response response = call.execute();
                                        assert response.body() != null;
                                        String responsePond = response.body().string();
                                        JSONObject jsonObject = new JSONObject(responsePond);
                                        String fd=jsonObject.getString("msg");
                                        FeedSowActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(FeedSowActivity.this, fd,Toast.LENGTH_SHORT).show();
                                                adapter.notifyDataSetChanged();
                                            }
                                        });
                                    } catch (IOException | JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                        data_1.clear();
                        adapter.idList.clear();
                        postSync();
                        adapter.notifyDataSetChanged();
                        Log.e("onClick: ", String.valueOf(data_1));
                        newPopWindow.dismiss();
                    }
                });
            }
        });
        postSync();
        refreshLayout.setColorSchemeResources(R.color.blue,R.color.blue);//???????????????????????????????????????????????????????????????????????????
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postSync();
                refreshLayout.setRefreshing(false);
            }
        });
        adapter.setMasterOnItemListener(new AllTextMasterAdapter.MasterOnItemListener() {
            @Override
            public void OnItemClickListener(View view, int position) {
                //null
            }

            @Override
            public void OnItemLongClickListener(View view, int position, AllText allText) {
            }
        });
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_feed_sow;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }
    public void postSync() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept())
                .build();
        Request requestField = new Request.Builder()
                .get()
                .url("http://124.222.111.61:9000/daily/operation/queryOperation")
                .build();
        new Thread(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                try {
                    Call call = httpClient.newCall(requestField);
                    Response response = call.execute();
                    assert response.body() != null;
                    String responseData = response.body().string();
                    data_1.clear();
                    String mid = null;
                    JSONObject jsonObject = new JSONObject(responseData);
                    System.out.println(responseData);
                    String json = jsonObject.getJSONObject("data").toString();
                    Map<String, JsonArray> map = new Gson()
                            .fromJson(json, new TypeToken<Map<String, JsonArray>>() {
                            }
                                    .getType());
                    for (Map.Entry<String, JsonArray> entry : map.entrySet()) {
                        AllText one1;
                        List<AllText> allTextList1 = new ArrayList<>();
                        String mapKey = entry.getKey();
                        JsonArray mapValue = entry.getValue();
                        JSONArray pond = new JSONArray(String.valueOf(mapValue));
                        Log.e("pond: ", String.valueOf(pond));
                        for (int i = 0; i < pond.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) pond.get(i);
                            int id = jsonObject1.getInt("id");
                            String name = jsonObject1.getString("name");
                            double num= jsonObject1.getDouble("num");
                            String numUnit = jsonObject1.getString("numUnit");
                            String time = jsonObject1.getString("time");
                            String type = jsonObject1.getString("type");
                            String productionName = jsonObject1.getString("productionName");
                            name = "???????????????" + name;
                            String data =
                                    "\n????????????" + num + numUnit +
                                            "\n???????????????" + productionName+
                                            "\n???????????????" + time;
                            if(type.equals("feed")){
                                one1 = new AllText(name, data, id);
                                allTextList1.add(one1);
                            }
                            FeedSowActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                        data_1.add(new AllTextMaster(mapKey, allTextList1));

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}