package com.example.newag.mvp.ui.template;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.newag.R;
import com.example.newag.mvp.ui.plus.template_plus;

public class vegetable_template extends AppCompatActivity {
    Button ce1;
    Button ce2;
    Button ce3;
    Button ce4;
    Button ce5;
    Button plus;
    Button tb1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_vegetable);
        ce1=findViewById(R.id.ce1);
        ce2=findViewById(R.id.ce2);
        ce3=findViewById(R.id.ce3);
        ce4=findViewById(R.id.ce4);
        ce5=findViewById(R.id.ce5);
        plus=findViewById(R.id.plus);
        DrawerLayout root = findViewById(R.id.root);
        ce1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(vegetable_template.this, drug_template.class);
                startActivity(intent);
                finish();
            }
        });
        ce2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(vegetable_template.this, feed_template.class);
                startActivity(intent);
                finish();
            }
        });

        ce3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(vegetable_template.this, fish_template.class);
                startActivity(intent);
                finish();
            }
        });
        ce5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(vegetable_template.this, other_template.class);
                startActivity(intent);
                finish();
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(vegetable_template.this, template_plus.class);
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
}
