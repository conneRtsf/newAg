package com.example.newag.mvp.ui.costs;

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

import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllCostActivity extends BaseActivity {
    @OnClick(R.id.tb1)
    void onClick(View view) {
        root.openDrawer(Gravity.LEFT);
    }
    @OnClick(R.id.ce1)
    void onClick1(View view) {
        Intent intent = new Intent();
        intent.setClass(AllCostActivity.this, PeopleCostActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.ce2)
    void onClick2(View view) {
        Intent intent = new Intent();
        intent.setClass(AllCostActivity.this, FeedCostActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.ce3)
    void onClick3(View view) {
        Intent intent = new Intent();
        intent.setClass(AllCostActivity.this, FeedCostActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.ce4)
    void onClick4(View view) {
        Intent intent = new Intent();
        intent.setClass(AllCostActivity.this, FishCostActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.ce5)
    void onClick5(View view) {
        Intent intent = new Intent();
        intent.setClass(AllCostActivity.this, VegetableCostActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.ce6)
    void onClick6(View view) {
        Intent intent = new Intent();
        intent.setClass(AllCostActivity.this, OtherCostActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.ce7)
    void onClick7(View view) {
        Intent intent = new Intent();
        intent.setClass(AllCostActivity.this, AllCostActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.plus)
    void onClick11(View view) {
        setDialog();
    }
    @BindView(R.id.left)
    Button Button;

    @BindView(R.id.btn_Date)
    Button btnDate;
    @BindView(R.id.root)
    DrawerLayout root;

    @BindView(R.id.view_one)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.content)
    View contentView;

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
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy年\nM月 ");
        Date curDate =  new Date(System.currentTimeMillis());
        String   str   =   formatter.format(curDate);
        btnDate.setText(str);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(AllCostActivity.this,  2, btnDate, calendar);
            }
        });
        initText();//为原始数据添加数据
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter=new AllTextMasterAdapter(this,data_1);
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
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(AllCostActivity.this,  2, btnDate, calendar);;
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
        return R.layout.activity_costall;
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
    private void setDialog() {
        Dialog mCameraDialog = new Dialog(this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.activity_pluspeople, null);
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
    private void showPopWindow() {
        //定义一个view，其中包含popwindow的布局文件
        View view1= LayoutInflater.from(AllCostActivity.this).inflate(R.layout.footer_batch,null);
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
                View rootView= LayoutInflater.from(AllCostActivity.this).inflate(R.layout.activity_costall,null);
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
        View rootView=LayoutInflater.from(AllCostActivity.this).inflate(R.layout.activity_costall,null);
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);//展示自定义的popwindow，（放哪个布局里，放布局里的位置，x，y），cv工程
        View view2=LayoutInflater.from(AllCostActivity.this).inflate(R.layout.ppw_delete,null);
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
        allTextList22.clear();
        AllText vegetable1=new AllText("1.蔬菜支出1\n200元");
        allTextList11.add(vegetable1);
        AllText vegetable2=new AllText("2.蔬菜支出2\n300元");
        allTextList11.add(vegetable2);
        AllText vegetable3=new AllText("3.蔬菜支出3\n400元");
        allTextList11.add(vegetable3);
        AllText vegetable4=new AllText("1.蔬菜支出4\n900元");
        allTextList22.add(vegetable4);

        AllText other1=new AllText("4.其他支出1\n200元");
        allTextList11.add(other1);
        AllText other2=new AllText("5.其他支出2\n300元");
        allTextList11.add(other2);
        AllText other3=new AllText("6.其他支出3\n400元");
        allTextList11.add(other3);
        AllText other4=new AllText("2.其他支出4\n900元");
        allTextList22.add(other4);

        AllText fish1=new AllText("7.鱼支出1\n200元");
        allTextList11.add(fish1);
        AllText fish2=new AllText("8.鱼支出2\n300元");
        allTextList11.add(fish2);
        AllText fish3=new AllText("9.鱼支出3\n400元");
        allTextList11.add(fish3);
        AllText fish4=new AllText("3.鱼支出4\n900元");
        allTextList22.add(fish4);

        AllText feed1=new AllText("10.饲料支出1\n200元");
        allTextList11.add(feed1);
        AllText feed2=new AllText("11.饲料支出2\n300元");
        allTextList11.add(feed2);
        AllText feed3=new AllText("12.饲料支出3\n400元");
        allTextList11.add(feed3);
        AllText feed4=new AllText("4.饲料支出4\n900元");
        allTextList22.add(feed4);

        AllText energy1=new AllText("13.能源支出1\n200元");
        allTextList11.add(energy1);
        AllText energy2=new AllText("14.能源支出2\n300元");
        allTextList11.add(energy2);
        AllText energy3=new AllText("15.能源支出3\n400元");
        allTextList11.add(energy3);
        AllText energy4=new AllText("5.能源支出4\n900元");
        allTextList22.add(energy4);

        AllText one1=new AllText("16.工资1\n2000元");
        allTextList11.add(one1);
        AllText two2=new AllText("17.工资2\n3000元");
        allTextList11.add(two2);
        AllText three3=new AllText("18.工资3\n4000元");
        allTextList11.add(three3);
        AllTextMaster add11=new AllTextMaster("4月10日",allTextList11);
        data_2.add(add11);
        AllText one2=new AllText("6.工资4\n9000元");
        allTextList22.add(one2);
        AllTextMaster add22=new AllTextMaster("4月9日",allTextList22);
        data_2.add(add22);
    }
}
