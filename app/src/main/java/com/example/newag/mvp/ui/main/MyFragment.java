package com.example.newag.mvp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.newag.R;
import com.example.newag.base.BaseFragment;
import com.example.newag.mvp.ui.login.LoginActivity;
import com.example.newag.mvp.ui.login.LoginActivity_ViewBinding;
import com.example.newag.mvp.ui.my.AgreementPrivacyActivity;
import com.example.newag.mvp.ui.my.ChangeMyActivity;
import com.example.newag.mvp.ui.my.FarmChangeNameActivity;
import com.example.newag.mvp.ui.my.SafeMeActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MyFragment extends BaseFragment {
    private Unbinder unbinder;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    @OnClick(R.id.changeMy)
    void onClick1(View view) {
        Intent intent = new Intent(getActivity(), ChangeMyActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt1)
    void onClick2(View view) {
        Intent intent = new Intent(getActivity(), SafeMeActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt2)
    void onClick3(View view) {
        Intent intent = new Intent(getActivity(), FarmChangeNameActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt4)
    void onClick4(View view) {
        Intent intent = new Intent(getActivity(), AgreementPrivacyActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt5)
    void onClick5(View view) {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        System.exit(0);
    }
}