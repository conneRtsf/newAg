package com.example.newag.mvp.model.bean;
//自定义小类
public class AllText {
    private String name;
    private int id;
    public AllText(String name,int id){
        this.name=name;
        this.id=id;
    }
    public AllText(String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }
    public int getNum(){
        return id;
    }
}
