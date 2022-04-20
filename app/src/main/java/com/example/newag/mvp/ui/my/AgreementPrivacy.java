package com.example.newag.mvp.ui.my;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newag.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AgreementPrivacy extends AppCompatActivity {
    @OnClick(R.id.back)
    void Click1(){
        AgreementPrivacy.this.finish();
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement_privacy);
        ButterKnife.bind(this);
    }
}
