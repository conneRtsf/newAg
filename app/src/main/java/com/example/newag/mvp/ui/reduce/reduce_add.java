package com.example.newag.mvp.ui.reduce;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newag.mvp.model.Book;
import com.example.newag.R;
import com.example.newag.mvp.adapter.ListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class reduce_add extends AppCompatActivity implements View.OnClickListener{
    public Button btnDate;
    private RecyclerView recyclerView;
    private CheckBox checkbox;
    private TextView selected;
    private ListAdapter adapter;
    private EventBus event;
    private boolean isChange = false;
    private final ArrayList<Book> list = new ArrayList<>();
    Calendar calendar= Calendar.getInstance(Locale.CHINA);

    @Subscribe
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reduce_add);
        btnDate= (Button) findViewById(R.id.btn_Date);
        btnDate.setOnClickListener(this);
        SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年\nM月 ");
        Date curDate =  new Date(System.currentTimeMillis());
        String   str   =   formatter.format(curDate);
        btnDate.setText(str);
//        Button bt3=findViewById(R.id.ib1);
//        bt3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CheckBox ch= LayoutInflater.from(reduce_add.this).inflate(R.layout.activity_main_item, null).findViewById(R.id.checkbox);
//                ch.setVisibility(View.GONE);
//            }
//        });
        initView();
        initData();
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
    @Override
    public void onClick(View view) {
        showDatePickerDialog(this,  2, btnDate, calendar);;
        }

    @Subscribe
    public void initView() {
        event = EventBus.getDefault();
        event.register(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        selected = (TextView) findViewById(R.id.selected);
    }

    public void initData() {
        for (int i = 0; i < 20; i++) {
            Book model = new Book();
            model.setId(i);
            model.setName("商品" + i);
            model.setDesc("描述" + i);
            list.add(model);
        }
        adapter = new ListAdapter(list, event);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();
                    int count = 0;
                    if (isChecked) {
                        isChange = false;
                    }
                    for (int i = 0, p = list.size(); i < p; i++) {
                        if (isChecked) {
                            map.put(i, true);
                            count++;
                        } else {
                            if (!isChange) {
                                map.put(i, false);
                                count = 0;
                            } else {
                                map = adapter.getMap();
                                count = map.size();
                            }
                        }
                    }
                    selected.setText("已选" + count + "项");
                    adapter.setMap(map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        adapter.setOnItemClickListener(new ListAdapter.ItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                Log.e("onItemClick", "" + position);
            }

            @Override
            public void onItemLongClick(final RecyclerView.ViewHolder holder, final int position) {
                Log.e("onItemLongClick", "" + position);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        event.unregister(this);
    }
}


