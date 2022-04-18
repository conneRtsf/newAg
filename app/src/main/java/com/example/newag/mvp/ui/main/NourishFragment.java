package com.example.newag.mvp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.newag.R;
import com.example.newag.mvp.ui.accounting.SalesAccounting;
import com.example.newag.mvp.ui.costs.PeopleCost;
import com.example.newag.mvp.ui.inputperiod.FishPeriod;
import com.example.newag.mvp.ui.inspection.Inspection;
import com.example.newag.mvp.ui.program.ProgramAdd;
import com.example.newag.mvp.ui.reduce.ReduceAdd;
import com.example.newag.mvp.ui.template.DrugTemplate;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NourishFragment extends Fragment {
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nourish, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    @OnClick(R.id.bt7)
    void onClick7(View view) {
        Intent intent = new Intent(getActivity(), PeopleCost.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt3)
    void onClick3(View view) {
        Intent intent = new Intent(getActivity(), SalesAccounting.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt4)
    void onClick4(View view) {
        Intent intent = new Intent(getActivity(), DrugTemplate.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt5)
    void onClick5(View view) {
        Intent intent = new Intent(getActivity(), FishPeriod.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt6)
    void onClick6(View view) {
        Intent intent = new Intent(getActivity(), ProgramAdd.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt9)
    void onClick9(View view) {
        Intent intent = new Intent(getActivity(), Inspection.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt1)
    void onClick(View view) {
        Intent intent = new Intent(getActivity(), ReduceAdd.class);
        startActivity(intent);
    }
}