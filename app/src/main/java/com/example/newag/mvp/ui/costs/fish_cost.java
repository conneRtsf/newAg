package com.example.newag.mvp.ui.costs;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.newag.R;
import com.example.newag.mvp.ui.main.nourishFragment;

import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;

public class fish_cost extends AppCompatActivity {
    Button tb1;
    Button ce1;
    Button ce2;
    Button ce3;
    Button ce4;
    Button ce5;
    Button ce6;
    Button ce7;
    public Button btnDate;
    @Subscribe
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fish_cost);
        DrawerLayout root = findViewById(R.id.root);
        ce1=findViewById(R.id.ce1);
        ce2=findViewById(R.id.ce2);
        ce3=findViewById(R.id.ce3);
        ce4=findViewById(R.id.ce4);
        ce5=findViewById(R.id.ce5);
        ce6=findViewById(R.id.ce6);
        ce7=findViewById(R.id.ce7);
        ce1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(fish_cost.this, Input_class.class);
                startActivity(intent);
                finish();
            }
        });

        ce2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(fish_cost.this, feed_cost.class);
                startActivity(intent);
                finish();
            }
        });

        ce3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(fish_cost.this, energy_cost.class);
                startActivity(intent);
                finish();
            }
        });

        ce5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(fish_cost.this, vegetable_cost.class);
                startActivity(intent);
                finish();
            }
        });

        ce6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(fish_cost.this, other_cost.class);
                startActivity(intent);
                finish();
            }
        });

        ce7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(fish_cost.this, all_cost.class);
                startActivity(intent);
                finish();
            }
        });
        btnDate=findViewById(R.id.btn_Date);
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy年\nM月 ");
        Date curDate =  new Date(System.currentTimeMillis());
        String   str   =   formatter.format(curDate);
        btnDate.setText(str);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(fish_cost.this, nourishFragment.class);
        startActivity(intent);
        finish();
    }
}
