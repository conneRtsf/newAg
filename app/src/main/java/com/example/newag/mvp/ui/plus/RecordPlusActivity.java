package com.example.newag.mvp.ui.plus;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;
import com.example.newag.intercept.LoginIntercept;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
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

public class RecordPlusActivity extends BaseActivity {
    @BindView(R.id.plan)
    Spinner plan;
    @BindView(R.id.spinner_produce)
    Spinner produce;
    @BindView(R.id.note)
    EditText note;
    @OnClick(R.id.commit)
    void po(){
        postSync(getB(),getC());
    }
    public int[] midIn;
    public int[] midIn3;
    public String[] db;

    public int[] getMidIn3() {
        return midIn3;
    }

    public void setMidIn3(int[] midIn3) {
        this.midIn3 = midIn3;
    }

    public int a;
    public int b;
    public int c;

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int[] midIn2;
    public String[] db2;
    private ArrayAdapter<CharSequence> adapter;
    public int[] getMidIn() {
        return midIn;
    }

    public void setMidIn(int[] midIn) {
        this.midIn = midIn;
    }

    public String[] getDb() {
        return db;
    }

    public void setDb(String[] db) {
        this.db = db;
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

    @Override
    protected void initBaseData() {

    }

    @Override
    protected void baseConfigView() {
        OkHttpClient httpClient=new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept())
                .build();
        Request requestField = new Request.Builder()
                .get()
                .url("http://124.222.111.61:9000/daily/ponds/query")
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
                    RecordPlusActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new ArrayAdapter<CharSequence>(RecordPlusActivity.this,android.R.layout.simple_spinner_item, finalMidS);
                            produce.setAdapter(adapter);
                            produce.setOnItemSelectedListener(new RecordPlusActivity.OnItemSelectedListenerImpl1());
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
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
                    break;
                }
            }
            OkHttpClient httpClient2=new OkHttpClient.Builder()
                    .addInterceptor(new LoginIntercept())
                    .build();
            Request requestField2 = new Request.Builder()
                    .get()
                    .url("http://124.222.111.61:9000/daily/record/queryPlan?productionUnit="+getA())
                    .build();
            new Thread(new Runnable() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void run() {
                    try {
                        int[] midI=new int[1000];
                        int[] midI2=new int[1000];
                        String[] misS=new String[0];
                        String[] newone;
                        Call call = httpClient2.newCall(requestField2);
                        Response response=call.execute();
                        assert response.body() != null;
                        String responseData=response.body().string();
                        JSONObject jsonObject = new JSONObject(responseData);
                        Log.e("run213: ", responseData);
                        JSONArray json=jsonObject.getJSONArray("data");
                        for (int i = 0; i < json.length(); i++) {
                            newone=new String[1];
                            JSONObject js= (JSONObject) json.get(i);
                            midI[i]=js.getInt("period");
                            JSONObject json1=js.getJSONObject("raisePlan");
                            midI2[i]=json1.getInt("id");
                            System.out.println(midI2[i]+"  "+midI[i]);
                            String name = json1.getString("name");
                            newone[0] = name;
                            misS=insert(misS,newone);
                        }
                        setMidIn2(midI);
                        setMidIn3(midI2);
                        setDb2(misS);
                        String[] finalMidS = misS;
                        RecordPlusActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new ArrayAdapter<CharSequence>(RecordPlusActivity.this,android.R.layout.simple_spinner_item, finalMidS);
                                plan.setAdapter(adapter);
                                plan.setOnItemSelectedListener(new OnItemSelectedListenerImpl2());
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
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
            int[] BCs=getMidIn3();
            for (int i = 0; i < BS.length; i++) {
                System.out.println(BS[i]+BC[i]);
                if (city.equals(BS[i])) {
                    setB(BC[i]);
                    setC(BCs[i]);
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
    protected int layoutId() {
        return R.layout.activity_plusrecord;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

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
    public void postSync(int A,int B) {
        System.out.println(A+"+"+B);
        HashMap<String, String> paramsMap = new HashMap<>();
        String As=String.valueOf(A);
        String BS=String.valueOf(B);
        paramsMap.put("PlanId", BS);
        paramsMap.put("PeriodId", As);
        paramsMap.put("note", String.valueOf(note.getText()));
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
                .url("http://124.222.111.61:9000/daily/record/addRecord")
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
                        Toast.makeText(RecordPlusActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        if(jsonObject1.getString("msg").equals("记录插入成功")){
                            RecordPlusActivity.this.finish();
                        }
                        Looper.loop();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });
    }
}
