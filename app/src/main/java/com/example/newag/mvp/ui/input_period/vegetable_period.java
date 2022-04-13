package com.example.newag.mvp.ui.input_period;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.newag.R;
import com.example.newag.mvp.ui.plus.period_plus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class vegetable_period extends AppCompatActivity {
    Button ce1;
    Button ce2;
    Button ce3;
    Button plus;
    Button tb1;
    Calendar calendar= Calendar.getInstance(Locale.CHINA);
    public Button btnDate;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.period_vegetable);
        ce1=findViewById(R.id.ce1);
        ce2=findViewById(R.id.ce2);
        btnDate=findViewById(R.id.btn_Date);
        DrawerLayout root = findViewById(R.id.root);
        ce3=findViewById(R.id.ce3);
        plus=findViewById(R.id.plus);
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy年\nM月 ");
        Date curDate =  new Date(System.currentTimeMillis());
        String   str   =   formatter.format(curDate);
        btnDate.setText(str);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(vegetable_period.this,  2, btnDate, calendar);;
            }
        });
        ce1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(vegetable_period.this, fish_period.class);
                startActivity(intent);
                finish();
            }
        });

        ce3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(vegetable_period.this, other_period.class);
                startActivity(intent);
                finish();
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(vegetable_period.this, period_plus.class);
                startActivity(intent);
            }
        });
        tb1=findViewById(R.id.tb1);
        final View contentView = findViewById(R.id.content);
        tb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                root.openDrawer(Gravity.LEFT);
            }
        });
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, root, android.R.string.yes, android.R.string.cancel) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                contentView.setTranslationX(slideX);
            }
        };
        root.addDrawerListener(actionBarDrawerToggle);
    }
    public static void showDatePickerDialog(Activity activity, int themeResId, Button bt, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器(How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作

                bt.setText(year + "年\n" + (monthOfYear + 1) + "月" );
            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
