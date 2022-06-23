package com.example.newag.mvp.ui.login;

import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newag.R;
import com.example.newag.mvp.model.api.HttpClientUtils;
import com.example.newag.mvp.model.api.service.LoginApiService;
import com.example.newag.mvp.model.api.service.RegisterApiService;
import com.example.newag.mvp.model.bean.LoginTranslation;
import com.example.newag.mvp.model.bean.RegisterTranslation;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        Button register = findViewById(R.id.btn1);
        register.setOnClickListener(this::postSync);
    }

    public void postSync(View view) {

        EditText editText1 = findViewById(R.id.enter1);
        EditText editText2 = findViewById(R.id.enter2);
        EditText editText3 = findViewById(R.id.enter3);
        android.util.Log.e("postSync: ", String.valueOf(editText1.getText()));
        android.util.Log.e("postSync: ", String.valueOf(editText2.getText()));
        android.util.Log.e("postSync: ", String.valueOf(editText3.getText()));

        final String username = editText1.getText().toString();
        final String password = editText2.getText().toString();
        final String icode = editText3.getText().toString();

        if(detailsCheck(username, password, icode)) {

        } else {
            Toast.makeText(RegisterActivity.this,"账号、密码或邀请码为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = HttpClientUtils.getRetrofitWithGsonAdapter();

        RegisterApiService postRequest = retrofit.create(RegisterApiService.class);

        Call<RegisterTranslation> call = postRequest.register(username, password, icode,"1234");

        call.enqueue(new Callback<RegisterTranslation>() {

            @Override
            public void onResponse(Call<RegisterTranslation> call, Response<RegisterTranslation> response) {

                RegisterTranslation registerTranslation = response.body();
                Integer code = registerTranslation.getCode();
                String msg = registerTranslation.getMsg();

                android.util.Log.e("ServerRet: ", code.toString()+" "+msg);

                Object body = response.body();
                if (body == null) return;

                RegisterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(RegisterActivity.this);
                            builder1.setMessage(msg);
                            builder1. setPositiveButton("确定", (dialog, which) -> {
                            });
                            AlertDialog alert = builder1.create();
                            alert.show();
                            if(msg.equals("注册成功")){
                                RegisterActivity.this.finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

//                Toast.makeText(RegisterActivity.this, "success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<RegisterTranslation> call, Throwable throwable) {
                Log.e(TAG, "info：" + throwable.getMessage() + "," + throwable.toString());
                Toast.makeText(RegisterActivity.this, "error", Toast.LENGTH_SHORT).show();
            }

        });

    }

    public Boolean detailsCheck(String uname, String pwd, String icode) {

        if(uname.isEmpty()||pwd.isEmpty()||icode.isEmpty()) {
            return false;
        } else {
            return true;
        }

    }

}
