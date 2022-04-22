package com.example.newag.mvp.ui.reduce;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

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
import com.example.newag.mvp.ui.plus.ReducePlus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReduceAddActivity extends BaseActivity implements View.OnClickListener{
    @OnClick(R.id.plus)
    void onClick11(View view) {
        Intent intent = new Intent();
        intent.setClass(ReduceAddActivity.this, ReducePlus.class);
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
    @BindView(R.id.btn_Date)
    Button btnDate;
    Calendar calendar= Calendar.getInstance(Locale.CHINA);
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
        SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年\nM月 ");
        Date curDate =  new Date(System.currentTimeMillis());
        String   str   =   formatter.format(curDate);
        btnDate.setText(str);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(ReduceAddActivity.this,  2, btnDate, calendar);;
            }
        });
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
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_reduceadd;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

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
    private void showPopWindow() {
        //定义一个view，其中包含popwindow的布局文件
        View view1= LayoutInflater.from(ReduceAddActivity.this).inflate(R.layout.footer_batch,null);
        popupWindow =new PopupWindow(view1, RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT,true);//设置popwindow的属性（布局，x，y，true）
        TextView make_text=(TextView)view1.findViewById(R.id.make_text);
        TextView back_test=(TextView)view1.findViewById(R.id.back_test);
        make_text.setOnClickListener(this);
        back_test.setOnClickListener(this);
        //定义一个view，其中包含main4的布局文件
        View rootView=LayoutInflater.from(ReduceAddActivity.this).inflate(R.layout.activity_reduceadd,null);
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);//展示自定义的popwindow，（放哪个布局里，放布局里的位置，x，y），cv工程
        View view2=LayoutInflater.from(ReduceAddActivity.this).inflate(R.layout.ppw_delete,null);
        newPopWindow=new PopupWindow(view2,RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT,false);
        Button button_delete=(Button) view2.findViewById(R.id.delete);
        button_delete.setOnClickListener(this);
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
        AllText one1=new AllText("菜地1\n50m^2/40m^2");
        allTextList11.add(one1);
        AllText two2=new AllText("菜地2\n60m^2/40m^2");
        allTextList11.add(two2);
        AllText three3=new AllText("菜地3\n40m^2/40m^2");
        allTextList11.add(three3);
        AllTextMaster add11=new AllTextMaster("4月10日",allTextList11);
        data_2.add(add11);
        allTextList22.clear();
        AllText one2=new AllText("鱼池1\n40m^3/40m^3");
        allTextList22.add(one2);
        AllTextMaster add22=new AllTextMaster("4月9日",allTextList22);
        data_2.add(add22);
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.make_text:{
                adapter.setCheckbox(true);
                adapter.notifyDataSetChanged();
                popupWindow.dismiss();//销毁popwindow
                View rootView= LayoutInflater.from(ReduceAddActivity.this).inflate(R.layout.activity_reduceadd,null);
                newPopWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
            }
            break;
            case R.id.btn_Date:{
                showDatePickerDialog(this,  2, btnDate, calendar);;
                break;
            }
            case R.id.back_test:
                popupWindow.dismiss();
                break;
            case R.id.delete:
                adapter.setCheckbox(false);
                adapter.notifyDataSetChanged();
                newPopWindow.dismiss();
        }
    }
    private void setDialog() {
        Dialog mCameraDialog = new Dialog(this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.activity_plusreduce, null);
        //初始化视图
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
//        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();

        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }
}


