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

import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.PopupWindow;
import android.widget.TextView;

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

import com.example.newag.mvp.ui.plus.ReduceFieldPlus;
import com.example.newag.mvp.ui.plus.ReduceFishPlus;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.example.newag.mvp.ui.change.ReduceAdd;
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

public class ReduceAddActivity extends BaseActivity implements View.OnClickListener{
    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.fishPlus)
    void onClick11(View view) {
        Intent intent = new Intent();
        intent.setClass(ReduceAddActivity.this, ReduceFishPlus.class);
        startActivity(intent);
    }
    @OnClick(R.id.fieldPlus)
    void onClick12(View view) {
        Intent intent = new Intent();
        intent.setClass(ReduceAddActivity.this, ReduceFieldPlus.class);
        startActivity(intent);
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
    @BindView(R.id.textView2)
    TextView field;
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
        btnDate.setText(str);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(ReduceAddActivity.this,  2, btnDate, calendar);
            }
        });
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);//设置布局管理器，cv工程
        recyclerView.setLayoutManager(linearLayoutManager);//为recycleView添加布局管理器，cv
        adapter=new AllTextMasterAdapter(this,data_1);//定义一个新的大适配器（AllTextMasterAdapter），并且把数据传进去
        recyclerView.setAdapter(adapter);//设置适配器
        postSync();
        InquireField();
        InquirePond();
        refreshLayout.setColorSchemeResources(R.color.blue,R.color.blue);//设置下拉刷新主题（最多支持三种颜色变换，这里两种)
        refreshLayout.setOnRefreshListener(() -> {
            postSync();
            InquireField();
            InquirePond();
            refreshLayout.setRefreshing(false);
        });
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();//展示popWindow的方法
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
            }
        });
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_reduceadd;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }
    @SuppressLint({"NotifyDataSetChanged", "NonConstantResourceId"})
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.make_text:{
                adapter.setCheckbox(true);
                adapter.notifyDataSetChanged();
                popupWindow.dismiss();//销毁popWindow
                @SuppressLint("InflateParams") View rootView= LayoutInflater.from(ReduceAddActivity.this).inflate(R.layout.activity_reduceadd,null);
                newPopWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
            }
            break;
            case R.id.btn_Date:{
                showDatePickerDialog(this,  2, btnDate, calendar);;
                break;
            }
            case R.id.back_test:
                popupWindow.dismiss();
                break;
            case R.id.delete:
                adapter.setCheckbox(false);

                adapter.notifyDataSetChanged();
                newPopWindow.dismiss();
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
                    ReduceAddActivity.this.runOnUiThread(new Runnable() {
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
    public void InquireField(){
        OkHttpClient httpClient=new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept())
                .build();
        Request request = new Request.Builder()
                .get()
                .url("http://124.222.111.61:9000/daily/field/queryCount")
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call call = httpClient.newCall(request);
                    Response response=call.execute();
                    assert response.body() != null;
                    String responseField=response.body().string();
                    ReduceAddActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(responseField);
                                String fd=jsonObject.getString("data");
                                field.setText("菜地\n"+ fd+"个");
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
        Request requestField = new Request.Builder()
                .get()
                .url("http://124.222.111.61:9000/daily/field/queryAll")
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
                            double length=jsonObject1.getDouble("length");
                            double width=jsonObject1.getDouble("width");
                            double area=jsonObject1.getDouble("area");
                            double areaUsed=jsonObject1.getDouble("areaUsed");
                            double lengthUsed=jsonObject1.getDouble("lengthUsed");
                            double widthUsed=jsonObject1.getDouble("widthUsed");
                            String orientation=jsonObject1.getString("orientation");
                            String time=jsonObject1.getString("time");
                            String location=jsonObject1.getString("location");
                            String product=jsonObject1.getString("product");
                            String username=jsonObject1.getString("username");
                            String data="名称："+name+"菜地"+
                                    "\n位置："+location+
                                    "\n长："+length+
                                    "\n宽："+width+
                                    "\n已用长："+lengthUsed+
                                    "\n已用宽："+widthUsed+
                                    "\n面积："+area+
                                    "\n已用面积："+areaUsed+
                                    "\n方向："+orientation+
                                    "\n添加时间："+time+
                                    "\n产品："+product+
                                    "\n用户名："+username;
                            one1=new AllText(data,id);
                            allTextList1.add(one1);
                            ReduceAddActivity.this.runOnUiThread(new Runnable() {
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
        new Thread(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                try {
                    Call call = httpClient.newCall(requestPond);
                    Response response=call.execute();
                    assert response.body() != null;
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
                            double length=jsonObject1.getDouble("length");
                            double width=jsonObject1.getDouble("width");
                            double height=jsonObject1.getDouble("height");
                            double lengthUsed=jsonObject1.getDouble("lengthUsed");
                            double widthUsed=jsonObject1.getDouble("widthUsed");
                            double heightUsed=jsonObject1.getDouble("heightUsed");
                            String orientation=jsonObject1.getString("orientation");
                            double volume=jsonObject1.getDouble("volume");
                            double volumeUsed=jsonObject1.getDouble("volumeUsed");
                            String time=jsonObject1.getString("time");
                            String location=jsonObject1.getString("location");
                            String product=jsonObject1.getString("product");
                            String username=jsonObject1.getString("username");
                            String data="名称："+name+"鱼池"+
                                    "\n位置："+location+
                                    "\n长："+length+
                                    "\n宽："+width+
                                    "\n高："+height+
                                    "\n已用长："+lengthUsed+
                                    "\n已用宽："+widthUsed+
                                    "\n已用高："+heightUsed+
                                    "\n方向："+orientation+
                                    "\n体积："+volume+
                                    "\n已用体积："+volumeUsed+
                                    "\n添加时间："+time+
                                    "\n产品："+product+
                                    "\n用户名："+username;
                            one1=new AllText(data,id);
                            allTextList1.add(one1);
                            ReduceAddActivity.this.runOnUiThread(new Runnable() {
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
    public static void showDatePickerDialog(Activity activity, int themeResId, Button bt, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器(How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作

                bt.setText(year + "年\n" + (monthOfYear + 1) + "月" );
            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void showPopWindow(AllText allText,int position) {
        View view = LayoutInflater.from(ReduceAddActivity.this).inflate(R.layout.pop_plusreduce, null);
        EditText editText = view.findViewById(R.id.et_1);
        editText.setText(allText.getName());
        Button button=view.findViewById(R.id.make_text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ReduceAddActivity.this, ReduceAdd.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("data",allText);
                bundle.putSerializable("position",position);
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
            }
        });
        popupWindow = new PopupWindow(view, RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT, true);//设置popwindow的属性（布局，x，y，true）
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);//展示自定义的popwindow，（放哪个布局里，放布局里的位置，x，y），cv工程
    }
    private void showPopWindow() {
        //定义一个view，其中包含popwindow的布局文件
        View view1= LayoutInflater.from(ReduceAddActivity.this).inflate(R.layout.footer_batch,null);
        popupWindow =new PopupWindow(view1, RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT,true);//设置popwindow的属性（布局，x，y，true）
        TextView make_text=(TextView)view1.findViewById(R.id.make_text);
        TextView back_test=(TextView)view1.findViewById(R.id.back_test);
        make_text.setOnClickListener(this);
        back_test.setOnClickListener(this);
        //定义一个view，其中包含main4的布局文件
        View rootView=LayoutInflater.from(ReduceAddActivity.this).inflate(R.layout.activity_reduceadd,null);
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);//展示自定义的popwindow，（放哪个布局里，放布局里的位置，x，y），cv工程
        View view2=LayoutInflater.from(ReduceAddActivity.this).inflate(R.layout.ppw_delete,null);
        newPopWindow=new PopupWindow(view2,RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT,false);
        Button button_delete=(Button) view2.findViewById(R.id.delete);
        button_delete.setOnClickListener(this);
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


