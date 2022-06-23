package com.example.newag.mvp.ui.plus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;
import com.example.newag.intercept.LoginIntercept;
import com.example.newag.mvp.model.bean.AllText;
import com.example.newag.mvp.model.bean.AllTextMaster;
import com.example.newag.mvp.ui.program.ProgramAddActivity;
import com.example.newag.mvp.ui.reduce.ReduceFieldAddActivity;
import com.example.newag.mvp.ui.reduce.ReduceFishAddActivity;
import com.example.newag.mvp.ui.stock.AdultFishStockActivity;
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
import java.util.HashMap;
import java.util.List;
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

public class SowingPlusActivity extends BaseActivity {
    @OnClick(R.id.tb2)
    void click1(){
        finish();
    }
    @BindView(R.id.time)
    Button time;
    @BindView(R.id.num)
    EditText num;
    @BindView(R.id.RG)
    RadioGroup radioGroup;
    @BindView(R.id.sow1)
    RadioButton sow1;

    public String getFPondName() {
        return FPondName;
    }

    public void setFPondName(String FPondName) {
        this.FPondName = FPondName;
    }

    public String getFPlanName() {
        return FPlanName;
    }

    public void setFPlanName(String FPlanName) {
        this.FPlanName = FPlanName;
    }

    @BindView(R.id.sow2)
    RadioButton sow2;
    @BindView(R.id.classes)
    Spinner classes;
    @BindView(R.id.field)
    Spinner field;
    @BindView(R.id.commit)
    Button commit;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;
    Calendar calendar= Calendar.getInstance(Locale.CHINA);

    public String[] proMid;
    public String[] pondMid;
    public String[] planMid;
    public int[] planId;
    public int[] proId;
    public int[] fieldId;
    public int FPro;
    public int FPond;
    public int FPlan;
    public String FPondName;
    public String FPlanName;
    public int getFPro() {
        return FPro;
    }

    public void setFPro(int FPro) {
        this.FPro = FPro;
    }

    public int getFPond() {
        return FPond;
    }

    public void setFPond(int FPond) {
        this.FPond = FPond;
    }

    public int getFPlan() {
        return FPlan;
    }

    public void setFPlan(int FPlan) {
        this.FPlan = FPlan;
    }

    private ArrayAdapter<CharSequence> adapterField;
    private ArrayAdapter<CharSequence> adapterPro;
    private ArrayAdapter<CharSequence> adapterPlan;


    public String[] getProMid() {
        return proMid;
    }

    public void setProMid(String[] proMid) {
        this.proMid = proMid;
    }

    public String[] getPondMid() {
        return pondMid;
    }

    public void setPondMid(String[] pondMid) {
        this.pondMid = pondMid;
    }

    public String[] getPlanMid() {
        return planMid;
    }

    public void setPlanMid(String[] planMid) {
        this.planMid = planMid;
    }

    public int[] getPlanId() {
        return planId;
    }

    public void setPlanId(int[] planId) {
        this.planId = planId;
    }

    public int[] getProId() {
        return proId;
    }

    public void setProId(int[] proId) {
        this.proId = proId;
    }

    public int[] getFieldId() {
        return fieldId;
    }

    public void setFieldId(int[] fieldId) {
        this.fieldId = fieldId;
    }

    @Override
    protected void initBaseData() {

    }

