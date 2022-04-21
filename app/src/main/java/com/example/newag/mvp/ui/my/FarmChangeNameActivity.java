package com.example.newag.mvp.ui.my;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newag.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FarmChangeNameActivity extends AppCompatActivity {
    @OnClick(R.id.back)
    void Click1(){
        FarmChangeNameActivity.this.finish();
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmnamechange);
        ButterKnife.bind(this);
    }
}
