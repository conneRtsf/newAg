package com.example.newag.mvp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.newag.R;
import com.example.newag.mvp.ui.my.AgreementPrivacy;
import com.example.newag.mvp.ui.my.ChangeMy;
import com.example.newag.mvp.ui.my.FarmChangeName;
import com.example.newag.mvp.ui.my.SafeMe;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MyFragment extends Fragment {
    private Unbinder unbinder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    @OnClick(R.id.changeMy)
    void onClick1(View view) {
        Intent intent = new Intent(getActivity(), ChangeMy.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt1)
    void onClick2(View view) {
        Intent intent = new Intent(getActivity(), SafeMe.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt2)
    void onClick3(View view) {
        Intent intent = new Intent(getActivity(), FarmChangeName.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt4)
    void onClick4(View view) {
        Intent intent = new Intent(getActivity(), AgreementPrivacy.class);
        startActivity(intent);
    }
}