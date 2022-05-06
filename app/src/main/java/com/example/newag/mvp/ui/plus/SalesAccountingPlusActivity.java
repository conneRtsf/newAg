package com.example.newag.mvp.ui.plus;

import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;
import com.example.newag.intercept.LoginIntercept;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SalesAccountingPlusActivity extends BaseActivity {
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.sale)
    EditText sale;
    @BindView(R.id.price)
    EditText price;
    @BindView(R.id.saleAmount)
    EditText saleAmount;
    @BindView(R.id.note)
    EditText note;
    @BindView(R.id.commit)
    Button commit;
    @Override
    protected void initBaseData() {
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postSync();
            }
        });
    }

    @Override
    protected void baseConfigView() {

    }

    @Override
    protected int layoutId() {
        return R.layout.activity_salesaccountingplus;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }
    public void postSync() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("name", String.valueOf(name.getText()));
        paramsMap.put("price", String.valueOf(price.getText()));
        paramsMap.put("saleAmount", String.valueOf(saleAmount.getText()));
        paramsMap.put("sales", String.valueOf(sale.getText()));
        paramsMap.put("note", String.valueOf(note.getText()));
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
                .url("http://124.222.111.61:9000/daily/sales/addSales")
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
                        Toast.makeText(SalesAccountingPlusActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        if(jsonObject1.getString("msg").equals("添加成功")){
                            SalesAccountingPlusActivity.this.finish();
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
