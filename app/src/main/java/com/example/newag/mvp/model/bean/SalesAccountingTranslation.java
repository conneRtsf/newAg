package com.example.newag.mvp.model.bean;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Map;

public class SalesAccountingTranslation {
    private Integer code;
    private String msg;
    private Map<String, JsonArray> data;
    private Long timestamp;

    public Map<String, JsonArray> getData() {
        return data;
    }

    public void setData(Map<String, JsonArray> data) {
        this.data = data;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
