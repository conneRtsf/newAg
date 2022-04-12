package com.example.newag.mvp.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newag.R;
import com.example.newag.mvp.adapter.AllTextAdapter;
import com.example.newag.mvp.adapter.AllTextMasterAdapter;
import com.example.newag.mvp.model.bean.AllText;
import com.example.newag.mvp.model.bean.AllTextMaster;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于两个类，两个适配器，大类嵌套小类，大适配器嵌套小适配器
 * 本质上是两个recycleview，其中小recycleview带边框和标题
 * 用了两种不同适配器写法
 */
public class MainActivity4 extends Activity implements View.OnClickListener {
    private int flag=0;
    private AllTextAdapter adapter;
    /*private JSONArray first=new JSONArray();//模拟初始数据
    private JSONArray basic=new JSONArray();//模拟基础数据*/
    private List<AllText> allTextList1=new ArrayList<>();
    private List<AllText> allTextList2=new ArrayList<>();//定义一个新的arraylist,数据类型为自定义的AllText，基础数据
    private List<AllTextMaster> data_1=new ArrayList<>();//定义数据1,原始数据
    private List<AllTextMaster> data_2=new ArrayList<>();//定义数据2,模拟修改后的数据
    private PopupWindow popupWindow;//定义一个新的popupWindow
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        initText();//为原始数据添加数据
        RecyclerView recyclerView=findViewById(R.id.view_one);//找到布局中的recycleview控件
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);//设置布局管理器，cv工程
        recyclerView.setLayoutManager(linearLayoutManager);//为recycleview添加布局管理器，cv
         /*adapter=new AllTextAdapter(allTextList);//定义一个新的自定义适配器（AllTextAdapter），并且把数据传进去
        recyclerView.setAdapter(adapter);//为recycleview传入定义好的适配器，并展示*/
        AllTextMasterAdapter adapter=new AllTextMasterAdapter(this,data_1);//定义一个新的大适配器（AllTextMasterAdapter），并且把数据传进去
        recyclerView.setAdapter(adapter);//设置适配器
        ImageButton imageButton=findViewById(R.id.back_1);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();//展示popwindow的方法
                if (flag==1){
                    adapter.setNewData(data_2);//模拟数据变换
                }
            }//展示popwindow
        });
    }

    private void showPopWindow() {
        //定义一个view，其中包含popwindow的布局文件
        View view= LayoutInflater.from(MainActivity4.this).inflate(R.layout.test_popupwindow,null);
        popupWindow =new PopupWindow(view, RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT,true);//设置popwindow的属性（布局，x，y，true）
        TextView make_text=(TextView)view.findViewById(R.id.make_text);
        TextView back_test=(TextView)view.findViewById(R.id.back_test);
        make_text.setOnClickListener(this);
        back_test.setOnClickListener(this);
        //定义一个view，其中包含main4的布局文件
        View rootView=LayoutInflater.from(MainActivity4.this).inflate(R.layout.activity_main4,null);
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);//展示自定义的popwindow，（放哪个布局里，放布局里的位置，x，y），cv工程
    }
//添加数据相关方法
    private void initText() {
            AllText one=new AllText("one",R.drawable.ic_baseline_test_1);
            allTextList1.add(one);
            AllText two=new AllText("two",R.drawable.ic_baseline_text_2);
            allTextList1.add(two);
            AllText three=new AllText("three",R.drawable.ic_baseline_test_3);
            allTextList1.add(three);
            AllTextMaster add1=new AllTextMaster(0,allTextList1);
            data_1.add(add1);
            allTextList2.add(one);
            allTextList2.add(two);
            AllTextMaster add2=new AllTextMaster(1,allTextList2);
            data_1.add(add2);
    }
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.make_text:{
                allTextList1.clear();
                AllText one=new AllText("gaile1",R.drawable.ic_baseline_test_1);
                allTextList1.add(one);
                AllText two=new AllText("gaile2",R.drawable.ic_baseline_text_2);
                allTextList1.add(two);
                AllText three=new AllText("gaile3",R.drawable.ic_baseline_test_3);
                allTextList1.add(three);
                AllTextMaster add1=new AllTextMaster(123,allTextList1);
                data_2.add(add1);
                allTextList2.clear();
                allTextList2.add(one);
                AllTextMaster add2=new AllTextMaster(123,allTextList2);
                data_2.add(add2);
                flag=1;
                popupWindow.dismiss();//销毁popwindow
            }
            break;
            case R.id.back_test:
                popupWindow.dismiss();
                break;
        }
    }
}