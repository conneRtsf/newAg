package com.example.newag.mvp.adapter;

import android.content.Context;

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
public class AllTextMasterAdapter extends BaseQuickAdapter<AllTextMaster,BaseViewHolder> {
    Boolean flag=false;
    private List<AllText> allTextList=new ArrayList<>();//用来传入数据
    public AllTextMasterAdapter(Context context, @Nullable List<AllTextMaster> data) {
        super(R.layout.ltem_big_list, data);//传入边框布局，数据
        this.mContext=context;//传入上下文
    }
    @Override
    protected void convert(BaseViewHolder helper, AllTextMaster item) {
        allTextList.clear();//除去旧数据
        try {
            helper.setText(R.id.tv_group_title,""+item.getItem_Type());//设置标题
        }catch (Exception e){
            e.printStackTrace();
        }

        allTextList.addAll(item.getAllTextList());//添加新数据
        com.example.newag.mvp.adapter.AllTextAdapter allTextAdapter=new com.example.newag.mvp.adapter.AllTextAdapter(allTextList);//创建小适配器，并传入数据
        ((RecyclerView)helper.getView(R.id.recyclerview)).setLayoutManager(new LinearLayoutManager(mContext,RecyclerView.VERTICAL,false));//传入布局
        /*((RecyclerView)helper.getView(R.id.recyclerview)).addItemDecoration(new HomeSpaceItemDecoration(1,Screen));*/
        ((RecyclerView) helper.getView(R.id.recyclerview)).setAdapter(allTextAdapter);//设置适配器
        if (flag==true){
            allTextAdapter.setCheckbox(true);
        }
        allTextAdapter.notifyDataSetChanged();//提醒数据发生改变
    }
    public void setCheckbox(Boolean flag){
        this.flag=flag;
    }
}
