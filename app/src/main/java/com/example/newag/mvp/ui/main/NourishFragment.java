package com.example.newag.mvp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.newag.R;
import com.example.newag.mvp.ui.accounting.SalesAccountingActivity;
import com.example.newag.mvp.ui.costs.PeopleCostActivity;
import com.example.newag.mvp.ui.inputperiod.FishPeriodActivity;
import com.example.newag.mvp.ui.inspection.InspectionActivity;
import com.example.newag.mvp.ui.program.ProgramAddActivity;
import com.example.newag.mvp.ui.reduce.ReduceAddActivity;
import com.example.newag.mvp.ui.template.DrugTemplateActivity;

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
        Intent intent = new Intent(getActivity(), PeopleCostActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt3)
    void onClick3(View view) {
        Intent intent = new Intent(getActivity(), SalesAccountingActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt4)
    void onClick4(View view) {
        Intent intent = new Intent(getActivity(), DrugTemplateActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt5)
    void onClick5(View view) {
        Intent intent = new Intent(getActivity(), FishPeriodActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt6)
    void onClick6(View view) {
        Intent intent = new Intent(getActivity(), ProgramAddActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt9)
    void onClick9(View view) {
        Intent intent = new Intent(getActivity(), InspectionActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt1)
    void onClick(View view) {
        Intent intent = new Intent(getActivity(), ReduceAddActivity.class);
        startActivity(intent);
    }
}