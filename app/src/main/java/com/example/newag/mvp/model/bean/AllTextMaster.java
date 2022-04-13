package com.example.newag.mvp.model.bean;

import java.util.List;

//自定义大类
public class AllTextMaster {
    private String item_Type;
    private List<AllText> allTextlist;
    public AllTextMaster(String item_Type,List<AllText> allTextlist){
        this.item_Type=item_Type;
        this.allTextlist=allTextlist;
    }
    public String getItem_Type(){return item_Type;}
    public List<AllText> getAllTextList(){return allTextlist;}
}
