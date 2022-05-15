package com.example.newag.mvp.ui.plus;

import android.annotation.SuppressLint;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

public class CostFishPlusActivity extends BaseActivity {
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.commit)
    Button commit;
    @BindView(R.id.more)
    EditText more;
    @BindView(R.id.inventory)
    EditText inventory;
    @BindView(R.id.inventoryUnit)
    EditText inventoryUnit;
    @BindView(R.id.price)
    EditText price;
    @BindView(R.id.stock)
    Spinner stock;
    @BindView(R.id.factory)
    EditText factory;
    @OnClick(R.id.tb2)
    void  onClick2(){
        finish();
    }
    public int[] midIn;
    public String[] db;

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
    public String[] unit;

    public String[] getUnit() {
        return unit;
    }

    public void setUnit(String[] unit) {
        this.unit = unit;
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
                .url("http://124.222.111.61:9000/daily/input/queryAll")
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
                    String[] midS=new String[2];
                    String[] midw=new String[1000];
                    midS[0]="不添加";
                    midS[1]="新添加到库存";
                    String type2 = null;
                    for (Map.Entry<String, JsonArray> entry : map.entrySet()) {
                        JsonArray mapValue = entry.getValue();
                        JSONArray pond = new JSONArray(String.valueOf(mapValue));
                        Log.e("pond1: ", String.valueOf(pond));
                        newone=new String[pond.length()];
                        for (int i = 0; i < pond.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) pond.get(i);
                            int id = jsonObject1.getInt("id");
                            String type = jsonObject1.getString("type");
                            String unit=jsonObject1.getString("inventoryUnit");
                            type2=type;
                            if (type.equals("fish")){
                                midI[t] = id;
                                midw[t]=unit;
                                t++;
                                String name = jsonObject1.getString("name");
                                newone[i] = name;
                            }
                        }
                        if (type2.equals("fish")) {
                            midS = insert(midS, newone);
                        }
                    }
                    setMidIn(midI);
                    setDb(midS);
                    setUnit(midw);
                    String[] finalMidS = midS;
                    CostFishPlusActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new ArrayAdapter<CharSequence>(CostFishPlusActivity.this,android.R.layout.simple_spinner_item, finalMidS);
                            stock.setAdapter(adapter);
                            stock.setOnItemSelectedListener(new CostFishPlusActivity.OnItemSelectedListenerImpl());
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private class OnItemSelectedListenerImpl implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String city = (String) parent.getItemAtPosition(position);
            Log.e( "onItemSelected: ", String.valueOf(id));
            String[] BS=getDb();
            int[] BC=getMidIn();
            String[] Unit=getUnit();
            if (city.equals("新添加到库存")) {
                inventoryUnit.setText("");
                name.setText("");
                inventoryUnit.setEnabled(true);
                name.setEnabled(true);
            }else if(city.equals("不添加")) {
                inventoryUnit.setText("");
                name.setText("");
                inventoryUnit.setEnabled(true);
                name.setEnabled(true);
            }else{
                for (int i = 2; i < BS.length; i++) {
                    if(city.equals(BS[i])){
                        inventoryUnit.setText(Unit[i-2]);
                        name.setText(city);
                        inventoryUnit.setEnabled(false);
                        name.setEnabled(false);
                        break;
                    }
                }
            }
            commit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (city.equals("新添加到库存")) {
                        HashMap<String, String> paramsMap = new HashMap<>();
                        paramsMap.put("name", String.valueOf(name.getText()));
                        paramsMap.put("inventory", String.valueOf(inventory.getText()));
                        paramsMap.put("inventoryUnit", String.valueOf(inventoryUnit.getText()));
                        paramsMap.put("factory", String.valueOf(factory.getText()));
                        paramsMap.put("note", String.valueOf(more.getText()));
                        paramsMap.put("type", "fish");
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
                                .url("http://124.222.111.61:9000/daily/input/add")
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
                                        Toast.makeText(CostFishPlusActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                                        if (jsonObject1.getString("msg").equals("添加成功")) {
                                            CostFishPlusActivity.this.finish();
                                        }
                                        Looper.loop();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }).start();
                            }
                        });
                        postSync();
                    }else if(city.equals("不添加")){
                        postSync();
                    }else{
                        for (int i = 2; i < BS.length; i++) {
                            if(city.equals(BS[i])){
                                postSync(BC[i-2]);
                            }
                        }
                    }
                }
            });
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
        }

    }
    @Override
    protected int layoutId() {
        return R.layout.activity_plusfishvegetablefeed;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }
    public void postSync(int id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        String id1=String.valueOf(id);
        paramsMap.put("inputId", id1);
        paramsMap.put("price", String.valueOf(price.getText()));
        paramsMap.put("note", String.valueOf(more.getText()));
        paramsMap.put("weight", String.valueOf(inventory.getText()));
        paramsMap.put("type", "fish");
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
                .url("http://124.222.111.61:9000/daily/cost/addCosts")
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
                        Toast.makeText(CostFishPlusActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        if(jsonObject1.getString("msg").equals("添加成功")){
                            CostFishPlusActivity.this.finish();
                        }
                        Looper.loop();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });
    }
    public void postSync() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("name", String.valueOf(name.getText()));
        paramsMap.put("price", String.valueOf(price.getText()));
        paramsMap.put("note", String.valueOf(more.getText()));
        paramsMap.put("weight", String.valueOf(inventory.getText()));
        paramsMap.put("weightUnit", String.valueOf(inventoryUnit.getText()));
        paramsMap.put("type", "fish");
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
                .url("http://124.222.111.61:9000/daily/cost/addCosts")
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
                        Toast.makeText(CostFishPlusActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        if(jsonObject1.getString("msg").equals("添加成功")){
                            CostFishPlusActivity.this.finish();
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
