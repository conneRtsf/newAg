package com.example.newag.mvp.ui.plus;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newag.R;

public class period_plus extends AppCompatActivity {
    private Spinner spCity = null;
    private ArrayAdapter<CharSequence> adapterCity = null;
    private static final String[] fish_info={"鱼池1号","鱼池2号","鱼池10号","鱼池666号"};
    private static final String[] vegetable_info={"菜地1号","菜地2号","菜地10号","菜地666号"};
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.period_plus);
        //动态实现的下拉框，数据在程序中获得，实际项目可能来自数据库等
        this.spCity = (Spinner) super.findViewById(R.id.spinner_name);
        this.adapterCity = new ArrayAdapter<CharSequence>(this,
                    android.R.layout.simple_spinner_dropdown_item, fish_info);
        this.spCity.setAdapter(adapterCity);
        this.spCity.setOnItemSelectedListener(new OnItemSelectedListenerImpl());

        this.spCity = (Spinner) super.findViewById(R.id.spinner_produce);
        this.adapterCity = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_dropdown_item, vegetable_info);
        this.spCity.setAdapter(adapterCity);
        this.spCity.setOnItemSelectedListener(new OnItemSelectedListenerImpl());
    }
        //下拉框选择事件
    private static class OnItemSelectedListenerImpl implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                  int position, long id) {
            String city = parent.getItemAtPosition(position).toString();
            TextView v1 = (TextView)view;
            v1.setTextColor(Color.BLACK);
            v1.setGravity(android.view.Gravity.CENTER);
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
        }
    }
}