    @Override
    protected void baseConfigView() {
        postSyncPro("vegetable");
        postSyncPond("field");
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postSyncPlus("vegetable");
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(SowingPlusActivity.this,  2, time,calendar);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.sow1:
                        tv1.setText("种植地块");
                        tv2.setText("预计采收时间");
                        tv3.setText("投入量");
                        tv4.setText("公斤/亩");
                        postSyncPond("field");
                        postSyncPro("vegetable");
                        commit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                postSyncPlus("vegetable");
                            }
                        });
                        break;
                    case R.id.sow2:
                        tv1.setText("养殖地块");
                        tv2.setText("预计捕捞时间");
                        tv3.setText("投入量");
                        tv4.setText("公斤/条");
                        postSyncPond("ponds");
                        postSyncPro("fish");
                        commit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                postSyncPlus("fish");
                            }
                        });
                        break;
                }
            }
        });
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_sowing_plus;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }
    public void showDatePickerDialog(Activity activity, int themeResId, Button bt, Calendar calendar) {
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                bt.setText(year + "/" + (monthOfYear + 1) + "/"+dayOfMonth);
            }
        }

                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void postSyncPro(String data) {
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
                    int t=0;
                    int[] midId=new int[1000];
                    String[] midString ;
                    String[] midA=new String[1];
                    midA[0]="无";
                    String type1 = null;
                    Call call = httpClient.newCall(requestField);
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
                        JsonArray mapValue = entry.getValue();
                        JSONArray pond=new JSONArray(String.valueOf(mapValue));
                        midString=new String[1];
                        for (int i = 0; i < pond.length(); i++) {
                            JSONObject jsonObject1= (JSONObject) pond.get(i);
                            int id=jsonObject1.getInt("id");
                            String name=jsonObject1.getString("name");
                            String type=jsonObject1.getString("type");
                            if (type.equals(data)){
                                midId[t+1] = id;
                                t++;
                                midString[0] = name;
                                midA = insert(midA,midString);
                            }
                        }
                    }
                    setProMid(midA);
                    setProId(midId);
                    SowingPlusActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterPro = new ArrayAdapter<CharSequence>(SowingPlusActivity.this,android.R.layout.simple_spinner_item, getProMid());
                            classes.setAdapter(adapterPro);
                            classes.setOnItemSelectedListener(new OnItemSelectedListenerImplPro());
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private class OnItemSelectedListenerImplPro implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String pro = (String) parent.getItemAtPosition(position);
            String[] finalPro=getProMid();
            int[] finalProID=getProId();
            for (int i = 0; i < finalPro.length; i++) {
                if(pro.equals(finalPro[i])){
                    setFPro(finalProID[i]);

                    System.out.println(finalProID[i]);
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
    public void postSyncPond(String data) {
        String URL=null;
        if(data.equals("ponds")){
            URL="http://124.222.111.61:9000/daily/"+data+"/query";
        }else{
            URL="http://124.222.111.61:9000/daily/"+data+"/queryAll";
        }
        OkHttpClient httpClient=new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept())
                .build();
        Request requestPond = new Request.Builder()
                .get()
                .url(URL)
                .build();
        new Thread(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                try {
                    int t=0;
                    int[] midId=new int[1000];
                    String[] midString ;
                    String[] midA=new String[1];
                    midA[0]="无";
                    Call call = httpClient.newCall(requestPond);
                    Response response=call.execute();
                    assert response.body() != null;
                    String responseData=response.body().string();
                    System.out.println(responseData);
                    JSONObject jsonObject = new JSONObject(responseData);
                    String json=jsonObject.getJSONObject("data").toString();
                    Log.e("run: ", responseData);
                    Map<String, JsonArray> map = new Gson()
                            .fromJson(json, new TypeToken<Map<String, JsonArray>>() {}
                                    .getType());
                    for (Map.Entry<String, JsonArray> entry : map.entrySet()) {
                        JsonArray mapValue = entry.getValue();
                        JSONArray pond=new JSONArray(String.valueOf(mapValue));
                        Log.e("pond: ", String.valueOf(pond));
                        midString=new String[pond.length()];
                        for (int i = 0; i < pond.length(); i++) {
                            JSONObject jsonObject1= (JSONObject) pond.get(i);
                            int id=jsonObject1.getInt("id");
                            String name=jsonObject1.getString("name");
                            midId[t+1] = id;
                            t++;
                            midString[i] = name;
                        }
                        midA = insert(midA,midString);
                    }
                    setFieldId(midId);
                    setPondMid(midA);
                    SowingPlusActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterField = new ArrayAdapter<CharSequence>(SowingPlusActivity.this,android.R.layout.simple_spinner_item, getPondMid());
                            field.setAdapter(adapterField);
                            field.setOnItemSelectedListener(new OnItemSelectedListenerImplField());
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private class OnItemSelectedListenerImplField implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String pro = (String) parent.getItemAtPosition(position);
            String[] finalPro=getPondMid();
            int[] finalProID=getFieldId();
            for (int i = 0; i < finalPro.length; i++) {
                if(pro.equals(finalPro[i])){
                    setFPond(finalProID[i]);
                    setFPondName(pro);
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
    public void postSyncPlus(String type) {
        System.out.println(getFPro());
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("num", String.valueOf(num.getText()));
        paramsMap.put("unitId", String.valueOf(getFPond()));
        paramsMap.put("except", String.valueOf(time.getText()));
        paramsMap.put("nameId", String.valueOf(getFPro()));
        paramsMap.put("associatedId", "1");
        paramsMap.put("type", "field");
        paramsMap.put("unitName",getFPondName());
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
                        Toast.makeText(SowingPlusActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        if(jsonObject1.getString("msg").equals("插入成功")){
                            SowingPlusActivity.this.finish();
                        }
                        Looper.loop();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });
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