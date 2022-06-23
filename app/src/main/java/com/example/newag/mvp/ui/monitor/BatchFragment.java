package com.example.newag.mvp.ui.monitor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.base.BaseFragment;
import com.example.newag.intercept.LoginIntercept;
import com.example.newag.mvp.adapter.AllTextMasterAdapter;
import com.example.newag.mvp.model.bean.AllText;
import com.example.newag.mvp.model.bean.AllTextMaster;
import com.example.newag.mvp.ui.monitor.Item.FeedSowActivity;
import com.example.newag.mvp.ui.reduce.ReduceFieldAddActivity;
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
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BatchFragment extends BaseFragment {
//    @BindView(R.id.view_one)
//    RecyclerView recyclerView;
//    @BindView(R.id.refresh)
//    SwipeRefreshLayout refreshLayout;
//    private final List<AllTextMaster> data_1=new ArrayList<>();//定义数据1,原始数据
//    private AllTextMasterAdapter adapter;
    @Override
    protected int getLayoutId() {
            return R.layout.fragment_batch;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {
//        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());//设置布局管理器，cv工程
//        recyclerView.setLayoutManager(linearLayoutManager);//为recycleview添加布局管理器，cv
//        adapter=new AllTextMasterAdapter(getActivity(),data_1);//定义一个新的大适配器（AllTextMasterAdapter），并且把数据传进去
//        recyclerView.setAdapter(adapter);//设置适配器
//        postSync();
//        refreshLayout.setColorSchemeResources(R.color.blue,R.color.blue);//设置下拉刷新主题（最多支持三种颜色变换，这里两种）
//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                postSync();
//                refreshLayout.setRefreshing(false);
//            }
//        });
//        adapter.setMasterOnItemListener(new AllTextMasterAdapter.MasterOnItemListener() {
//            @Override
//            public void OnItemClickListener(View view, int position) {
//                //null
//            }
//
//            @Override
//            public void OnItemLongClickListener(View view, int position, AllText allText) {
//            }
//        });
    }
//    public void postSync() {
//        OkHttpClient httpClient=new OkHttpClient.Builder()
//                .addInterceptor(new LoginIntercept())
//                .build();
//        Request requestField = new Request.Builder()
//                .get()
//                .url("http://124.222.111.61:9000/daily/ponds/batch/show")
//                .build();
//        new Thread(new Runnable() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void run() {
//                try {
//                    Call call = httpClient.newCall(requestField);
//                    Response response=call.execute();
//                    assert response.body() != null;
//                    String responseData=response.body().string();
//                    data_1.clear();
//                    JSONObject jsonObject = new JSONObject(responseData);
//                    String json=jsonObject.getJSONObject("data").toString();
//                    Log.e("run: ", responseData);
//                    Map<String, JsonArray> map = new Gson()
//                            .fromJson(json, new TypeToken<Map<String, JsonArray>>() {}
//                                    .getType());
//                    for (Map.Entry<String, JsonArray> entry : map.entrySet()) {
//                        AllText one1;
//                        List<AllText> allTextList1=new ArrayList<>();
//                        String mapKey = entry.getKey();
//                        JsonArray mapValue = entry.getValue();
//                        JSONArray pond=new JSONArray(String.valueOf(mapValue));
//                        Log.e("pond: ", String.valueOf(pond));
//                        for (int i = 0; i < pond.length(); i++) {
//                            JSONObject jsonObject1= (JSONObject) pond.get(i);
//                            int id=jsonObject1.getInt("id");
//                            String name=jsonObject1.getString("name");
//                            String data="";
//                            one1=new AllText(name,data,id);
//                            allTextList1.add(one1);
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    adapter.notifyDataSetChanged();
//                                }
//                            });
//                        }
//                        data_1.add(new AllTextMaster(mapKey,allTextList1));
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
}