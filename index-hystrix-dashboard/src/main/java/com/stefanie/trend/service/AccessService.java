package com.stefanie.trend.service;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpUtil;

public class AccessService {
  
    public static void main(String[] args) {
          
        while(true) {
            ThreadUtil.sleep(1000);
            try {
                String html= HttpUtil.get("http://127.0.0.1:8051/simulate?serviceCharge=0&sellThreshold=0.99&buyThreshold=1.01&ma=20&code=sz159998&getStartDate=null&getEndDate=null");
                System.out.println("html length:" + html.length());
            }
            catch(Exception e) {
                System.err.println(e.getMessage());
            }

  
        }
          
    }
}