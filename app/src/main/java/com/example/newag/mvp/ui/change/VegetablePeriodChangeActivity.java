package com.example.newag.mvp.ui.change;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;
import com.example.newag.intercept.LoginIntercept;
import com.example.newag.mvp.model.bean.AllText;
import com.example.newag.mvp.ui.plus.VegetablePeriodPlusActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VegetablePeriodChangeActivity extends BaseActivity {
    @OnClick(R.id.commit)
    public void cl(){
        postSync(getA(),getB(),getC(),getName1(),getChooseTime());
    }
    @BindView(R.id.spinner_produce)
    Spinner produce;
    @BindView(R.id.inputNum)
    EditText inputNum;
    @BindView(R.id.plan)
    Spinner plan;
    @BindView(R.id.nameId)
    Spinner nameId;
    @BindView(R.id.btn_Date)
    Button btnDate;

    public String getChooseTime() {
        return chooseTime;
    }

    public void setChooseTime(String chooseTime) {
        this.chooseTime = chooseTime;
    }

    String chooseTime;
    public int[] midIn;
    public String[] db;
    public int a;
    public String b;
    public int c;
    public int[] midIn2;
    Calendar calendar= Calendar.getInstance(Locale.CHINA);

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String name1;

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String[] db2;
    public int[] midIn3;

    public int[] getMidIn2() {
        return midIn2;
    }

    public void setMidIn2(int[] midIn2) {
        this.midIn2 = midIn2;
    }

    public String[] getDb2() {
        return db2;
    }

    public void setDb2(String[] db2) {
        this.db2 = db2;
    }

    public int[] getMidIn3() {
        return midIn3;
    }

    public void setMidIn3(int[] midIn3) {
        this.midIn3 = midIn3;
    }

    public String[] getDb3() {
        return db3;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public void setDb3(String[] db3) {
        this.db3 = db3;
    }

    public String[] db3;
    public String[] getDb() {
        return db;
    }

    public void setDb(String[] db) {
        this.db = db;
    }

    public int[] getMidIn() {
        return midIn;
    }

    public void setMidIn(int[] midIn) {
        this.midIn = midIn;
    }

    private ArrayAdapter<CharSequence> adapter;
    @Override
    protected void initBaseData() {

    }

    @Override
    protected void baseConfigView() {
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(VegetablePeriodChangeActivity.this,  2, btnDate, calendar);;
            }
        });
        OkHttpClient httpClient=new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept())
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
                    JSONObject jsonObject = new JSONObject(responseData);
                    String json=jsonObject.getJSONObject("data").toString();
                    Map<String, JsonArray> map = new Gson()
                            .fromJson(json, new TypeToken<Map<String, JsonArray>>() {}
                                    .getType());
                    String[] newone;
                    int t=0;
                    int[] midI=new int[1000];
                    String[] midS=new String[0];
                    for (Map.Entry<String, JsonArray> entry : map.entrySet()) {
                        JsonArray mapValue = entry.getValue();
                        JSONArray pond = new JSONArray(String.valueOf(mapValue));
                        Log.e("pond1: ", String.valueOf(pond));
                        newone=new String[pond.length()];
                        for (int i = 0; i < pond.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) pond.get(i);
                            int id = jsonObject1.getInt("id");
                            midI[t] = id;
                            t++;
                            String name = jsonObject1.getString("name");
                            newone[i] = name;
                        }
                        midS = insert(midS, newone);
                    }
                    if(midS.equals(null)){
                        midS=new String[1];
                        midS[0]="空";
                    }
                    setMidIn(midI);
                    setDb(midS);
                    String[] finalMidS = midS;
                    VegetablePeriodChangeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new ArrayAdapter<CharSequence>(VegetablePeriodChangeActivity.this,android.R.layout.simple_spinner_item, finalMidS);
                            produce.setAdapter(adapter);
                            produce.setOnItemSelectedListener(new VegetablePeriodChangeActivity.OnItemSelectedListenerImpl1());
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        OkHttpClient httpClient2=new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept())
                .build();
        Request requestField2 = new Request.Builder()
                .get()
                .url("http://124.222.111.61:9000/daily/plan/queryAll")
                .build();
        new Thread(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                try {
                    Call call = httpClient2.newCall(requestField2);
                    Response response=call.execute();
                    assert response.body() != null;
                    String responseData=response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    String json=jsonObject.getJSONObject("data").toString();
                    Map<String, JsonArray> map = new Gson()
                            .fromJson(json, new TypeToken<Map<String, JsonArray>>() {}
                                    .getType());
                    String[] newone;
                    int t=0;
                    int[] midI=new int[1000];
                    String[] midS=new String[0];
                    for (Map.Entry<String, JsonArray> entry : map.entrySet()) {
                        JsonArray mapValue = entry.getValue();
                        JSONArray pond = new JSONArray(String.valueOf(mapValue));
                        Log.e("pond1: ", String.valueOf(pond));
                        newone=new String[pond.length()];
                        for (int i = 0; i < pond.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) pond.get(i);
                            int id = jsonObject1.getInt("id");
                            midI[t] = id;
                            t++;
                            String name = jsonObject1.getString("name");
                            newone[i] = name;
                        }
                        midS = insert(midS, newone);
                    }
                    if(midS.equals(null)){
                        midS=new String[1];
                        midS[0]="空";
                    }
                    setMidIn2(midI);
                    setDb2(midS);
                    String[] finalMidS = midS;
                    VegetablePeriodChangeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new ArrayAdapter<CharSequence>(VegetablePeriodChangeActivity.this,android.R.layout.simple_spinner_item, finalMidS);
                            plan.setAdapter(adapter);
                            plan.setOnItemSelectedListener(new VegetablePeriodChangeActivity.OnItemSelectedListenerImpl2());
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        OkHttpClient httpClient3=new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept())
                .build();
        Request requestField3 = new Request.Builder()
                .get()
                .url("http://124.222.111.61:9000/daily/input/queryAll")
                .build();
        new Thread(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                try {
                    Call call = httpClient3.newCall(requestField3);
                    Response response=call.execute();
                    assert response.body() != null;
                    String responseData=response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    Log.e("runsc: ", responseData);
                    String json=jsonObject.getJSONObject("data").toString();
                    Map<String, JsonArray> map = new Gson()
                            .fromJson(json, new TypeToken<Map<String, JsonArray>>() {}
                                    .getType());
                    String[] newone;
                    int t=0;
                    int[] midI=new int[1000];
                    String[] midS=new String[0];
                    String type2 = "a";
                    for (Map.Entry<String, JsonArray> entry : map.entrySet()) {
                        type2 = "a";
                        JsonArray mapValue = entry.getValue();
                        JSONArray pond = new JSONArray(String.valueOf(mapValue));
                        Log.e("pond2: ", String.valueOf(pond));
                        newone=new String[pond.length()];
                        for (int i = 0; i < pond.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) pond.get(i);
                            int id = jsonObject1.getInt("id");
                            String type = jsonObject1.getString("type");
                            if (type.equals("vegetable")){
                                type2=type;
                                midI[t] = id;
                                t++;
                                String name = jsonObject1.getString("name");
                                newone[i] = name;
                            }
                        }
                        if (type2.equals("vegetable")) {
                            midS = insert(midS, newone);
                        }
                    }
                    if(midS.equals(null)){
                        midS=new String[1];
                        midS[0]="空";
                    }
                    setMidIn3(midI);
                    setDb3(midS);
                    String[] finalMidS = midS;
                    VegetablePeriodChangeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new ArrayAdapter<CharSequence>(VegetablePeriodChangeActivity.this,android.R.layout.simple_spinner_item, finalMidS);
                            nameId.setAdapter(adapter);
                            nameId.setOnItemSelectedListener(new VegetablePeriodChangeActivity.OnItemSelectedListenerImpl3());
                        }
                    });
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

                bt.setText(year + "年\n" + (monthOfYear + 1) + "月" +dayOfMonth+"日");
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
                String startTime = sdf1.format(calendar.getTime());
                setChooseTime(startTime);
            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    private class OnItemSelectedListenerImpl3 implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String city = (String) parent.getItemAtPosition(position);
            Log.e("onItemSelected: ", String.valueOf(id));
            String[] BS=getDb3();
            int[] BC=getMidIn3();
            for (int i = 0; i < BS.length; i++) {
                System.out.println(BS[i]+BC[i]);
                if (city.equals(BS[i])) {
                    setC(BC[i]);
                    break;
                }
            }
        }


        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
        }
    }
    private class OnItemSelectedListenerImpl1 implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String city = (String) parent.getItemAtPosition(position);
            Log.e("onItemSelected: ", String.valueOf(id));
            String[] BS=getDb();
            int[] BC=getMidIn();
            for (int i = 0; i < BS.length; i++) {
                System.out.println(BS[i]+BC[i]);
                if (city.equals(BS[i])) {
                    setA(BC[i]);
                    setName1(BS[i]);
                    break;
                }
            }
        }


        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
        }
    }
    private class OnItemSelectedListenerImpl2 implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String city = (String) parent.getItemAtPosition(position);
            Log.e("onItemSelected: ", String.valueOf(id));
            String[] BS=getDb2();
            int[] BC=getMidIn2();
            for (int i = 0; i < BS.length; i++) {
                System.out.println(BS[i]+BC[i]);
                if (city.equals(BS[i])) {
                    setB(BS[i]);
                    break;
                }
            }
        }


        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
        }
    }
    @Override
    protected void setActivityComponent(AppComponent appComponent) {
    }
    public void postSync(int A,String B,int C,String name1,String time) {
        Log.e("postSync: ", B+"  "+A+"  "+C);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        int id= (int) bundle.getSerializable("data");
        String Id=String.valueOf(id);
        HashMap<String, String> paramsMap = new HashMap<>();
        String As=String.valueOf(A);
        String Cs=String.valueOf(C);
        paramsMap.put("id", Id);
        paramsMap.put("planName", B);
        paramsMap.put("unitName", name1);
        paramsMap.put("num", String.valueOf(inputNum.getText()));
        paramsMap.put("unitId", As);
        paramsMap.put("except", time);
        paramsMap.put("nameId", Cs);
        paramsMap.put("type", "vegetable");
        if(time.equals(null)){
            Toast.makeText(VegetablePeriodChangeActivity.this, "时间不可为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.e("postSync: ", String.valueOf(paramsMap));
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            builder.add(key, Objects.requireNonNull(paramsMap.get(key)));
        }
        RequestBody formBody = builder.build();
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept())
                .build();
        Request request = new Request.Builder()
                .post(formBody)
                .url("http://124.222.111.61:9000/daily/period/addPeriod")
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                String ResponseData = response.body().string();
                Log.e("onResponse: ", ResponseData);
                new Thread(() -> {
                    try {
                        Looper.prepare();
                        JSONObject jsonObject1 = new JSONObject(ResponseData);
                        Toast.makeText(VegetablePeriodChangeActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        if(jsonObject1.getString("msg").equals("插入成功")){
                            VegetablePeriodChangeActivity.this.finish();
                        }
                        Looper.loop();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });
    }
    @Override
    protected int layoutId() {
        return R.layout.activity_plusperiod;
    }
    private static String[] insert(String[] arr, String... str) {
        int size = arr.length; // 获取原数组长度
        int newSize = size + str.length; // 原数组长度加上追加的数据的总长度

        // 新建临时字符串数组
        String[] tmp = new String[newSize];
        // 先遍历将原来的字符串数组数据添加到临时字符串数组
        for (int i = 0; i < size; i++) {
            tmp[i] = arr[i];
        }
        // 在末尾添加上需要追加的数据
        for (int i = size; i < newSize; i++) {
            tmp[i] = str[i - size];
        }
        return tmp; // 返回拼接完成的字符串数组
    }
}