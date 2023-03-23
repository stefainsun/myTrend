package com.stefanie.trend.service;

import com.stefanie.trend.pojo.IndexData;

import java.util.List;

public interface IndexDataService {
    public List<IndexData> getData(String code);
}
