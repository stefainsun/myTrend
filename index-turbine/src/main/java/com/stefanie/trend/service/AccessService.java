package com.stefanie.trend.service;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpUtil;
 
public class AccessService {
    public static void main(String[] args) {
        while(true) {
            ThreadUtil.sleep(1000);
            access(8051);
//            access(8052);
        }
    }
 
    public static void access(int port) {
        try {
            String html= HttpUtil.get(String.format("http://127.0.0.1:"+port+"/simulate?serviceCharge=0&sellThreshold=0.99&buyThreshold=1.01&ma=20&code=sz159998&getStartDate=null&getEndDate=null",port));
            System.out.printf("%d 地址的模拟回测服务访问成功，返回大小是 %d%n" ,port, html.length());
        }
        catch(Exception e) {
            System.err.printf("%d 地址的模拟回测服务无法访问%n",port);
        }
    }
}