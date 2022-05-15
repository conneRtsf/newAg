package com.example.newag.mvp.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;
import com.example.newag.intercept.LoginIntercept;
import com.example.newag.mvp.ui.costs.PeopleCostActivity;
import com.example.newag.mvp.ui.login.LoginActivity;
import com.example.newag.mvp.ui.login.LoginActivity_ViewBinding;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    private TextView nourish;
    private TextView my;

    private Fragment nourishFragment;
    private Fragment myFragment;

    @Override
    protected void initBaseData() {

    }

    @Override
    protected void baseConfigView() {
        postSync();
        init();
        setFragment(0);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            default:
                break;
            case R.id.nourish:
                setFragment(0);
                break;
            case R.id.my:
                setFragment(1);
                break;
        }
    }

    private void init(){
        nourish = (TextView)findViewById(R.id.nourish);
        my = (TextView)findViewById(R.id.my);

        nourish.setOnClickListener(this);
        my.setOnClickListener(this);
    }

    private void setFragment(int index){
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        hideFragments(mTransaction);
        switch (index){
            case 0:
                nourish.setTextColor(getResources()
                        .getColor(R.color.press));
                nourish.setCompoundDrawablesWithIntrinsicBounds(0,
                        R.drawable.farm_press,0,0);
                if(nourishFragment == null){
                    nourishFragment = new NourishFragment();
                    mTransaction.add(R.id.container, nourishFragment,
                            "clothes_fragment");
                }else {
                    mTransaction.show(nourishFragment);
                }
                break;
            case 1:
                my.setTextColor(getResources()
                        .getColor(R.color.press));
                my.setCompoundDrawablesWithIntrinsicBounds(0,
                        R.drawable.my_press,0,0);
                if(myFragment== null){
                    myFragment= new MyFragment();
                    mTransaction.add(R.id.container, myFragment,
                            "hotel_fragment");
                }else {
                    mTransaction.show(myFragment);
                }
                break;
        }
        mTransaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction){
        if(nourishFragment != null){
            transaction.hide(nourishFragment);
            nourish.setTextColor(getResources()
                    .getColor(R.color.grey));
            nourish.setCompoundDrawablesWithIntrinsicBounds(0,
                    R.drawable.farm,0,0);
        }
        if(myFragment!= null){
            transaction.hide(myFragment);
            my.setTextColor(getResources()
                    .getColor(R.color.grey));
            my.setCompoundDrawablesWithIntrinsicBounds(0,
                    R.drawable.my,0,0);
        }
    }
    public void postSync() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept())
                .build();
        Request request = new Request.Builder()
                .get()
                .url("http://124.222.111.61:9000/daily/ponds/query")
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call call = httpClient.newCall(request);
                    Response response = call.execute();
                    assert response.body() != null;
                    String responseData=response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    String json=jsonObject.getString("msg");
                    Log.e("run: ", json);
                    if(!json.equals("查询成功")){
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, json,Toast.LENGTH_SHORT).show();
                            }
                        });
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        finish();
                        startActivity(intent);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}