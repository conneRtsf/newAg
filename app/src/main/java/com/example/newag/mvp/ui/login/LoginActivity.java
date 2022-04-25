package com.example.newag.mvp.ui.login;

import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newag.R;
import com.example.newag.mvp.model.api.service.LoginApiService;
import com.example.newag.mvp.model.bean.LoginTranslation;
import com.example.newag.mvp.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.enter1)
    EditText editText1;
    @BindView(R.id.enter2)
    EditText editText2;
    @BindView(R.id.btn1)
    Button log;
    @BindView(R.id.btn2)
    Button Register;
    @OnClick(R.id.pass)
    void cl(){
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        ButterKnife.bind(this);

        log.setOnClickListener(this::postSync);
        Register.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

    }

    public void postSync(View view) {

        android.util.Log.e("postSync: ", String.valueOf(editText1.getText()));
        android.util.Log.e("postSync: ", String.valueOf(editText2.getText()));

        final String username = editText1.getText().toString();
        final String password = editText2.getText().toString();

        if(detailsCheck(username, password)) {

        } else {
            Toast.makeText(LoginActivity.this,"账号或密码为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ctos17.free.idcfengye.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginApiService postRequest = retrofit.create(LoginApiService.class);

        Call<LoginTranslation> call = postRequest.login(username, password);

        call.enqueue(new Callback<LoginTranslation>() {

            @Override
            public void onResponse(Call<LoginTranslation> call, Response<LoginTranslation> response) {

                LoginTranslation loginTranslation = response.body();
                Integer code = loginTranslation.getCode();
                String msg = loginTranslation.getMsg();

                android.util.Log.e("ServerRet: ", code.toString()+" "+msg);

                Object body = response.body();
                if (body == null) return;

                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                            builder1.setMessage(msg);
                            builder1. setPositiveButton("确定", (dialog, which) -> {
                            });
                            AlertDialog alert = builder1.create();
                            alert.show();
                            if(msg.equals("登录成功")){
                                Thread.sleep(1500);
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                LoginActivity.this.finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

//                Toast.makeText(LoginActivity.this, "success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<LoginTranslation> call, Throwable throwable) {
                Log.e(TAG, "info：" + throwable.getMessage() + "," + throwable.toString());
                Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
            }

        });

    }

    public Boolean detailsCheck(String uname, String pwd) {

        if(uname.isEmpty()||pwd.isEmpty()) {
            return false;
        } else {
            return true;
        }

    }

}
