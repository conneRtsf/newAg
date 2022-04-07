package com.example.newag.mvp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.newag.R;

public class vegetable_cost extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.vegetable_cost);
            Button Register = findViewById(R.id.button2);
            Register.setOnClickListener(view -> {
                Intent intent = new Intent();
                intent.setClass(vegetable_cost.this, other_cost.class);
                startActivity(intent);
            });
        }
    }

