package com.example.newag.mvp.ui.monitor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.example.newag.R;
import com.example.newag.base.BaseFragment;
import com.example.newag.intercept.LoginIntercept;
import com.example.newag.mvp.adapter.BucketAdapter;
import com.example.newag.mvp.model.bean.Icon;
import com.example.newag.mvp.ui.plus.ReduceFishPlusActivity;
import com.example.newag.mvp.ui.reduce.ReduceFishAddActivity;
import com.umeng.commonsdk.debug.I;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BucketFragment extends BaseFragment {
    private Context mContext;
    private GridView grid_photo;
    private BucketAdapter mAdapter = null;
    private ArrayList<Icon> mData = null;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bucket;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        getData(view);
    }

    @Override
    protected void initData() {
        refreshLayout.setColorSchemeResources(R.color.blue,R.color.blue);//设置下拉刷新主题（最多支持三种颜色变换，这里两种)
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(false);
            getData(requireView());
            mAdapter.notifyDataSetChanged();
        });
    }
    private void getData(View view){
        mContext=getContext();
        grid_photo = (GridView)view.findViewById(R.id.grid_photo);
        mAdapter = new BucketAdapter<Icon>(mData, R.layout.item_grid_icon) {
            @Override
            public void bindView(BucketAdapter.ViewHolder holder, Icon obj) {
                holder.setText(R.id.id,obj.getId());
                holder.setText(R.id.test, obj.getiName());
            }
        };
        grid_photo.setAdapter(mAdapter);
        mData = new ArrayList<Icon>();
        mData.clear();
        OkHttpClient httpClient=new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept())
                .build();
        Request requestPond = new Request.Builder()
                .get()
                .url("http://124.222.111.61:9000/daily/ponds/query")
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
                    JSONArray pond= jsonObject.getJSONArray("data");
                    Log.e("run: ", responseData);
                    for (int i = 0; i < pond.length(); i++) {
                        JSONObject jsonObject1 = (JSONObject) pond.get(i);
                        int id = jsonObject1.getInt("id");
                        String name = jsonObject1.getString("name");
                        String batch = jsonObject1.getString("batch");
                        if(batch=="null"){
                            batch="无批次";
                        }
                        System.out.println(name+batch+id);
                        mData.add(new Icon(batch,name,id));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        mData.add(new Icon("添加","+",0));
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
        grid_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mData.get(position).getId().equals("+")){
                    Intent intent = new Intent();
                    intent.setClass(getContext(), ReduceFishPlusActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(getContext(), ItemActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putInt("RTableCategoryID", mData.get(position).getRealId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }
}