package com.example.newag.mvp.ui.farmwork;

import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.widget.Toast;

import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;
import com.example.newag.intercept.LoginIntercept;
import com.example.newag.mvp.adapter.AllTextMasterAdapter;
import com.example.newag.mvp.model.api.HttpClientUtils;
import com.example.newag.mvp.model.api.service.FarmActionApiService;
import com.example.newag.mvp.model.bean.AllText;
import com.example.newag.mvp.model.bean.AllTextMaster;
import com.example.newag.mvp.model.bean.SalesAccountingTranslation;
import com.example.newag.mvp.ui.plus.MedicationPlusActivity;
import com.example.newag.mvp.ui.plus.SowingPlusActivity;
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
import retrofit2.Callback;
import retrofit2.Retrofit;

public class MedicationActivity extends BaseActivity {
    @OnClick(R.id.ce1)
    void click3(){
        Intent intent = new Intent();
        intent.setClass(MedicationActivity.this, SowingActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.plus)
    void onClick1(View view) {
        Intent intent = new Intent();
        intent.setClass(MedicationActivity.this, MedicationPlusActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.ce2)
    void click1(){
        Intent intent = new Intent();
        intent.setClass(MedicationActivity.this, HarvestFishingActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.ce3)
    void click2(){
        Intent intent = new Intent();
        intent.setClass(MedicationActivity.this, MedicationActivity.class);
        startActivity(intent);
        finish();
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
    android.widget.Button btnDate;
    @BindView(R.id.view_one)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.left)
    Button Button;private final List<AllTextMaster> data_1=new ArrayList<>();//????????????1,????????????
    private PopupWindow newPopWindow;//???
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
                showDatePickerDialog(MedicationActivity.this,  2, btnDate, calendar);;
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
                View view2= LayoutInflater.from(MedicationActivity.this).inflate(R.layout.ppw_delete,null);
                newPopWindow=new PopupWindow(view2,RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT,false);
                adapter.setCheckbox(true);
                adapter.notifyDataSetChanged();
                rootView= LayoutInflater.from(MedicationActivity.this).inflate(R.layout.activity_medication,null);
                newPopWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
                Button button_cancel=view2.findViewById(R.id.cancel);
                button_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newPopWindow.dismiss();
                        data_1.clear();
                        postSync();
                        MedicationActivity.this.runOnUiThread(new Runnable() {
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
                                        MedicationActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(MedicationActivity.this, fd,Toast.LENGTH_SHORT).show();
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
        btnDate.setText("??????");
        refreshLayout.setColorSchemeResources(R.color.blue,R.color.blue);//???????????????????????????????????????????????????????????????????????????
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postSync();
                btnDate.setText("??????");
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
        return R.layout.activity_medication;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }
    public void showDatePickerDialog(Activity activity, int themeResId, Button bt, Calendar calendar) {
        // ??????????????????DatePickerDialog???????????????????????????????????????
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // ???????????????(How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                data_1.clear();
                bt.setText(year + "???\n" + (monthOfYear + 1) + "???");
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
                MedicationActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MedicationActivity.this, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                });
                postSync(startTime,endTime);
                MedicationActivity.this.runOnUiThread(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
                // ??????????????????
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
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
                            String operation = jsonObject1.getString("operation");
                            double num= jsonObject1.getDouble("num");
                            String productionName=jsonObject1.getString("productionName");
                            String numUnit = jsonObject1.getString("numUnit");
                            String time = jsonObject1.getString("time");
                            String type = jsonObject1.getString("type");
                            mid=type;
                            name="???????????????"+name;
                            String data =
                                    "????????????:"+operation+
                                            "\n????????????" + num+numUnit+
                                            "\n???????????????" + productionName +
                                            "\n???????????????" + time;
                            if (mid.equals("drug")) {
                                one1 = new AllText(name,data, id);
                                allTextList1.add(one1);
                            }
                            MedicationActivity.this.runOnUiThread(new Runnable() {
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

    public void postSync(String startTime,String endTime) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept())
                .build();
        Request requestField = new Request.Builder()
                .get()
                .url("http://124.222.111.61:9000/daily/operation/queryOperation?endTime=" + endTime + "&startTime=" + startTime)
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
                            String operation = jsonObject1.getString("operation");
                            double num = jsonObject1.getDouble("num");
                            String productionName = jsonObject1.getString("productionName");
                            String numUnit = jsonObject1.getString("numUnit");
                            String time = jsonObject1.getString("time");
                            String type = jsonObject1.getString("type");
                            mid = type;
                            name = "???????????????" + name;
                            String data =
                                    "????????????:" + operation +
                                            "\n????????????" + num + numUnit +
                                            "\n???????????????" + productionName +
                                            "\n???????????????" + time;
                            if (type.equals("drug")) {
                                one1 = new AllText(name, data, id);
                                allTextList1.add(one1);
                            }
                            MedicationActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                        if (mid.equals("drug")) {
                            data_1.add(new AllTextMaster(mapKey, allTextList1));
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}