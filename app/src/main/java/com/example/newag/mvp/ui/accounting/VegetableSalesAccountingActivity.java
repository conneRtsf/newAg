package com.example.newag.mvp.ui.accounting;

import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;
import com.example.newag.intercept.LoginIntercept;
import com.example.newag.mvp.adapter.AllTextMasterAdapter;
import com.example.newag.mvp.model.api.HttpClientUtils;
import com.example.newag.mvp.model.api.service.SalesAccountingApiService;
import com.example.newag.mvp.model.bean.AllText;
import com.example.newag.mvp.model.bean.AllTextMaster;
import com.example.newag.mvp.model.bean.SalesAccountingTranslation;
import com.example.newag.mvp.ui.plus.FishSalesAccountingPlusActivity;
import com.example.newag.mvp.ui.plus.VegetableSalesAccountingPlusActivity;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class VegetableSalesAccountingActivity extends BaseActivity {
    @OnClick(R.id.ce1)
    void click1(){
        Intent intent = new Intent();
        intent.setClass(VegetableSalesAccountingActivity.this,FishSalesAccountingActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.plus)
    void onClick1(View view) {
        Intent intent = new Intent();
        intent.setClass(VegetableSalesAccountingActivity.this, VegetableSalesAccountingPlusActivity.class);
        startActivity(intent);
    }
    @BindView(R.id.root)
    DrawerLayout root;
    @OnClick(R.id.tb1)
    public void onClick123(View v) {
        root.openDrawer(Gravity.LEFT);
    }
    @OnClick(R.id.tb2)
    void  onClick2(){
        finish();
    }
    @BindView(R.id.btn_Date)
    Button btnDate;
    @BindView(R.id.view_one)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.left)
    Button Button;private final List<AllTextMaster> data_1=new ArrayList<>();//定义数据1,原始数据
    private PopupWindow newPopWindow;//副
    private AllTextMasterAdapter adapter;
    Calendar calendar= Calendar.getInstance(Locale.CHINA);

    @Override
    protected void initBaseData() {

    }

    @Override
    protected void baseConfigView() {
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(VegetableSalesAccountingActivity.this,  2, btnDate, calendar);;
            }
        });
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);//设置布局管理器，cv工程
        recyclerView.setLayoutManager(linearLayoutManager);//为recycleview添加布局管理器，cv
        adapter=new AllTextMasterAdapter(this,data_1);//定义一个新的大适配器（AllTextMasterAdapter），并且把数据传进去
        recyclerView.setAdapter(adapter);//设置适配器
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View rootView;
                View view2=LayoutInflater.from(VegetableSalesAccountingActivity.this).inflate(R.layout.ppw_delete,null);
                newPopWindow=new PopupWindow(view2,RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT,false);
                adapter.setCheckbox(true);
                adapter.notifyDataSetChanged();
                rootView= LayoutInflater.from(VegetableSalesAccountingActivity.this).inflate(R.layout.activity_vegetable_sales_accounting,null);
                newPopWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
                Button button_cancel=view2.findViewById(R.id.cancel);
                button_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newPopWindow.dismiss();
                        data_1.clear();
                        postSync();
                        VegetableSalesAccountingActivity.this.runOnUiThread(new Runnable() {
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
                                    .url("http://124.222.111.61:9000/daily/field/delete/"+adapter.idList.get(i))
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
                                        VegetableSalesAccountingActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(VegetableSalesAccountingActivity.this, fd,Toast.LENGTH_SHORT).show();
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
        btnDate.setText("全部");
        refreshLayout.setColorSchemeResources(R.color.blue,R.color.blue);//设置下拉刷新主题（最多支持三种颜色变换，这里两种）
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postSync();
                btnDate.setText("全部");
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
        return R.layout.activity_vegetable_sales_accounting;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }
    public void showDatePickerDialog(Activity activity, int themeResId, Button bt, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器(How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                data_1.clear();
                bt.setText(year + "年\n" + (monthOfYear + 1) + "月");
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                int firstDay = calendar.getMinimum(Calendar.DATE);
                calendar.set(Calendar.DAY_OF_MONTH, firstDay);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
                String startTime = sdf1.format(calendar.getTime());
                calendar.set(Calendar.MONTH, monthOfYear + 1);
                int lastDay = calendar.getMinimum(Calendar.DATE);
                calendar.set(Calendar.DAY_OF_MONTH, lastDay - 1);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String endTime = sdf.format(calendar.getTime());
                Log.e("onDateSet: ", startTime + endTime);
                VegetableSalesAccountingActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(VegetableSalesAccountingActivity.this, "刷新以重新获取全部数据", Toast.LENGTH_SHORT).show();
                    }
                });
                postSync(startTime,endTime);
                VegetableSalesAccountingActivity.this.runOnUiThread(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void postSync() {
        Retrofit retrofit = HttpClientUtils.getRetrofitWithGsonAdapter();
        SalesAccountingApiService salesAccountingApiService =retrofit.create(SalesAccountingApiService.class);
        retrofit2.Call<SalesAccountingTranslation> call=salesAccountingApiService.SalesAccounting();
        call.enqueue(new Callback<SalesAccountingTranslation>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<SalesAccountingTranslation> call, @NonNull retrofit2.Response<SalesAccountingTranslation> response) {
                try{
                    data_1.clear();
                    String mid = null;
                    SalesAccountingTranslation salesAccountingTranslation=response.body();
                    assert salesAccountingTranslation != null;
                    Map<String, JsonArray> map =salesAccountingTranslation.getData();
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
                            String type = jsonObject1.getString("type");
                            double price= jsonObject1.getDouble("price");
                            double sales= jsonObject1.getDouble("sales");
                            double saleAmount= jsonObject1.getDouble("saleAmount");
                            String note = jsonObject1.getString("note");
                            String time = jsonObject1.getString("time");
                            mid=type;
                            String data =
                                    "单价：" + price +"元"+
                                            "\n销售量：" + sales+
                                            "\n备注：" + note +
                                            "\n总价：" + saleAmount +"元"+
                                            "\n添加时间：" + time;
                            if (type.equals("adultVegetable")) {
                                one1 = new AllText(name,data, id);
                                allTextList1.add(one1);
                            }
                            VegetableSalesAccountingActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                        if (mid.equals("adultVegetable")) {
                            data_1.add(new AllTextMaster(mapKey, allTextList1));
                        }

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<SalesAccountingTranslation> call, Throwable throwable) {
                Log.e(TAG, "info：" + throwable.getMessage() + "," + throwable.toString());
                Toast.makeText(VegetableSalesAccountingActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void postSync(String startTime,String endTime) {
        Retrofit retrofit = HttpClientUtils.getRetrofitWithGsonAdapter();
        SalesAccountingApiService salesAccountingApiService =retrofit.create(SalesAccountingApiService.class);
        retrofit2.Call<SalesAccountingTranslation> call=salesAccountingApiService.SalesAccounting(endTime,startTime);
        call.enqueue(new Callback<SalesAccountingTranslation>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<SalesAccountingTranslation> call, @NonNull retrofit2.Response<SalesAccountingTranslation> response) {
                try{
                    data_1.clear();
                    String mid = null;
                    SalesAccountingTranslation salesAccountingTranslation=response.body();
                    assert salesAccountingTranslation != null;
                    Map<String, JsonArray> map =salesAccountingTranslation.getData();
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
                            String type = jsonObject1.getString("type");
                            double price= jsonObject1.getDouble("price");
                            double sales= jsonObject1.getDouble("sales");
                            double saleAmount= jsonObject1.getDouble("saleAmount");
                            String note = jsonObject1.getString("note");
                            String time = jsonObject1.getString("time");
                            mid=type;
                            String data =
                                    "单价：" + price +"元"+
                                            "\n销售量：" + sales+
                                            "\n备注：" + note +
                                            "\n总价：" + saleAmount +"元"+
                                            "\n添加时间：" + time;
                            if (type.equals("adultVegetable")) {
                                one1 = new AllText(name,data, id);
                                allTextList1.add(one1);
                            }
                            VegetableSalesAccountingActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                        if (mid.equals("adultVegetable")) {
                            data_1.add(new AllTextMaster(mapKey, allTextList1));
                        }

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<SalesAccountingTranslation> call, Throwable throwable) {
                Log.e(TAG, "info：" + throwable.getMessage() + "," + throwable.toString());
                Toast.makeText(VegetableSalesAccountingActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
