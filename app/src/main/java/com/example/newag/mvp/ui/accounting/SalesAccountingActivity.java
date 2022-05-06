package com.example.newag.mvp.ui.accounting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.newag.mvp.ui.plus.SalesAccountingPlusActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

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

public class SalesAccountingActivity extends BaseActivity {
    @OnClick(R.id.plus)
    void onClick1(View view) {
        Intent intent = new Intent();
        intent.setClass(SalesAccountingActivity.this, SalesAccountingPlusActivity.class);
        startActivity(intent);
    }
    @BindView(R.id.btn_Date)
    Button btnDate;
    @BindView(R.id.view_one)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.left)
    Button Button;private final List<AllTextMaster> data_1=new ArrayList<>();//定义数据1,原始数据
    private PopupWindow popupWindow;//定义一个新的popupWindow 主
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
                showDatePickerDialog(SalesAccountingActivity.this,  2, btnDate, calendar);;
            }
        });
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);//设置布局管理器，cv工程
        recyclerView.setLayoutManager(linearLayoutManager);//为recycleview添加布局管理器，cv
        adapter=new AllTextMasterAdapter(this,data_1);//定义一个新的大适配器（AllTextMasterAdapter），并且把数据传进去
        recyclerView.setAdapter(adapter);//设置适配器
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();//展示popwindow的方法
            }
        });
        //
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
        return R.layout.activity_accounting;
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
                SalesAccountingActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SalesAccountingActivity.this, "刷新以重新获取全部数据", Toast.LENGTH_SHORT).show();
                    }
                });
                OkHttpClient httpClient = new OkHttpClient.Builder()
                        .addInterceptor(new LoginIntercept())
                        .build();
                Request requestDate = new Request.Builder()
                        .get()
                        .url("http://124.222.111.61:9000/daily/sales/querySales?endTime=" + endTime + "&startTime=" + startTime)
                        .build();
                new Thread(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        try {
                            Call call = httpClient.newCall(requestDate);
                            Response response = call.execute();
                            assert response.body() != null;
                            data_1.clear();
                            Log.e("rundate: ", String.valueOf(data_1));
                            String responseData = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseData);
                            String json = jsonObject.getJSONObject("data").toString();
                            Log.e("rundate: ", responseData);
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
                                    double price= jsonObject1.getDouble("price");
                                    double sales= jsonObject1.getDouble("sales");
                                    double saleAmount= jsonObject1.getDouble("saleAmount");
                                    String note = jsonObject1.getString("note");
                                    String time = jsonObject1.getString("time");
                                    String data = "销售名称：" + name +
                                            "\n单价：" + price +
                                            "\n销售量：" + sales+
                                            "\n备注：" + note +
                                            "\n总价：" + saleAmount +
                                            "\n添加时间：" + time;
                                    one1 = new AllText(data, id);
                                    allTextList1.add(one1);
                                    SalesAccountingActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                                data_1.add(new AllTextMaster(mapKey, allTextList1));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                SalesAccountingActivity.this.runOnUiThread(new Runnable() {
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
    private void showPopWindow() {
        //定义一个view，其中包含popwindow的布局文件
        View view1= LayoutInflater.from(SalesAccountingActivity.this).inflate(R.layout.footer_batch,null);
        popupWindow =new PopupWindow(view1, RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT,true);//设置popwindow的属性（布局，x，y，true）
        TextView make_text=(TextView)view1.findViewById(R.id.make_text);
        TextView back_test=(TextView)view1.findViewById(R.id.back_test);
        make_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.setCheckbox(true);
                adapter.notifyDataSetChanged();
                popupWindow.dismiss();//销毁popwindow
                View rootView= LayoutInflater.from(SalesAccountingActivity.this).inflate(R.layout.activity_accounting,null);
                newPopWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
            }
        });
        back_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        //定义一个view，其中包含main4的布局文件
        View rootView=LayoutInflater.from(SalesAccountingActivity.this).inflate(R.layout.activity_accounting,null);
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);//展示自定义的popwindow，（放哪个布局里，放布局里的位置，x，y），cv工程
        View view2=LayoutInflater.from(SalesAccountingActivity.this).inflate(R.layout.ppw_delete,null);
        newPopWindow=new PopupWindow(view2,RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT,false);
        Button button_delete=(Button) view2.findViewById(R.id.delete);
        button_delete.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                adapter.setCheckbox(false);
                OkHttpClient httpClient=new OkHttpClient.Builder()
                        .addInterceptor(new LoginIntercept()).build();
                for (int i = 0; i < adapter.idList.size(); i++) {
                    Request request=new Request.Builder()
                            .delete()
                            .url("http://124.222.111.61:9000/daily/sales/deleteSale/"+adapter.idList.get(i))
                            .build();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                data_1.clear();
                                Call call = httpClient.newCall(request);
                                Response response = call.execute();
                                assert response.body() != null;
                                String responsePond = response.body().string();
                                JSONObject jsonObject = new JSONObject(responsePond);
                                String fd=jsonObject.getString("msg");
                                SalesAccountingActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SalesAccountingActivity.this, fd,Toast.LENGTH_SHORT).show();
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
    public void postSync() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept())
                .build();
        Request requestField = new Request.Builder()
                .get()
                .url("http://124.222.111.61:9000/daily/sales/querySales")
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
                    JSONObject jsonObject = new JSONObject(responseData);
                    String json = jsonObject.getJSONObject("data").toString();
                    Log.e("run: ", responseData);
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
                            double price= jsonObject1.getDouble("price");
                            double sales= jsonObject1.getDouble("sales");
                            double saleAmount= jsonObject1.getDouble("saleAmount");
                            String note = jsonObject1.getString("note");
                            String time = jsonObject1.getString("time");
                            String data = "销售名称：" + name +
                                    "\n单价：" + price +
                                    "\n销售量：" + sales+
                                    "\n备注：" + note +
                                    "\n总价：" + saleAmount +
                                    "\n添加时间：" + time;
                                one1 = new AllText(data, id);
                                allTextList1.add(one1);
                            SalesAccountingActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                        data_1.add(new AllTextMaster(mapKey, allTextList1));
                    }
                } catch (Exception e) {
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
