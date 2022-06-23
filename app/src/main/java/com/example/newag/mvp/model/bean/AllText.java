package com.example.newag.mvp.model.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

//自定义小类
public class AllText implements Serializable {
    private String name;
    private String data;
    private int id;
    private Bitmap bm;

    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public AllText(String name, String data, int id,Bitmap bm){
        this.data=data;
        this.name=name;
        this.id=id;
        this.bm=bm;
    }
    public AllText(String name, String data, int id){
        this.data=data;
        this.name=name;
        this.id=id;
    }
    public AllText(String name,int id){
        this.name=name;
        this.id=id;
    }
    public AllText(String name){
        this.name=name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setImageId(int imageId) {
        this.id = imageId;
    }

    public String getName(){
        return name;
    }
    public int getNum(){
        return id;
    }
}
