package com.example.newag.mvp.ui.plus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;
import com.example.newag.intercept.LoginIntercept;
import com.example.newag.mvp.ui.monitor.Item.DataActivity;
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

public class RaisePlusActivity extends BaseActivity {
    public String names;
    public String period;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }
    @OnClick(R.id.tb2)
    void click1(){
        finish();
    }
    @BindView(R.id.commit)
    Button commit;
    @BindView(R.id.stock)
    Spinner stock;
    @BindView(R.id.inventory)
    EditText inventory;
    @BindView(R.id.inventoryUnit)
    TextView inventoryUnit;
    public String[] proMid;
    public String[] pondMid;
    public int[] proId;
    public int[] fieldId;
    public int FPro;
    private ArrayAdapter<CharSequence> adapterPro;
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

    public String getFPondName() {
        return FPondName;
    }

    public void setFPondName(String FPondName) {
        this.FPondName = FPondName;
    }

    public int FPond;
    public String FPondName;
    @Override
    protected void initBaseData() {

    }

    @Override
    protected void baseConfigView() {
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        int ID=bundle.getInt("ID");
        setMidId(ID);
        getItemW(ID);getItem(ID);
        postSyncPro("feed");
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postSyncPlus();
            }
        });
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_raise_plus;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

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
                    midA[0]="???";
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
                        midString=new String[pond.length()];
                        for (int i = 0; i < pond.length(); i++) {
                            JSONObject jsonObject1= (JSONObject) pond.get(i);
                            int id=jsonObject1.getInt("id");
                            String name=jsonObject1.getString("name");
                            String type=jsonObject1.getString("type");
                            type1=type;
                            if (type.equals(data)){
                                midId[t] = id;
                                t++;
                                midString[i] = name;
                                midA = insert(midString,midA);
                            }
                        }

                    }
                    setProMid(midA);
                    setProId(midId);
                    RaisePlusActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterPro = new ArrayAdapter<CharSequence>(RaisePlusActivity.this,android.R.layout.simple_spinner_item, getProMid());
                            stock.setAdapter(adapterPro);
                            stock.setOnItemSelectedListener(new RaisePlusActivity.OnItemSelectedListenerImplPro());
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
    private void getItem(int id) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept())
                .build();
        Request requestPond = new Request.Builder()
                .get()
                .url("http://124.222.111.61:9000/daily/ponds/query/"+id)
                .build();
        new Thread(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                try {
                    Call call = httpClient.newCall(requestPond);
                    Response response = call.execute();
                    assert response.body() != null;
                    String responseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONObject fin=jsonObject.getJSONObject("data");
                    setNames(fin.getString("name"));
                    setMidId(fin.getInt("id"));
                    Log.e("run: ", responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void postSyncPlus() {
        System.out.println(getFPro()+"   "+getNames()+getPeriod());
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("num", String.valueOf(inventory.getText()));
        paramsMap.put("id", String.valueOf(getFPro()));
        paramsMap.put("operation","feed");
        paramsMap.put("periodId",getPeriod());
        paramsMap.put("productionName", String.valueOf(getNames()));
        paramsMap.put("PlusOrSub","sub");
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
                .url("http://124.222.111.61:9000/daily/operation/addOperation")
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
                        Toast.makeText(RaisePlusActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        if(jsonObject1.getString("msg").equals("????????????")){
                            RaisePlusActivity.this.finish();
                        }
                        Looper.loop();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });
    }
    private void getItemW(int id){
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept())
                .build();
        Request requestPond = new Request.Builder()
                .get()
                .url("http://124.222.111.61:9000/daily/period/queryPeriodById/"+id)
                .build();
        new Thread(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                try {
                    Call call = httpClient.newCall(requestPond);
                    Response response = call.execute();
                    assert response.body() != null;
                    String responseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    System.out.println(jsonObject);
                    JSONObject fin = jsonObject.getJSONObject("data");
                    setPeriod(fin.getString("id"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    protected static String[] insert(String[] arr, String... str) {
        int size = arr.length; // ?????????????????????
        int newSize = size + str.length; // ????????????????????????????????????????????????

        // ???????????????????????????
        String[] tmp = new String[newSize];
        // ????????????????????????????????????????????????????????????????????????
        for (int i = 0; i < size; i++) {
            tmp[i] = arr[i];
        }
        // ???????????????????????????????????????
        for (int i = size; i < newSize; i++) {
            tmp[i] = str[i - size];
        }
        return tmp; // ????????????????????????????????????
    }
}