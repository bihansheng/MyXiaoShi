package com.example.administrator.myapplication;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/8.
 */
public class Result implements Serializable{
    private static final long serialVersionUID = 1L;
    private String result;
    private String save_path;
    private String name;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSave_path() {
        return save_path;
    }

    public void setSave_path(String save_path) {
        this.save_path = save_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
