package com.example.newag.mvp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newag.R;
import com.example.newag.mvp.model.bean.AllText;

import java.util.List;

public class AllTextAdapter extends RecyclerView.Adapter<AllTextAdapter.ViewHolder> {
    Boolean flag=false;
    private List<AllText> mAllTextList;//定义一个新的arraylist,数据类型为自定义的AllText
    //写获取布局中相关控件的方法
    static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        ImageView testImage;
        TextView textName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox= (CheckBox) itemView.findViewById(R.id.cb);
            testImage=(ImageView) itemView.findViewById(R.id.test_image);
            textName=(TextView) itemView.findViewById(R.id.test_text);
        }
    }
    public void setCheckbox(Boolean flag){
        this.flag=flag;
    }
    public AllTextAdapter(List<AllText> allTextList){
        mAllTextList=allTextList;
    }//重载，传入数据的方法

    //开始的触发事件（自动创建的构造）
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //获取具体布局
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_small_list,parent,false);
        //LayoutInflater.from(parent.getContext()).inflate(R.layout.xxx,parent,false);重复
        ViewHolder holder=new ViewHolder(view);//获取具体布局中的具体控件
        return holder;
    }
    //中间过程触发事件（自动创建的构造）
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AllText allText=mAllTextList.get(position);//获取当前位置的数据，我的理解是for循环的游标
        //为控件添加数据
        holder.textName.setText(allText.getName());
        holder.testImage.setImageResource(allText.getImageId());
        if (flag==false){
            holder.checkBox.setVisibility(View.INVISIBLE);
        }else holder.checkBox.setVisibility(View.VISIBLE);
    }
    //返回recycleview的行数，这里返回mAllTextList数据个数
    @Override
    public int getItemCount() {
        return mAllTextList.size();
    }


}
