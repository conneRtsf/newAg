package com.example.newag.mvp.ui.my;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AgreementPrivacyActivity extends BaseActivity {
    @OnClick(R.id.back)
    void Click1(){
        AgreementPrivacyActivity.this.finish();
    }
    @Override
    protected void initBaseData() {

    }

    @Override
    protected void baseConfigView() {

    }

    @Override
    protected int layoutId() {
        return R.layout.activity_agreement_privacy;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }
}
