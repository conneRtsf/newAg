package com.example.newag.mvp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.newag.R;
import com.example.newag.mvp.ui.Program.program_add;
import com.example.newag.mvp.ui.accounting.Sales_accounting;
import com.example.newag.mvp.ui.costs.people_cost;
import com.example.newag.mvp.ui.input_period.fish_period;
import com.example.newag.mvp.ui.inspection.inspection;
import com.example.newag.mvp.ui.reduce.reduce_add;
import com.example.newag.mvp.ui.template.drug_template;

public class nourishFragment extends Fragment {
    Button bt1;
    Button bt7;
    Button bt3;
    Button bt4;
    Button bt5;
    Button bt6;
    Button bt8;
    Button bt9;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nourish, container, false);
        bt1=view.findViewById(R.id.bt1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), reduce_add.class);
                startActivity(intent);
            }
        });
        bt3=view.findViewById(R.id.bt3);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Sales_accounting.class);
                startActivity(intent);
            }
        });
        bt4=view.findViewById(R.id.bt4);
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), drug_template.class);
                startActivity(intent);
            }
        });
        bt5=view.findViewById(R.id.bt5);
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), fish_period.class);
                startActivity(intent);
            }
        });
        bt6=view.findViewById(R.id.bt6);
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), program_add.class);
                startActivity(intent);
            }
        });
        bt7=view.findViewById(R.id.bt7);
        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), people_cost.class);
                startActivity(intent);
            }
        });
//        bt8=view.findViewById(R.id.bt8);
//        bt8.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), MainActivity4.class);
//                startActivity(intent);
//            }
//        });
        bt9=view.findViewById(R.id.bt9);
        bt9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), inspection.class);
                startActivity(intent);
            }
        });
        return view;
    }
}