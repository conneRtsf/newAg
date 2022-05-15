package com.example.newag.mvp.ui.reduce;

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

import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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

import com.example.newag.mvp.ui.plus.ReduceFishPlusActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.example.newag.mvp.ui.change.ReduceFishChangeActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReduceFishAddActivity extends BaseActivity implements View.OnClickListener{
    @OnClick(R.id.ce2)
    void onClick2(View view) {
        Intent intent = new Intent();
        intent.setClass(ReduceFishAddActivity.this, ReduceFieldAddActivity.class);
        startActivity(intent);
        finish();
    }
    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.fishPlus)
    void onClick11(View view) {
        Intent intent = new Intent();
        intent.setClass(ReduceFishAddActivity.this, ReduceFishPlusActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.tb2)
    void  onClick2(){
        finish();
    }
    @OnClick(R.id.tb1)
    public void onClick123(View v) {
        root.openDrawer(Gravity.LEFT);
    }
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.root)
    DrawerLayout root;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.left)
    Button Button;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.view_one)
    RecyclerView recyclerView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_Date)
    Button btnDate;
    @BindView(R.id.textView3)
    TextView ponds;
    Calendar calendar= Calendar.getInstance(Locale.CHINA);
    private final List<AllTextMaster> data_1=new ArrayList<>();//定义数据1,原始数据
    private PopupWindow popupWindow;
    private PopupWindow newPopWindow;
    private AllTextMasterAdapter adapter;

    @Override
    protected void initBaseData() {

    }
    @Override
    protected void baseConfigView() {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年\nM月 ");
        Date curDate =  new Date(System.currentTimeMillis());
        String   str   =   formatter.format(curDate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(ReduceFishAddActivity.this,  2, btnDate, calendar);
            }
        });
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);//设置布局管理器，cv工程
        recyclerView.setLayoutManager(linearLayoutManager);//为recycleView添加布局管理器，cv
        adapter=new AllTextMasterAdapter(this,data_1);//定义一个新的大适配器（AllTextMasterAdapter），并且把数据传进去
        recyclerView.setAdapter(adapter);//设置适配器
        postSync();
        InquirePond();
        refreshLayout.setColorSchemeResources(R.color.blue,R.color.blue);//设置下拉刷新主题（最多支持三种颜色变换，这里两种)
        refreshLayout.setOnRefreshListener(() -> {
            postSync();
            InquirePond();
            refreshLayout.setRefreshing(false);
            btnDate.setText("全部");
        });
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View rootView;
                View view2=LayoutInflater.from(ReduceFishAddActivity.this).inflate(R.layout.ppw_delete,null);
                newPopWindow=new PopupWindow(view2,RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT,false);
                adapter.setCheckbox(true);
                adapter.notifyDataSetChanged();
                rootView= LayoutInflater.from(ReduceFishAddActivity.this).inflate(R.layout.activity_reduce_fish_add,null);
                newPopWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
                Button button_delete=(Button) view2.findViewById(R.id.delete);
                Button button_cancel=view2.findViewById(R.id.cancel);
                button_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newPopWindow.dismiss();
                        data_1.clear();
                        postSync();
                        ReduceFishAddActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                        adapter.setCheckbox(false);
                    }
                });
                button_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapter.setCheckbox(false);
                        OkHttpClient httpClient=new OkHttpClient.Builder()
                                .addInterceptor(new LoginIntercept()).build();
                        for (int i = 0; i < adapter.idList.size(); i++) {
                            Request request=new Request.Builder()
                                    .delete()
                                    .url("http://124.222.111.61:9000/daily/ponds/delete/"+adapter.idList.get(i))
                                    .build();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Call call = httpClient.newCall(request);
                                        Response response = call.execute();
                                        assert response.body() != null;
                                        String responsePond = response.body().string();
                                        Log.e("run: ", responsePond);
                                        JSONObject jsonObject = new JSONObject(responsePond);
                                        String fd=jsonObject.getString("msg");
                                        ReduceFishAddActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(ReduceFishAddActivity.this, fd,Toast.LENGTH_SHORT).show();
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
                        postSync();
                        InquirePond();
                        Log.e("onClick: ", String.valueOf(data_1));
                        newPopWindow.dismiss();
                    }
                });
            }
        });
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
        return R.layout.activity_reduce_fish_add;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }
    @SuppressLint({"NotifyDataSetChanged", "NonConstantResourceId"})
    @Override
    public void onClick(View v) {
        int id=v.getId();
        if (id == R.id.btn_Date) {
            showDatePickerDialog(this, 2, btnDate, calendar);
            ;
        }
    }
    public void InquirePond(){
        OkHttpClient httpClient=new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept())
                .build();
        Request request = new Request.Builder()
                .get()
                .url("http://124.222.111.61:9000/daily/ponds/queryCount")
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call call = httpClient.newCall(request);
                    Response response=call.execute();
                    assert response.body() != null;
                    String responsePond=response.body().string();
                    ReduceFishAddActivity.this.runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(responsePond);
                                String fd=jsonObject.getString("data");
                                ponds.setText("鱼池\n"+ fd+"个");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void postSync() {
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
                    Response response=call.execute();
                    data_1.clear();
                    assert response.body() != null;
                    Log.e("run: ", String.valueOf(data_1));
                    String responseData=response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    String json=jsonObject.getJSONObject("data").toString();
                    Log.e("run: ", responseData);
                    Map<String, JsonArray> map = new Gson()
                                                .fromJson(json, new TypeToken<Map<String, JsonArray>>() {}
                                                .getType());
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
                            double radius=jsonObject1.getDouble("radius");
                            double height=jsonObject1.getDouble("height");
                            double heightUsed=jsonObject1.getDouble("heightUsed");
                            double volume=jsonObject1.getDouble("volume");
                            double volumeUsed=jsonObject1.getDouble("volumeUsed");
                            String time=jsonObject1.getString("time");
                            String location=jsonObject1.getString("location");
                            String product=jsonObject1.getString("product");
                            String data=
                                    "位置："+location+
                                    "\n半径："+radius+"米"+
                                    "\n高："+height+"米"+
                                    "\n已用高："+heightUsed+"米"+
                                    "\n体积："+volume+"立方米"+
                                    "\n已用体积："+volumeUsed+"立方米"+
                                    "\n添加时间："+time+
                                    "\n产品："+product;
                            one1=new AllText(name,data,id);
                            allTextList1.add(one1);
                            ReduceFishAddActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                        data_1.add(new AllTextMaster(mapKey,allTextList1));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void showDatePickerDialog(Activity activity, int themeResId, Button bt, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器(How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作
                data_1.clear();

                bt.setText(year + "年\n" + (monthOfYear + 1) + "月");
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                int firstDay = calendar.getMinimum(Calendar.DATE);
                calendar.set(Calendar.DAY_OF_MONTH, firstDay);
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
                String startTime = sdf1.format(calendar.getTime());
                calendar.set(Calendar.MONTH, monthOfYear + 1);
                int lastDay = calendar.getMinimum(Calendar.DATE);
                calendar.set(Calendar.DAY_OF_MONTH, lastDay - 1);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String endTime = sdf.format(calendar.getTime());
                Log.e("onDateSet: ", startTime + endTime);
                ReduceFishAddActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ReduceFishAddActivity.this, "刷新以重新获取全部数据", Toast.LENGTH_SHORT).show();
                    }
                });
                OkHttpClient httpClient = new OkHttpClient.Builder()
                        .addInterceptor(new LoginIntercept())
                        .build();
                Request requestDate = new Request.Builder()
                        .get()
                        .url("http://124.222.111.61:9000/daily/ponds/query?endTime=" + endTime + "&startTime=" + startTime)
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
                                Log.e("date: ", String.valueOf(pond));
                                for (int i = 0; i < pond.length(); i++) {
                                    JSONObject jsonObject1 = (JSONObject) pond.get(i);
                                    int id = jsonObject1.getInt("id");
                                    String name = jsonObject1.getString("name");
                                    double radius = jsonObject1.getDouble("radius");
                                    double height = jsonObject1.getDouble("height");
                                    double heightUsed = jsonObject1.getDouble("heightUsed");
                                    double volume = jsonObject1.getDouble("volume");
                                    double volumeUsed = jsonObject1.getDouble("volumeUsed");
                                    String time = jsonObject1.getString("time");
                                    String location = jsonObject1.getString("location");
                                    String product = jsonObject1.getString("product");
                                    String username = jsonObject1.getString("username");
                                    String data =
                                            "位置：" + location +
                                            "\n半径："+radius+"米"+
                                            "\n高："+height+"米"+
                                            "\n已用高："+heightUsed+"米"+
                                            "\n体积："+volume+"立方米"+
                                            "\n已用体积："+volumeUsed+"立方米"+
                                            "\n添加时间：" + time +
                                            "\n产品：" + product ;
                                    one1 = new AllText(name,data, id);
                                    allTextList1.add(one1);
                                    ReduceFishAddActivity.this.runOnUiThread(new Runnable() {
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
                ReduceFishAddActivity.this.runOnUiThread(new Runnable() {
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
    private void showPopWindow(AllText allText,int position) {
        View view = LayoutInflater.from(ReduceFishAddActivity.this).inflate(R.layout.pop_plusreduce, null);
        TextView editText = view.findViewById(R.id.et_1);
        editText.setText(allText.getName());
        TextView editText2 = view.findViewById(R.id.data);
        editText2.setText(allText.getData());
        Button button=view.findViewById(R.id.make_text);
        System.out.println(allText.getNum());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ReduceFishAddActivity.this, ReduceFishChangeActivity.class);
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


}


