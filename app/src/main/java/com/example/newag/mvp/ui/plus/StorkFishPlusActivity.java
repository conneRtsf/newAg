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

public class StorkFishPlusActivity extends BaseActivity {
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.inventory)
    EditText inventory;
    @BindView(R.id.inventoryUnit)
    EditText inventoryUnit;
    @BindView(R.id.factory)
    EditText factory;
    @BindView(R.id.note)
    EditText note;
    @BindView(R.id.commit)
    Button commit;
    @BindView(R.id.stock)
    Spinner stock;
    @OnClick(R.id.tb2)
    void  onClick2(){
        finish();
    }
    public int[] midIn;

    public String[] getmFactory() {
        return mFactory;
    }

    public void setmFactory(String[] mFactory) {
        this.mFactory = mFactory;
    }

    public String[] db;
    public String[] mFactory;

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
                    String[] midS=new String[1];
                    String[] midw=new String[1000];
                    String[] midf=new String[1000];
                    midS[0]="新添加到库存";
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
                            String factory=jsonObject1.getString("factory");
                            type2=type;
                            if (type.equals("fish")){
                                midI[t] = id;
                                midw[t]=unit;
                                midf[t]=factory;
                                t++;
                                String name = jsonObject1.getString("name");
                                newone[i] = name;
                            }
                        }
                        if (type2.equals("fish")) {
                            midS = insert(midS, newone);
                        }
                    }
                    setmFactory(midf);
                    setMidIn(midI);
                    setDb(midS);
                    setUnit(midw);
                    String[] finalMidS = midS;
                    StorkFishPlusActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new ArrayAdapter<CharSequence>(StorkFishPlusActivity.this,android.R.layout.simple_spinner_item, finalMidS);
                            stock.setAdapter(adapter);
                            stock.setOnItemSelectedListener(new StorkFishPlusActivity.OnItemSelectedListenerImpl());
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
            String[] mFactory=getmFactory();
            if (city.equals("新添加到库存")) {
                factory.setText("");
                inventoryUnit.setText("");
                name.setText("");
                inventoryUnit.setEnabled(true);
                name.setEnabled(true);
                factory.setEnabled(true);
                note.setEnabled(true);
            }else{
                for (int i = 1; i < BS.length; i++) {
                    if(city.equals(BS[i])){
                        factory.setText(mFactory[i-1]);
                        inventoryUnit.setText(Unit[i-1]);
                        name.setText(city);
                        inventoryUnit.setEnabled(false);
                        name.setEnabled(false);
                        factory.setEnabled(false);
                        note.setEnabled(false);
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
                        paramsMap.put("note", String.valueOf(note.getText()));
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
                                        Toast.makeText(StorkFishPlusActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                                        if (jsonObject1.getString("msg").equals("添加成功")) {
                                            StorkFishPlusActivity.this.finish();
                                        }
                                        Looper.loop();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }).start();
                            }
                        });
                    }else{
                        for (int i = 1; i < BS.length; i++) {
                            if(city.equals(BS[i])){
                                HashMap<String, String> paramsMap = new HashMap<>();
                                paramsMap.put("inventory", String.valueOf(inventory.getText()));
                                paramsMap.put("id", String.valueOf(BC[i-1]));
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
                                                Toast.makeText(StorkFishPlusActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                                                if (jsonObject1.getString("msg").equals("更新成功")) {
                                                    StorkFishPlusActivity.this.finish();
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
        return R.layout.activity_plusstork;
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
}
