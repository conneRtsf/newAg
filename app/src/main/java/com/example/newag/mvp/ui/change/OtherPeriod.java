package com.example.newag.mvp.ui.change;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.newag.R;
import com.example.newag.mvp.model.bean.AllText;

public class OtherPeriod extends AppCompatActivity {
    AllText allText;
    int position;
    private Spinner spCity = null;
    private ArrayAdapter<CharSequence> adapterCity = null;
    private static final String[] fish_info={"鱼池1号","鱼池2号","鱼池10号","鱼池666号"};
    private static final String[] vegetable_info={"菜地1号","菜地2号","菜地10号","菜地666号"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_plusperiod);
        EditText editText=findViewById(R.id.et_id);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        allText=(AllText) bundle.getSerializable("data");
        editText.setText(allText.getName());
        position=(int)bundle.getSerializable("position");
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
        Button button=findViewById(R.id.commit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent();
                Bundle bundle1=new Bundle();
                allText.setName(editText.getText().toString());
                bundle1.putSerializable("data",allText);
                bundle1.putSerializable("position",position);
                intent1.putExtras(bundle1);
                setResult(2,intent1);
                finish();
            }
        });
    }
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