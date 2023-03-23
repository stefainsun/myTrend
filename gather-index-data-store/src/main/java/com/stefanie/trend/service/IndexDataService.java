package com.stefanie.trend.service;

import com.stefanie.trend.pojo.Index;
import com.stefanie.trend.pojo.IndexData;

import java.util.List;

public interface IndexDataService {
    public List<IndexData>  get(String code);
    public List<IndexData>  store(String code);
    public void remove(String code);
    public List<IndexData>  refresh(String code);
}
