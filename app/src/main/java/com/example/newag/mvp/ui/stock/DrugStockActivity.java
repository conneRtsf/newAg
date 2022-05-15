package com.example.newag.mvp.ui.stock;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.newag.mvp.ui.change.DrugStorkChangeActivity;
import com.example.newag.mvp.ui.main.MainActivity;
import com.example.newag.mvp.ui.plus.StorkAdultFishPlusActivity;
import com.example.newag.mvp.ui.plus.StorkDrugPlusActivity;
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

public class DrugStockActivity extends BaseActivity {
    @BindView(R.id.searchView)
    SearchView searchView;
    @OnClick(R.id.tb1)
    void onClick(View view) {
        root.openDrawer(Gravity.LEFT);
    }
    @OnClick(R.id.ce1)
    void onClick1(View view) {
        Intent intent = new Intent();
        intent.setClass(DrugStockActivity.this, DrugStockActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.ce2)
    void onClick2(View view) {
        Intent intent = new Intent();
        intent.setClass(DrugStockActivity.this, FeedStockActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.ce3)
    void onClick3(View view) {
        Intent intent = new Intent();
        intent.setClass(DrugStockActivity.this, FishStockActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.ce4)
    void onClick4(View view) {
        Intent intent = new Intent();
        intent.setClass(DrugStockActivity.this, VegetableStockActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.ce5)
    void onClick5(View view) {
        Intent intent = new Intent();
        intent.setClass(DrugStockActivity.this, OtherStockActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.ce6)
    void onClick6(View view) {
        Intent intent = new Intent();
        intent.setClass(DrugStockActivity.this, AdultFishStockActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.ce7)
    void onClick7(View view) {
        Intent intent = new Intent();
        intent.setClass(DrugStockActivity.this, AdultVegetableStockActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.plus)
    void onClick11(View view) {
        Intent intent = new Intent();
        intent.setClass(DrugStockActivity.this, StorkDrugPlusActivity.class);
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
    private final List<AllTextMaster> data_1=new ArrayList<>();//定义数据1,原始数据
    private PopupWindow popupWindow;//定义一个新的popupWindow 主
    private PopupWindow newPopWindow;//副
    private AllTextMasterAdapter adapter;

    @Override
    protected void initBaseData() {

    }

    @Override
    protected void baseConfigView() {
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("查找");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                data_1.clear();
                DrugStockActivity.this.runOnUiThread(new Runnable() {
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
                DrugStockActivity.this.runOnUiThread(new Runnable() {
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
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);//设置布局管理器，cv工程
        recyclerView.setLayoutManager(linearLayoutManager);//为recycleview添加布局管理器，cv
        adapter=new AllTextMasterAdapter(this,data_1);//定义一个新的大适配器（AllTextMasterAdapter），并且把数据传进去
        recyclerView.setAdapter(adapter);//设置适配器
        postSync();
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View rootView;
                View view2=LayoutInflater.from(DrugStockActivity.this).inflate(R.layout.ppw_delete,null);
                newPopWindow=new PopupWindow(view2,RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT,false);
                adapter.setCheckbox(true);
                adapter.notifyDataSetChanged();
                rootView= LayoutInflater.from(DrugStockActivity.this).inflate(R.layout.activity_reduce_field_add,null);
                newPopWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
                Button button_cancel=view2.findViewById(R.id.cancel);
                button_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newPopWindow.dismiss();
                        data_1.clear();
                        postSync();
                        DrugStockActivity.this.runOnUiThread(new Runnable() {
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
                                        DrugStockActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(DrugStockActivity.this, fd,Toast.LENGTH_SHORT).show();
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
        //
        refreshLayout.setColorSchemeResources(R.color.blue,R.color.blue);//设置下拉刷新主题（最多支持三种颜色变换，这里两种）
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postSync();
                refreshLayout.setRefreshing(false);
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
        return R.layout.activity_templatedrug;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }
    private void showPopWindow(AllText allText,int position) {
        View view = LayoutInflater.from(DrugStockActivity.this).inflate(R.layout.pop_plustemplate, null);
        TextView editText = view.findViewById(R.id.et_1);
        editText.setText(allText.getData());
        TextView editText2 = view.findViewById(R.id.name);
        editText2.setText(allText.getName());
        Button button=view.findViewById(R.id.make_text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DrugStockActivity.this, DrugStorkChangeActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("data",allText.getNum());
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
            }
        });
        popupWindow = new PopupWindow(view, RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT, true);//设置popwindow的属性（布局，x，y，true）
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);//展示自定义的popwindow，（放哪个布局里，放布局里的位置，x，y），cv工程
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
                        List<AllText> allTextList1 = new ArrayList<>();
                        String mapKey = entry.getKey();
                        JsonArray mapValue = entry.getValue();
                        JSONArray pond = new JSONArray(String.valueOf(mapValue));
                        Log.e("pond: ", String.valueOf(pond));
                        for (int i = 0; i < pond.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) pond.get(i);
                            int id = jsonObject1.getInt("id");
                            String name = jsonObject1.getString("name");
                            double inventory = jsonObject1.getDouble("inventory");
                            String factory = jsonObject1.getString("factory");
                            String time = jsonObject1.getString("time");
                            String note = jsonObject1.getString("note");
                            String type = jsonObject1.getString("type");
                            String inventoryUnit = jsonObject1.getString("inventoryUnit");
                            String data =
                                    "\n库存量：" + inventory + inventoryUnit +
                                    "\n厂商：" + factory +
                                    "\n备注：" + note;
                            mid = type;
                            if (type.equals("drug")) {
                                one1 = new AllText(name,data, id);
                                allTextList1.add(one1);
                            }
                            DrugStockActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                        if (mid.equals("drug")) {
                            data_1.add(new AllTextMaster("药品（修改库存数量请长按）", allTextList1));
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void postSync(String query) {
        OkHttpClient httpClient=new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept())
                .build();
        Request requestField = new Request.Builder()
                .get()
                .url("http://124.222.111.61:9000/daily/input/queryAll?name="+query)
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
                        List<AllText> allTextList1 = new ArrayList<>();
                        String mapKey = entry.getKey();
                        JsonArray mapValue = entry.getValue();
                        JSONArray pond = new JSONArray(String.valueOf(mapValue));
                        Log.e("pond: ", String.valueOf(pond));
                        for (int i = 0; i < pond.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) pond.get(i);
                            int id = jsonObject1.getInt("id");
                            String name = jsonObject1.getString("name");
                            double inventory = jsonObject1.getDouble("inventory");
                            String factory = jsonObject1.getString("factory");
                            String time = jsonObject1.getString("time");
                            String note = jsonObject1.getString("note");
                            String type = jsonObject1.getString("type");
                            String inventoryUnit = jsonObject1.getString("inventoryUnit");
                            String data =
                                    "\n库存量：" + inventory + inventoryUnit +
                                            "\n厂商：" + factory +
                                            "\n备注：" + note;
                            mid = type;
                            if (type.equals("drug")) {
                                one1 = new AllText(name,data, id);
                                allTextList1.add(one1);
                            }
                            DrugStockActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                        if (mid.equals("drug")) {
                            data_1.add(new AllTextMaster("药品（修改库存数量请长按）", allTextList1));
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
                    Log.d("data","名称是"+name+"位置为"+position);
                }
        }
    }
}
