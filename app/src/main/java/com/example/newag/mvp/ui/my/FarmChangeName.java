package com.example.newag.mvp.ui.my;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newag.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FarmChangeName extends AppCompatActivity {
    @OnClick(R.id.back)
    void Click1(){
        FarmChangeName.this.finish();
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmnamechange);
        ButterKnife.bind(this);
    }
}
