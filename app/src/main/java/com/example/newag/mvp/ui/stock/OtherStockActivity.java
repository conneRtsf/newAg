package com.example.newag.mvp.ui.stock;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;
import com.example.newag.intercept.LoginIntercept;
import com.example.newag.mvp.adapter.AllTextMasterAdapter;
import com.example.newag.mvp.model.bean.AllText;
import com.example.newag.mvp.model.bean.AllTextMaster;
import com.example.newag.mvp.ui.change.OtherStorkChangeActivity;
import com.example.newag.mvp.ui.plus.StorkOtherPlusActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OtherStockActivity extends BaseActivity {
    @BindView(R.id.searchView)
    SearchView searchView;
    @OnClick(R.id.tb1)
    void onClick(View view) {
        root.openDrawer(Gravity.LEFT);
    }
    @OnClick(R.id.tb2)
    void  onClick2(){
        finish();
    }
    @OnClick(R.id.ce1)
    void onClick1(View view) {
        Intent intent = new Intent();
        intent.setClass(OtherStockActivity.this, DrugStockActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.ce2)
    void onClick2(View view) {
        Intent intent = new Intent();
        intent.setClass(OtherStockActivity.this, FeedStockActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.ce3)
    void onClick3(View view) {
        Intent intent = new Intent();
        intent.setClass(OtherStockActivity.this, FishStockActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.ce4)
    void onClick4(View view) {
        Intent intent = new Intent();
        intent.setClass(OtherStockActivity.this, VegetableStockActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.ce5)
    void onClick5(View view) {
        Intent intent = new Intent();
        intent.setClass(OtherStockActivity.this, OtherStockActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.ce7)
    void onClick7(View view) {
        Intent intent = new Intent();
        intent.setClass(OtherStockActivity.this, AdultVegetableStockActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.ce6)
    void onClick6(View view) {
        Intent intent = new Intent();
        intent.setClass(OtherStockActivity.this, AdultFishStockActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.plus)
    void onClick11(View view) {
        Intent intent = new Intent();
        intent.setClass(OtherStockActivity.this, StorkOtherPlusActivity.class);
        startActivity(intent);
    }
    @BindView(R.id.root)
    DrawerLayout root;
    @BindView(R.id.left)
    Button Button;
    @BindView(R.id.view_one)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.content)
    View contentView;
    private final List<AllTextMaster> data_1=new ArrayList<>();//????????????1,????????????
    private PopupWindow popupWindow;//??????????????????popupWindow ???
    private PopupWindow newPopWindow;//???
    private AllTextMasterAdapter adapter;

    @Override
    protected void initBaseData() {

    }

    @Override
    protected void baseConfigView() {
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("??????");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                data_1.clear();
                OtherStockActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                postSync(query);
                Log.e("onQueryTextSubmit1: ", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                data_1.clear();
                OtherStockActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                postSync(newText);
                Log.e("onQueryTextSubmit2: ", newText);
                return false;
            }
        });
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);//????????????????????????cv??????
        recyclerView.setLayoutManager(linearLayoutManager);//???recycleview????????????????????????cv
        adapter=new AllTextMasterAdapter(this,data_1);//?????????????????????????????????AllTextMasterAdapter??????????????????????????????
        recyclerView.setAdapter(adapter);//???????????????
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View rootView;
                View view2=LayoutInflater.from(OtherStockActivity.this).inflate(R.layout.ppw_delete,null);
                newPopWindow=new PopupWindow(view2,RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT,false);
                adapter.setCheckbox(true);
                adapter.notifyDataSetChanged();
                rootView= LayoutInflater.from(OtherStockActivity.this).inflate(R.layout.activity_reduce_field_add,null);
                newPopWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
                Button button_cancel=view2.findViewById(R.id.cancel);
                button_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newPopWindow.dismiss();
                        data_1.clear();
                        postSync();
                        OtherStockActivity.this.runOnUiThread(new Runnable() {
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
                                    .url("http://124.222.111.61:9000/daily/input/delete/"+adapter.idList.get(i))
                                    .build();
                            Log.e("onClick: ", "http://124.222.111.61:9000/daily/field/delete/"+adapter.idList.get(i));
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
                                        OtherStockActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(OtherStockActivity.this, fd,Toast.LENGTH_SHORT).show();
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
                refreshLayout.setRefreshing(false);
                postSync();
            }
        });
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, root, android.R.string.yes, android.R.string.cancel) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                contentView.setTranslationX(slideX);
            }
        };
        root.addDrawerListener(actionBarDrawerToggle);
        adapter.setMasterOnItemListener(new AllTextMasterAdapter.MasterOnItemListener() {
            @Override
            public void OnItemClickListener(View view, int position) {
                //null
            }

            @Override
            public void OnItemLongClickListener(View view, int position, AllText allText) {
                showPopWindow(allText,position);
                postSync();
            }
        });
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_templateother;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }
    private void showPopWindow(AllText allText,int position) {
        View view = LayoutInflater.from(OtherStockActivity.this).inflate(R.layout.pop_plustemplate, null);
        TextView editText = view.findViewById(R.id.et_1);
        editText.setText(allText.getData());
        TextView editText2 = view.findViewById(R.id.name);
        editText2.setText(allText.getName());
        Button button=view.findViewById(R.id.make_text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OtherStockActivity.this, OtherStorkChangeActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("data",allText.getNum());
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
            }
        });
        popupWindow = new PopupWindow(view, RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT, true);//??????popwindow?????????????????????x???y???true???
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);//??????????????????popwindow???????????????????????????????????????????????????x???y??????cv??????
    }
    public void postSync() {
        OkHttpClient httpClient=new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept())
                .build();
        Request requestField = new Request.Builder()
                .get()
                .url("http://124.222.111.61:9000/daily/input/queryAll")
                .build();
        new Thread(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                try {
                    Call call = httpClient.newCall(requestField);
                    Response response=call.execute();
                    assert response.body() != null;
                    String responseData=response.body().string();
                    data_1.clear();
                    JSONObject jsonObject = new JSONObject(responseData);
                    String json=jsonObject.getJSONObject("data").toString();
                    Log.e("run: ", responseData);
                    Map<String, JsonArray> map = new Gson()
                            .fromJson(json, new TypeToken<Map<String, JsonArray>>() {}
                                    .getType());
                    String mid = null;
                    for (Map.Entry<String, JsonArray> entry : map.entrySet()) {
                        AllText one1;
                        List<AllText> allTextList1=new ArrayList<>();
                        String mapKey = entry.getKey();
                        JsonArray mapValue = entry.getValue();
                        JSONArray pond=new JSONArray(String.valueOf(mapValue));
                        Log.e("pond: ", String.valueOf(pond));
                        for (int i = 0; i < pond.length(); i++) {
                            JSONObject jsonObject1= (JSONObject) pond.get(i);
                            int id=jsonObject1.getInt("id");
                            String name=jsonObject1.getString("name");
                            double inventory=jsonObject1.getDouble("inventory");
                            String factory=jsonObject1.getString("factory");
                            String time=jsonObject1.getString("time");
                            String note=jsonObject1.getString("note");
                            String type=jsonObject1.getString("type");
                            String information=jsonObject1.getString("information");
                            String inventoryUnit=jsonObject1.getString("inventoryUnit");
                            String data=
                                    "?????????"+inventory+inventoryUnit+
                                    "\n?????????"+factory+
                                    "\n???????????????"+time+
                                    "\n?????????"+note;
                            mid=type;
                            if(type.equals("other")){
                                one1=new AllText(name,data,id,returnBitMap(information));
                                allTextList1.add(one1);
                            }
                            OtherStockActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                        if(mid.equals("other")) {
                            data_1.add(new AllTextMaster("???????????????????????????????????????", allTextList1));
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void postSync(String name) {
        OkHttpClient httpClient=new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept())
                .build();
        Request requestField = new Request.Builder()
                .get()
                .url("http://124.222.111.61:9000/daily/input/queryAll?name="+name)
                .build();
        new Thread(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                try {
                    Call call = httpClient.newCall(requestField);
                    Response response=call.execute();
                    assert response.body() != null;
                    String responseData=response.body().string();
                    data_1.clear();
                    JSONObject jsonObject = new JSONObject(responseData);
                    String json=jsonObject.getJSONObject("data").toString();
                    Log.e("run: ", responseData);
                    Map<String, JsonArray> map = new Gson()
                            .fromJson(json, new TypeToken<Map<String, JsonArray>>() {}
                                    .getType());
                    String mid = null;
                    for (Map.Entry<String, JsonArray> entry : map.entrySet()) {
                        AllText one1;
                        List<AllText> allTextList1=new ArrayList<>();
                        JsonArray mapValue = entry.getValue();
                        JSONArray pond=new JSONArray(String.valueOf(mapValue));
                        Log.e("pond: ", String.valueOf(pond));
                        for (int i = 0; i < pond.length(); i++) {
                            JSONObject jsonObject1= (JSONObject) pond.get(i);
                            int id=jsonObject1.getInt("id");
                            String name=jsonObject1.getString("name");
                            double inventory=jsonObject1.getDouble("inventory");
                            String factory=jsonObject1.getString("factory");
                            String time=jsonObject1.getString("time");
                            String note=jsonObject1.getString("note");
                            String type=jsonObject1.getString("type");
                            String information=jsonObject1.getString("information");
                            String inventoryUnit=jsonObject1.getString("inventoryUnit");
                            System.out.println("haha"+information);
                            String data=
                                    "?????????"+inventory+inventoryUnit+
                                            "\n?????????"+factory+
                                            "\n???????????????"+time+
                                            "\n?????????"+note;
                            mid=type;
                            if(type.equals("other")){
                                one1=new AllText(name,data,id,returnBitMap(information));
                                allTextList1.add(one1);
                            }
                            OtherStockActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                        if(mid.equals("other")) {
                            data_1.add(new AllTextMaster("???????????????????????????????????????", allTextList1));
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode){
            case 1:
                if (resultCode==2){
                    Bundle bundle=new Bundle();
                    bundle=intent.getExtras();
                    AllText allText=(AllText) bundle.getSerializable("data");
                    int position=(int)bundle.getSerializable("position");
                    String name=allText.getName();
                    Log.d("data","?????????"+name+"?????????"+position);
                }
        }
    }

}
