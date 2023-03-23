package com.stefanie.trend.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Index implements Serializable {
    String code;
    String name;
    public Index(){

    }
    public Index(String code, String name){
        this.code=code;
        this.name=name;
    }

}
