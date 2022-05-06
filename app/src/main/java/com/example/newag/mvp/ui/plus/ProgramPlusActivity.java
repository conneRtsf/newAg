package com.example.newag.mvp.ui.plus;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProgramPlusActivity extends BaseActivity {
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.commit)
    Button commit;
    @BindView(R.id.period)
    EditText period;
    @BindView(R.id.day)
    EditText day;
    @BindView(R.id.raiseNum)
    EditText raiseNum;
    @BindView(R.id.raiseTime)
    EditText raiseTime;
    @BindView(R.id.raiseId)
    Spinner raiseId;
    @Override
    protected void initBaseData() {

    }
    public int[] midIn;
    public String[] midasa;
    public String[] db;

    public String[] getDb() {
        return db;
    }

    public String[] getMidasa() {
        return midasa;
    }

    public void setMidasa(String[] midasa) {
        this.midasa = midasa;
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
                    String[] newone2 = new String[0];
                    int t=0;
                    int[] midI=new int[1000];
                    String[] midS=new String[0];
                    String[] midSw=new String[0];
                    String type2 = null;
                    for (Map.Entry<String, JsonArray> entry : map.entrySet()) {
                        JsonArray mapValue = entry.getValue();
                        JSONArray pond = new JSONArray(String.valueOf(mapValue));
                        Log.e("pond1: ", String.valueOf(pond));
                        newone=new String[pond.length()];
                        newone2=new String[pond.length()];
                        for (int i = 0; i < pond.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) pond.get(i);
                            int id = jsonObject1.getInt("id");
                            String type = jsonObject1.getString("type");
                            String inventoryUnit = jsonObject1.getString("inventoryUnit");
                            type2=type;
                            if (type.equals("feed")){
                                midI[t] = id;
                                newone2[i]=inventoryUnit;
                                t++;
                                String name = jsonObject1.getString("name");
                                newone[i] = name;
                            }
                        }
                        if (type2.equals("feed")) {
                            midSw=insert(midSw,newone2);
                            midS = insert(midS, newone);
                        }
                    }
                    if (midS.equals(null)){
                        midS=new String[1];
                        midS[0]="空";
                        midI[0]=0;
                    }
                    setMidasa(midSw);
                    setMidIn(midI);
                    setDb(midS);
                    String[] finalMidS = midS;
                    ProgramPlusActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new ArrayAdapter<CharSequence>(ProgramPlusActivity.this,android.R.layout.simple_spinner_item, finalMidS);
                            raiseId.setAdapter(adapter);
                            raiseId.setOnItemSelectedListener(new OnItemSelectedListenerImpl());
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
            Log.e("onItemSelected: ", String.valueOf(id));
            commit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String[] BS=getDb();
                    String[] BSw=getMidasa();
                    int[] BC=getMidIn();
                    for (int i = 0; i < BS.length; i++) {
                        System.out.println(BS[i]+BC[i]);
                        if (city.equals(BS[i])) {
                            postSync(BC[i],BS[i],BSw[i]);
                            break;
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
        return R.layout.activity_plusprogram;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }
    public void postSync(int raiseId,String raiseName,String unit) {
        HashMap<String, String> paramsMap = new HashMap<>();
        String id1=String.valueOf(raiseId);
        paramsMap.put("raiseId", id1);
        paramsMap.put("name", String.valueOf(name.getText()));
        paramsMap.put("period", String.valueOf(period.getText()));
        paramsMap.put("day", String.valueOf(day.getText()));
        paramsMap.put("raiseName", raiseName);
        paramsMap.put("numUnit", unit);
        paramsMap.put("raiseNum", String.valueOf(raiseNum.getText()));
        paramsMap.put("raiseTime", String.valueOf(raiseTime.getText()));
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
                .url("http://124.222.111.61:9000/daily/plan/addPlan")
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
                        Toast.makeText(ProgramPlusActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        if(jsonObject1.getString("msg").equals("添加成功")){
                           ProgramPlusActivity.this.finish();
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
        System.arraycopy(arr, 0, tmp, 0, size);
        // 在末尾添加上需要追加的数据
        if (newSize - size >= 0) System.arraycopy(str, size - size, tmp, size, newSize - size);
        return tmp; // 返回拼接完成的字符串数组
    }
}
