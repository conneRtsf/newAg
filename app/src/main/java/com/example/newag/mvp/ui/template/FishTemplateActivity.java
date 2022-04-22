package com.example.newag.mvp.ui.template;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;
import com.example.newag.mvp.adapter.AllTextMasterAdapter;
import com.example.newag.mvp.model.bean.AllText;
import com.example.newag.mvp.model.bean.AllTextMaster;
import com.example.newag.mvp.ui.plus.TemplatePlus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FishTemplateActivity extends BaseActivity {
    @OnClick(R.id.tb1)
    void onClick(View view) {
        root.openDrawer(Gravity.LEFT);
    }
    @OnClick(R.id.ce1)
    void onClick1(View view) {
        Intent intent = new Intent();
        intent.setClass(FishTemplateActivity.this, DrugTemplateActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.ce2)
    void onClick2(View view) {
        Intent intent = new Intent();
        intent.setClass(FishTemplateActivity.this, FeedTemplateActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.ce3)
    void onClick3(View view) {
        Intent intent = new Intent();
        intent.setClass(FishTemplateActivity.this, FishTemplateActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.ce4)
    void onClick4(View view) {
        Intent intent = new Intent();
        intent.setClass(FishTemplateActivity.this, VegetableTemplateActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.ce5)
    void onClick5(View view) {
        Intent intent = new Intent();
        intent.setClass(FishTemplateActivity.this, OtherTemplateActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.plus)
    void onClick11(View view) {
        Intent intent = new Intent();
        intent.setClass(FishTemplateActivity.this, TemplatePlus.class);
        startActivity(intent);
    }
    @BindView(R.id.root)
    DrawerLayout root;
    @BindView(R.id.left)
    Button Button;
    @BindView(R.id.view_one)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.content)
    View contentView;
    private final List<AllText> allTextList11=new ArrayList<>();
    private final List<AllText> allTextList22=new ArrayList<>();
    private final List<AllText> allTextList1=new ArrayList<>();
    private final List<AllText> allTextList2=new ArrayList<>();//定义一个新的arraylist,数据类型为自定义的AllText，基础数据
    private final List<AllTextMaster> data_1=new ArrayList<>();//定义数据1,原始数据
    private final List<AllTextMaster> data_2=new ArrayList<>();//定义数据2,模拟修改后的数据
    private PopupWindow popupWindow;//定义一个新的popupWindow 主
    private PopupWindow newPopWindow;//副
    private AllTextMasterAdapter adapter;

    @Override
    protected void initBaseData() {

    }

    @Override
    protected void baseConfigView() {
        initText();//为原始数据添加数据
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);//设置布局管理器，cv工程
        recyclerView.setLayoutManager(linearLayoutManager);//为recycleview添加布局管理器，cv
         /*adapter=new AllTextAdapter(allTextList);//定义一个新的自定义适配器（AllTextAdapter），并且把数据传进去
        recyclerView.setAdapter(adapter);//为recycleview传入定义好的适配器，并展示*/
        adapter=new AllTextMasterAdapter(this,data_1);//定义一个新的大适配器（AllTextMasterAdapter），并且把数据传进去
        recyclerView.setAdapter(adapter);//设置适配器
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();//展示popwindow的方法
            }
        });
        //
        refreshLayout.setColorSchemeResources(R.color.blue,R.color.blue);//设置下拉刷新主题（最多支持三种颜色变换，这里两种）
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setNewData(data_2);//模拟数据变换,以后这里就写从后端获取数据的逻辑
                refreshLayout.setRefreshing(false);
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
    protected int layoutId() {
        return R.layout.activity_templatefish;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }

    private void showPopWindow() {
        //定义一个view，其中包含popwindow的布局文件
        View view1= LayoutInflater.from(FishTemplateActivity.this).inflate(R.layout.footer_batch,null);
        popupWindow =new PopupWindow(view1, RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT,true);//设置popwindow的属性（布局，x，y，true）
        TextView make_text=(TextView)view1.findViewById(R.id.make_text);
        TextView back_test=(TextView)view1.findViewById(R.id.back_test);
        make_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.setCheckbox(true);
                adapter.notifyDataSetChanged();
                popupWindow.dismiss();//销毁popwindow
                View rootView= LayoutInflater.from(FishTemplateActivity.this).inflate(R.layout.activity_templatefish,null);
                newPopWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
            }
        });
        back_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        //定义一个view，其中包含main4的布局文件
        View rootView=LayoutInflater.from(FishTemplateActivity.this).inflate(R.layout.activity_templatefish,null);
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);//展示自定义的popwindow，（放哪个布局里，放布局里的位置，x，y），cv工程
        View view2=LayoutInflater.from(FishTemplateActivity.this).inflate(R.layout.ppw_delete,null);
        newPopWindow=new PopupWindow(view2,RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT,false);
        Button button_delete=(Button) view2.findViewById(R.id.delete);
        button_delete.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                adapter.setCheckbox(false);
                adapter.notifyDataSetChanged();
                newPopWindow.dismiss();
            }
        });
    }
    //添加数据相关方法
    private void initText() {
        AllText one=new AllText("one");
        allTextList1.add(one);
        AllText two=new AllText("two");
        allTextList1.add(two);
        AllText three=new AllText("three");
        allTextList1.add(three);
        AllTextMaster add1=new AllTextMaster("0",allTextList1);
        data_1.add(add1);
        allTextList2.add(one);
        allTextList2.add(two);
        AllTextMaster add2=new AllTextMaster("1",allTextList2);
        data_1.add(add2);
        //
        AllText one1=new AllText("1.鱼类投入1 a厂商");
        allTextList11.add(one1);
        AllText two2=new AllText("2.鱼类投入2 b厂商");
        allTextList11.add(two2);
        AllText three3=new AllText("3.鱼类投入3 c厂商");
        allTextList11.add(three3);
        AllTextMaster add11=new AllTextMaster("鱼类模板",allTextList11);
        AllText one2=new AllText("4.鱼类投入4 d厂商");
        allTextList11.add(one2);
        data_2.add(add11);
    }
}
