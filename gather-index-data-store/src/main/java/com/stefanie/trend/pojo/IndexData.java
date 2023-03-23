package com.stefanie.trend.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class IndexData implements Serializable{
    String date;
    float closePoint;
    public IndexData( String date,float closePoint){
        this.date=date;
        this.closePoint=closePoint;
    }
    public IndexData(){

    }
}
