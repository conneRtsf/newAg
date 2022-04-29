package com.example.newag.mvp.ui.change;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.newag.R;
import com.example.newag.mvp.model.bean.AllText;

public class DrugTemplate extends AppCompatActivity {
    AllText allText;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_plustemplate);
        EditText editText=findViewById(R.id.et_1);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        allText=(AllText) bundle.getSerializable("data");
        editText.setText(allText.getName());
        position=(int)bundle.getSerializable("position");
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
}