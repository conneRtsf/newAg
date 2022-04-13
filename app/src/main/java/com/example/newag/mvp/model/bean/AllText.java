package com.example.newag.mvp.model.bean;
//自定义小类
public class AllText {
    private String name;
    private int imageId;
    public AllText(String name){
//        this.imageId=imageId;
        this.name=name;
    }
    public String getName(){
        return name;
    }
    public int getImageId(){
        return imageId;
    }
}
