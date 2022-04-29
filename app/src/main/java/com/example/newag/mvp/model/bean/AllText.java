package com.example.newag.mvp.model.bean;

import java.io.Serializable;

//自定义小类
public class AllText implements Serializable {
    private String name;
    private int imageId;
    public AllText(String name){
//        this.imageId=imageId;
        this.name=name;
    }
    public AllText(String name,int imageId){
        this.name=name;
        this.imageId=imageId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName(){
        return name;
    }
    public int getImageId(){
        return imageId;
    }
}
