package com.example.newag.mvp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newag.R;
import com.example.newag.mvp.model.bean.AllText;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import java.util.List;

public class AllTextAdapter extends RecyclerView.Adapter<AllTextAdapter.ViewHolder> {
    Boolean flag=false;
    private OnChangeListener onChangeListener;
    private OnItemListener onItemListener;

    private final List<AllText> mAllTextList;//定义一个新的arraylist,数据类型为自定义的AllText

    //写获取布局中相关控件的方法
    static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        ImageView testImage;
        TextView textName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox= itemView.findViewById(R.id.cb);
            testImage= itemView.findViewById(R.id.test_image);
            textName= itemView.findViewById(R.id.test_text);
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
        return new ViewHolder(view);
    }
    //中间过程触发事件（自动创建的构造）
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        int position = holder.getLayoutPosition();
        AllText allText=mAllTextList.get(position);//获取当前位置的数据，我的理解是for循环的游标
        //为控件添加数据
        int id=allText.getNum();
        holder.textName.setText(allText.getName());
        if (!flag){
            holder.checkBox.setVisibility(View.INVISIBLE);
        }else holder.checkBox.setVisibility(View.VISIBLE);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onItemListener!=null){
                    onItemListener.OnItemLongClickListener(view,position,allText);
                }
                return true;
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if(onChangeListener!=null){
                        onChangeListener.onChangeClickListener(compoundButton,id);
                    }
                }
            }
        });
    }
    //返回recycleview的行数，这里返回mAllTextList数据个数
    @Override
    public int getItemCount() {
        return mAllTextList.size();
    }

    public  interface OnItemListener{
        void OnItemClickListener(View view, int position);
        void OnItemLongClickListener(View view, int position,AllText allText);
    }
    //接口回调步骤3：实例化 暴露给外面的调用者，定义Listener的方法（）
    public void setOnItemListenerListener(OnItemListener listener){
        this.onItemListener = listener;
    }


    public boolean getBox(){
        return flag;
    }
    public interface OnChangeListener{
        void  onChangeClickListener(CompoundButton compoundButton,int id);
    }
    public void setOnChangeListener(OnChangeListener listener){
        this.onChangeListener=listener;
    }
}
