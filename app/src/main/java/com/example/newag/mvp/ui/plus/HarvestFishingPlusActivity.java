package com.example.newag.mvp.ui.plus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;
import com.example.newag.intercept.LoginIntercept;
import com.example.newag.mvp.model.bean.Icon;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
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

public class HarvestFishingPlusActivity extends BaseActivity {
    @OnClick(R.id.tb2)
    void click1(){
        finish();
    }
    @BindView(R.id.commit)
    Button commit;
    @BindView(R.id.period)
    Spinner stock;
    @BindView(R.id.field)
    Spinner field;
    @BindView(R.id.inventory)
    EditText inventory;
    @BindView(R.id.inventoryUnit)
    TextView inventoryUnit;
    @BindView(R.id.RG)
    RadioGroup radioGroup;
    @BindView(R.id.sow1)
    RadioButton sow1;
    @BindView(R.id.sow2)
    RadioButton sow2;
    public String[] proMid;
    public String[] pondMid;
    public int[] proId;
    public int[] fieldId;
    public int FPro;
    private ArrayAdapter<CharSequence> adapterField;
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
        postSyncPro("vegetable");
        postSyncPond();
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postSyncPlus("harvest");
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.sow1:
                        postSyncPond();
                        postSyncPro("vegetable");
                        commit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                postSyncPlus("harvest");
                            }
                        });
                        break;
                    case R.id.sow2:
                        System.out.println("a");
                        postW();
                        postSyncPro("fish");
                        System.out.println("b");
                        commit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                postSyncPlus("fishing");
                            }
                        });
                        break;
                }
            }
        });
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_harvest_fishing_plus;
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
                .url("http://124.222.111.61:9000/daily/operation/queryOperation")
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
                        for (int i = 0; i < pond.length(); i++) {
                            midString=new String[1];
                            JSONObject jsonObject1= (JSONObject) pond.get(i);
                            int id=jsonObject1.getInt("associatedId");
                            String name=jsonObject1.getString("name");
                            String type=jsonObject1.getString("type");
                            String operationType=jsonObject1.getString("operationType");
                            if (type.equals(data)&&operationType.equals("sub")){
                                midId[t+1] = id;
                                t++;
                                midString[0] = name;
                                midA = insert(midA,midString);
                            }
                        }
                    }
                    setProMid(midA);
                    setProId(midId);
                    HarvestFishingPlusActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterPro = new ArrayAdapter<CharSequence>(HarvestFishingPlusActivity.this,android.R.layout.simple_spinner_item, getProMid());
                            stock.setAdapter(adapterPro);
                            stock.setOnItemSelectedListener(new HarvestFishingPlusActivity.OnItemSelectedListenerImplPro());
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
    public void postW() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
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
                    String[] midA=new String[1];
                    midA[0]="???";
                    int[] midId=new int[1000];
                    String[] midString = new String[1];
                    Call call = httpClient.newCall(requestPond);
                    Response response = call.execute();
                    assert response.body() != null;
                    String responseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONArray pond = jsonObject.getJSONArray("data");
                    Log.e("run: ", responseData);
                    for (int i = 0; i < pond.length(); i++) {
                        JSONObject jsonObject1 = (JSONObject) pond.get(i);
                        int id = jsonObject1.getInt("id");
                        String name = jsonObject1.getString("name");
                        midString[0]=name;
                        midA=insert(midA,midString);
                        midId[i]=id;
                    }
                    setFieldId(midId);
                    setPondMid(midA);
                    HarvestFishingPlusActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterField = new ArrayAdapter<CharSequence>(HarvestFishingPlusActivity.this,android.R.layout.simple_spinner_item, getPondMid());
                            field.setAdapter(adapterField);
                            field.setOnItemSelectedListener(new OnItemSelectedListenerImplField());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void postSyncPond() {
        OkHttpClient httpClient=new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept())
                .build();
        Request requestPond = new Request.Builder()
                .get()
                .url("http://124.222.111.61:9000/daily/field/queryAll")
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
                    HarvestFishingPlusActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterField = new ArrayAdapter<CharSequence>(HarvestFishingPlusActivity.this,android.R.layout.simple_spinner_item, getPondMid());
                            field.setAdapter(adapterField);
                            field.setOnItemSelectedListener(new HarvestFishingPlusActivity.OnItemSelectedListenerImplField());
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
    public void postSyncPlus(String data) {
        System.out.println(getFPro()+"   "+getFPondName());
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("num", String.valueOf(inventory.getText()));
        paramsMap.put("periodId", String.valueOf(getFPro()));
        paramsMap.put("productionName",getFPondName());
        paramsMap.put("operation",data);
        paramsMap.put("PlusOrSub","plus");
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
                        Toast.makeText(HarvestFishingPlusActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        if(jsonObject1.getString("msg").equals("????????????")){
                            HarvestFishingPlusActivity.this.finish();
                        }
                        Looper.loop();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });
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