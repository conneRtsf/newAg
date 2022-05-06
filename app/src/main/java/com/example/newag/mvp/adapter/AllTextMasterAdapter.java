package com.example.newag.mvp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.newag.R;
import com.example.newag.mvp.model.bean.AllText;
import com.example.newag.mvp.model.bean.AllTextMaster;


import java.util.ArrayList;
import java.util.List;

//基于BaseQuickAdapter
public class AllTextMasterAdapter extends BaseQuickAdapter<AllTextMaster, BaseViewHolder> {
    Boolean flag=false;
    public final List<Integer> idList=new ArrayList<>();
    public  List<String> numList=new ArrayList<>();
    public AllTextAdapter allTextAdapter;

    private final List<AllText> allTextList=new ArrayList<>();//用来传入数据

    public AllTextMasterAdapter(Context context, @Nullable List<AllTextMaster> data) {
        super(R.layout.ltem_big_list, data);//传入边框布局，数据
        this.mContext=context;//传入上下文
    }

    @SuppressLint("NotifyDataSetChanged")

    private MasterOnItemListener masterOnItemListener;
    public  interface MasterOnItemListener {
        void OnItemClickListener(View view, int position);
        void OnItemLongClickListener(View view, int position,AllText allText);
    }

    public void setMasterOnItemListener(MasterOnItemListener masterOnItemListener) {
        this.masterOnItemListener = masterOnItemListener;
    }


    @Override
    protected void convert(BaseViewHolder helper, AllTextMaster item) {
        String num=item.getItem_Type();
        allTextList.clear();//除去旧数据
        try {
            helper.setText(R.id.tv_group_title,""+item.getItem_Type());//设置标题
        }catch (Exception e){
            e.printStackTrace();
        }
        allTextList.addAll(item.getAllTextList());//添加新数据
        allTextAdapter=new AllTextAdapter(allTextList);//创建小适配器，并传入数据
        allTextAdapter.setOnItemListenerListener(new AllTextAdapter.OnItemListener() {
            @Override
            public void OnItemClickListener(View view, int position) {

            }

            @Override
            public void OnItemLongClickListener(View view, int position, AllText allText) {
                masterOnItemListener.OnItemLongClickListener(view,position,allText);
            }
        });
        allTextAdapter.setOnChangeListener(new AllTextAdapter.OnChangeListener() {
            @Override
            public void onChangeClickListener(CompoundButton compoundButton, int id) {
                boolean flag=true;
                for (int i = 0; i < idList.size(); i++) {
                    if(idList.get(i).equals(id)){
                        if(numList.get(i).equals(num)){
                            flag=false;
                        }
                    }
                }
                if (flag){
                    idList.add(id);
                    numList.add(num);
                }
                Log.e( "id: ", idList.toString());
                Log.e("num: ", numList.toString());
            }
        });
        ((RecyclerView)helper.getView(R.id.recyclerview)).setLayoutManager(new LinearLayoutManager(mContext,RecyclerView.VERTICAL,false));//传入布局
        ((RecyclerView) helper.getView(R.id.recyclerview)).setAdapter(allTextAdapter);//设置适配器
        if (flag){
            allTextAdapter.setCheckbox(true);
        }
        allTextAdapter.notifyDataSetChanged();//提醒数据发生改变
    }
    public void setCheckbox(Boolean flag){
        this.flag=flag;
    }

}
