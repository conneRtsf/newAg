package com.example.newag.mvp.model.bean;

public class Icon {
        private String iName;
        private CharSequence id;
        private int realId;

    public int getRealId() {
        return realId;
    }

    public void setRealId(int realId) {
        this.realId = realId;
    }

    public CharSequence getId() {
        return id;
    }

    public void setId(CharSequence id) {
        this.id = id;
    }

    public Icon() {
        }
    public Icon(String iName,CharSequence id,int realId) {
        this.iName = iName;
        this.id=id;
        this.realId=realId;
    }
        public Icon(String iName,CharSequence id) {
            this.iName = iName;
            this.id=id;
        }

        public String getiName() {
            return iName;
        }

        public void setiName(String iName) {
            this.iName = iName;
        }
}
